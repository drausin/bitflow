/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drausin.bitflow.blockchain;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.dropwizard.jersey.params.DateTimeParam;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequestFactory;
import org.drausin.bitflow.bitcoin.api.responses.BlockHeaderHashResponse;
import org.drausin.bitflow.bitcoin.api.responses.BlockHeaderResponse;
import org.drausin.bitflow.bitcoin.api.responses.BlockchainInfoResponse;
import org.drausin.bitflow.blockchain.api.BlockchainService;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.service.utils.BitflowResource;
import org.joda.time.DateTime;

public class BlockchainResource extends BitflowResource implements BlockchainService {

    private BitcoinNodeService bitcoinNodeService;

    public BlockchainResource(BitcoinNodeService bitcoinNodeService) {
        this.bitcoinNodeService = bitcoinNodeService;
    }

    @Override
    public final BlockchainInfo getBlockchainInfo(String authHeader) {
        // TODO(dwulsin): what to do with authHeader?
        BlockchainInfoResponse blockchainInfoResponse = bitcoinNodeService.getBlockchainInfo(
                BitcoinNodeRequestFactory.createBlockchainInfoRequest());
        blockchainInfoResponse.validateResult();
        return blockchainInfoResponse.getResult().get();
    }

    @Override
    public final BlockHeader getBlockHeader(String authHeader, Sha256Hash hash) {
        // TODO(dwulsin): what to do with authHeader?
        BlockHeaderResponse blockHeaderResponse = bitcoinNodeService.getBlockHeader(
                BitcoinNodeRequestFactory.createBlockHeaderRequest(hash));
        blockHeaderResponse.validateResult();
        return blockHeaderResponse.getResult().get();
    }

    @Override
    public final List<BlockHeader> getBlockHeaderTimeSubchain(String authHeader, DateTimeParam from, DateTimeParam to) {
        // TODO(dwulsin): what to do with authHeader?

        BlockchainInfo blockchainInfo = getBlockchainInfo(authHeader);
        BlockHeader earliest = getBlockHeaderAtHeight(blockchainInfo.getPruneHeight());
        BlockHeader latest = getBlockHeaderAtHeight(blockchainInfo.getNumBlocks());

        Preconditions.checkArgument(from.get().isAfter(earliest.getCreatedDateTime().toInstant()),
                "time from=%s is before than lowest available (non-pruned) block at time %s", from.toString(),
                earliest.getCreatedDateTime().toString());
        Preconditions.checkArgument(to.get().isBefore(latest.getCreatedDateTime().toInstant()),
                "time to=%s is after than latest available block at time %s", to.toString(),
                latest.getCreatedDateTime().toString());

        BlockHeader fromBlock = findBlockHeaderAtTime(from.get(), earliest, latest, true);
        BlockHeader toBlock = findBlockHeaderAtTime(to.get(), fromBlock, latest, false);
        long numBlocks = toBlock.getHeight() - fromBlock.getHeight() + 1;

        return getBlockHeaderSubchain(fromBlock.getHeaderHash(), toBlock.getHeaderHash(), numBlocks);
    }

    @Override
    public final List<BlockHeader> getBlockHeaderHeightSubchain(String authHeader, long from, long to) {
        // TODO(dwulsin): what to do with authHeader?

        BlockchainInfo blockchainInfo = getBlockchainInfo(authHeader);

        Preconditions.checkArgument(from >= blockchainInfo.getPruneHeight(),
                "height from=%s is smaller than lowest available (non-pruned) block at height %s", from,
                blockchainInfo.getPruneHeight());
        Preconditions.checkArgument(to <= blockchainInfo.getNumBlocks(),
                "height to=%s is greater than highest available block at height %s", to, blockchainInfo.getNumBlocks());

        // get the block header hashes at the starting and ending heights
        Sha256Hash fromHash = getBlockHeaderHashAtHeight(from);
        Sha256Hash toHash = getBlockHeaderHashAtHeight(to);

        return getBlockHeaderSubchain(fromHash, toHash, to - from + 1);
    }

    private List<BlockHeader> getBlockHeaderSubchain(Sha256Hash fromHash, Sha256Hash toHash, long numBlocks) {

        List<BlockHeader> subchain = Lists.newArrayList();
        Sha256Hash nextHash = fromHash;
        for (int c = 0; c < numBlocks; c++) {
            BlockHeaderResponse currentResponse = bitcoinNodeService.getBlockHeader(
                    BitcoinNodeRequestFactory.createBlockHeaderRequest(nextHash));
            currentResponse.validateResult();
            BlockHeader currentBlockHeader = currentResponse.getResult().get();
            subchain.add(currentBlockHeader);
            nextHash = currentBlockHeader.getNextBlockHash().get();
        }

        // check that the subchain hasn't changed during the call
        Sha256Hash lastHash = subchain.get(subchain.size() - 1).getHeaderHash();
        if (!lastHash.equals(toHash)) {
            throw new IllegalStateException(
                    String.format("'to' header hash changed from %s to %s during call", lastHash.toString(),
                            toHash.toString()));
        }

        return ImmutableList.copyOf(subchain);
    }

    private Sha256Hash getBlockHeaderHashAtHeight(long height) {
        BlockHeaderHashResponse response = bitcoinNodeService.getBlockHeaderHash(
                BitcoinNodeRequestFactory.createBlockHeaderHashRequest(height));
        response.validateResult();
        return response.getResult().get();
    }

    private BlockHeader getBlockHeaderAtHeight(long height) {
        return getBlockHeader("dummy header", getBlockHeaderHashAtHeight(height));
    }

    private BlockHeader findBlockHeaderAtTime(DateTime time, BlockHeader lowerBound, BlockHeader upperBound,
            boolean useBlockBefore) {

        Preconditions.checkArgument(!time.isBefore(lowerBound.getCreatedDateTime().toInstant()),
                "time %s is below lower bound %s", time.toString(), lowerBound.getCreatedDateTime().toString());
        Preconditions.checkArgument(!time.isAfter(upperBound.getCreatedDateTime().toInstant()),
                "time %s is above upper bound %s", time.toString(), upperBound.getCreatedDateTime().toString());

        long middleHeight = lowerBound.getHeight() + (upperBound.getHeight() - lowerBound.getHeight()) / 2;
        BlockHeader middle = getBlockHeaderAtHeight(middleHeight);

        if (lowerBound.getHeight() + 1 == upperBound.getHeight()) {
            // base case
            return useBlockBefore ? lowerBound : upperBound;
        } else if (time.isBefore(middle.getCreatedDateTime().toInstant())) {
            // recurse into first half
            return findBlockHeaderAtTime(time, lowerBound, middle, useBlockBefore);
        } else {
            // recurse into second half
            return findBlockHeaderAtTime(time, middle, upperBound, useBlockBefore);
        }

    }
}
