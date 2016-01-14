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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.responses.BitcoinNodeExampleResponses;
import org.junit.Before;
import org.junit.Test;

public class BlockchainResourceTest {

    private BlockchainResource blockchainResource;

    @Before
    public final void setUp() throws Exception {
        BitcoinNodeService bitcoinNodeService = mock(BitcoinNodeService.class);

        when(bitcoinNodeService.getBlockchainInfo(any(BitcoinNodeRequest.class))).thenReturn(
                BitcoinNodeExampleResponses.getBlockchainInfoResponse());
        when(bitcoinNodeService.getBlockHeader(any(BitcoinNodeRequest.class))).thenReturn(
                BitcoinNodeExampleResponses.getBlockHeaderResponse());

        blockchainResource = new BlockchainResource(bitcoinNodeService);
    }

    @Test
    public final void testGetBlockchainInfo() throws Exception {
        assertEquals(
                BitcoinNodeExampleResponses.getBlockchainInfoResponse().getResult().get(),
                blockchainResource.getBlockchainInfo("dummy auth header"));
    }

    @Test
    public final void testGetBlockHeader() throws Exception {

        Sha256Hash headerHash = BitcoinNodeExampleResponses.getBlockHeaderResponse().getResult().get().getHeaderHash();
        assertEquals(
                BitcoinNodeExampleResponses.getBlockHeaderResponse().getResult().get(),
                blockchainResource.getBlockHeader("dummy auth header", headerHash));
    }
}
