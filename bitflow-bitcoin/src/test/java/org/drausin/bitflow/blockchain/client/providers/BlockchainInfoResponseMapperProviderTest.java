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

package org.drausin.bitflow.blockchain.client.providers;

import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcJsonResponses;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcResponse;
import org.drausin.bitflow.blockchain.client.objects.ImmutableBitcoindRpcResponse;
import org.junit.Before;
import org.junit.Test;

public class BlockchainInfoResponseMapperProviderTest {

    private BlockchainInfoResponseMapperProvider provider;

    @Before
    public final void setUp() throws Exception {
        provider = new BlockchainInfoResponseMapperProvider();
    }

    @Test
    public final void testGetContext() throws Exception {
        ObjectMapper context = provider.getContext(BitcoindRpcResponse.class);
        BitcoindRpcResponse resultResponse = context.readValue(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESPONSE,
                ImmutableBitcoindRpcResponse.class);
        assertTrue(resultResponse.validateResult(BlockchainInfo.class));
    }
}
