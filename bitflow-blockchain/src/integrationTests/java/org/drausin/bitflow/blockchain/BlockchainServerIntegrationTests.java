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

import static org.junit.Assert.assertTrue;

import io.dropwizard.jersey.params.DateTimeParam;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.validation.SubchainValidator;
import org.drausin.bitflow.integration.AbstractIntegrationTest;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

public final class BlockchainServerIntegrationTests extends AbstractIntegrationTest {

    private static final String authHeader = "dummy authHeader";

    @Test
    public void testGetBlockchainInfo() {

        // ideally we would be able to hit the BitcoinNode endpoint directly, but the rpcallowip configuration parameter
        // means that we can only hit that endpoint from within the docker-compose 'bitflow' network, so for now the
        // commented portions won't work :/

        /*
        // get from the BitcoinNode endpoint
        BitcoinNodeRequest blockchainInfoRequest = BitcoinNodeRequestFactory.createBlockchainInfoRequest();
        BlockchainInfoResponse bitcoinNodeBlockchainInfoResponse = getBitcoinNode().getBlockchainInfo(
                blockchainInfoRequest);
        bitcoinNodeBlockchainInfoResponse.validateResult();
        BlockchainInfo bitcoinNodeBlockchainInfo = bitcoinNodeBlockchainInfoResponse.getResult().get();
        */

        // get from the Blockchain endpoint
        BlockchainInfo blockchainBlockchainInfo = getBlockchain().getBlockchainInfo(authHeader);
        assertTrue(blockchainBlockchainInfo.getNumBlocks() > 0);

        /*
        // this stuff should be the same
        assertEquals(bitcoinNodeBlockchainInfo.getChain(), blockchainBlockchainInfo.getChain());
        assertEquals(bitcoinNodeBlockchainInfo.getPruned(), blockchainBlockchainInfo.getPruned());

        // the second Blockchain end point call should have a block greater than or equal to the first
        assertTrue(bitcoinNodeBlockchainInfo.getNumBlocks() <= blockchainBlockchainInfo.getNumBlocks());
        assertTrue(bitcoinNodeBlockchainInfo.getNumHeaders() <= blockchainBlockchainInfo.getNumHeaders());
        assertTrue(bitcoinNodeBlockchainInfo.getVerificationProgress()
                <= bitcoinNodeBlockchainInfo.getVerificationProgress());
        assertTrue(bitcoinNodeBlockchainInfo.getPruneHeight() <= blockchainBlockchainInfo.getPruneHeight());
        */
    }

    @Test
    public void testGetBlockHeaderUnpruned() {

        BlockchainInfo blockchainInfo = getBlockchain().getBlockchainInfo(authHeader);
        BlockHeader blockHeader = getBlockchain().getBlockHeader(authHeader, blockchainInfo.getBestBlockHash());

        assertTrue(blockHeader.getTransactionIds().size() > 0);

    }

    @Test(expected = RuntimeException.class)
    public void testGetBlockHeaderPruned() {

        // make request for block that's probably not available, which should result in a RuntimeException
        Sha256Hash headerHash = Sha256Hash.wrap("000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39");
        getBlockchain().getBlockHeader(authHeader, headerHash);
    }

    @Test
    public void testGetBlockHeaderHeightSubchain() {

        BlockchainInfo blockchainInfo = getBlockchain().getBlockchainInfo(authHeader);
        long toBlockHeight = blockchainInfo.getNumBlocks();
        long fromBlockHeight = toBlockHeight - 10;
        List<BlockHeader> subchain = getBlockchain().getBlockHeaderHeightSubchain(authHeader, fromBlockHeight,
                toBlockHeight);

        SubchainValidator.validateSubchain(subchain, toBlockHeight - fromBlockHeight + 1);
    }

    @Test
    public void testGetBlockHeaderTimeSubchain() {

        BlockchainInfo blockchainInfo = getBlockchain().getBlockchainInfo(authHeader);
        BlockHeader toBlock = getBlockchain().getBlockHeader(authHeader, blockchainInfo.getNumBlocks());
        BlockHeader fromBlock = getBlockchain().getBlockHeader(authHeader, blockchainInfo.getNumBlocks() - 6 * 30);
        DateTimeParam toTimeParam = new DateTimeParam(
                toBlock.getCreatedDateTime().toString(ISODateTimeFormat.dateTime()));
        DateTimeParam fromTimeParam = new DateTimeParam(
                fromBlock.getCreatedDateTime().toString(ISODateTimeFormat.dateTime()));

        List<BlockHeader> subchain = getBlockchain().getBlockHeaderTimeSubchain(authHeader, fromTimeParam, toTimeParam);
        SubchainValidator.validateSubchain(subchain, fromBlock.getCreatedDateTime(), toBlock.getCreatedDateTime());
    }
}
