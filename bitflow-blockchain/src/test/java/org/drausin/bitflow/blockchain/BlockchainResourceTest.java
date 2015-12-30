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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoindRpcService;
import org.drausin.bitflow.bitcoin.api.objects.BitcoindRpcExampleResponses;
import org.junit.Before;
import org.junit.Test;

public class BlockchainResourceTest {

    private BlockchainResource blockchainResource;

    @Before
    public final void setUp() throws Exception {
        BitcoindRpcService bitcoindRpcService = mock(BitcoindRpcService.class);

        when(bitcoindRpcService.getBlockchainInfo()).thenReturn(BitcoindRpcExampleResponses.getBlockchainInfoResult());
        when(bitcoindRpcService.getBlockHeader(any(Sha256Hash.class)))
                .thenReturn(BitcoindRpcExampleResponses.getBlockHeaderResult());

        blockchainResource = new BlockchainResource(bitcoindRpcService);
    }

    @Test
    public final void testGetBlockchainInfo() throws Exception {
        assertThat(
                blockchainResource.getBlockchainInfo("dummy auth header"),
                is(BitcoindRpcExampleResponses.getBlockchainInfoResult()));
    }

    @Test
    public final void testGetBlockHeader() throws Exception {
        assertThat(
                blockchainResource.getBlockHeader("dummy auth header", any(Sha256Hash.class)),
                is(BitcoindRpcExampleResponses.getBlockHeaderResult()));
    }
}
