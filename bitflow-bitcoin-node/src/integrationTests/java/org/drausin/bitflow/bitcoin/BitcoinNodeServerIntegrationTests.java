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

package org.drausin.bitflow.bitcoin;

import static org.junit.Assert.assertTrue;

import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequestFactory;
import org.drausin.bitflow.bitcoin.api.responses.BlockHeaderResponse;
import org.drausin.bitflow.bitcoin.api.responses.BlockchainInfoResponse;
import org.drausin.bitflow.integration.AbstractIntegrationTest;
import org.junit.Test;

public final class BitcoinNodeServerIntegrationTests extends AbstractIntegrationTest {

    @Test
    public void testGetBlockchainInfo() {

        BitcoinNodeRequest blockchainInfoRequest = BitcoinNodeRequestFactory.createBlockchainInfoRequest();
        BlockchainInfoResponse blockchainInfoResponse = getBitcoinNode().getBlockchainInfo(blockchainInfoRequest);

        assertTrue(blockchainInfoResponse.validateResult());
        assertTrue(blockchainInfoResponse.getResult().get().getNumBlocks() > 0);
    }

    @Test
    public void testGetBlockHeaderUnpruned() {

        // first get blockchain info
        BitcoinNodeRequest blockchainInfoRequest = BitcoinNodeRequestFactory.createBlockchainInfoRequest();
        BlockchainInfoResponse blockchainInfoResponse = getBitcoinNode().getBlockchainInfo(blockchainInfoRequest);
        blockchainInfoResponse.validateResult();

        // make request for best block
        Sha256Hash headerHash = blockchainInfoResponse.getResult().get().getBestBlockHash();
        BitcoinNodeRequest blockHeaderRequest = BitcoinNodeRequestFactory.createBlockHeaderRequest(headerHash);
        BlockHeaderResponse blockHeaderResponse = getBitcoinNode().getBlockHeader(blockHeaderRequest);

        assertTrue(blockHeaderResponse.validateResult());
        assertTrue(blockHeaderResponse.getResult().get().getTransactionIds().size() > 0);

    }

    @Test(expected = RuntimeException.class)
    public void testGetBlockHeaderPruned() {
        // make request for block that's probably not available, which should result in a RuntimeException
        Sha256Hash headerHash = Sha256Hash.wrap("000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39");
        BitcoinNodeRequest blockHeaderRequest = BitcoinNodeRequestFactory.createBlockHeaderRequest(headerHash);
        getBitcoinNode().getBlockHeader(blockHeaderRequest);
    }

    // This shuts down the BitcoinNode service, so commenting out for now as a) not sure how to run this without harming
    // all tests that come after and b) it's a pretty simple test that we can manually run by itself if we want.
    /*
    @Test
    public void testStop() {
        BitcoinNodeRequest stopRequest = BitcoinNodeRequestFactory.createStopRequest();
        StopResponse stopResponse = getBitcoinNode().stop(stopRequest);

        assertTrue(stopResponse.validateResult());
    }
    */
}
