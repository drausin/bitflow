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
        // TODO(dwulsin)
        return null;
    }

    @Override
    public final List<BlockHeader> getBlockHeaderHeightSubchain(String authHeader, long from, long to) {
        // TODO(dwulsin): what to do with authHeader?

        BlockchainInfo blockchainInfo = getBlockchainInfo(authHeader);
        if (to > blockchainInfo.getNumBlocks()) {
            throw new RuntimeException(
                    String.format("height to=%s is greater than highest available block at height %s", to,
                            blockchainInfo.getNumBlocks()));
        }
        if (from < blockchainInfo.getPruneHeight()) {
            throw new RuntimeException(
                    String.format("height from=%s is smaller than lowest available (non-pruned) block at height %s",
                            from, blockchainInfo.getPruneHeight()));
        }

        // get the block header hashes at the starting and ending heights
        Sha256Hash fromHash = getBlockHeaderHashAtHeight(from);
        Sha256Hash toHash = getBlockHeaderHashAtHeight(to);

        List<BlockHeader> subchain = Lists.newArrayList();
        Sha256Hash nextHash = fromHash;
        for (int c = 0; c <= to - from; c++) {
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
}
