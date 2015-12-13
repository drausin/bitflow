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
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcResponse;
import org.drausin.bitflow.blockchain.client.objects.ImmutableBitcoindRpcResponse;
import org.junit.Before;
import org.junit.Test;

public class BlockHeaderResponsetMapperProviderTest {

    private BlockHeaderResponseMapperProvider provider;
    private String rpcResultResponseJson;

    @Before
    public void setUp() throws Exception {
        provider = new BlockHeaderResponseMapperProvider();

        rpcResultResponseJson = "{\n"
                + "    \"result\": {\n"
                + "         \"hash\" : \"000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39\",\n"
                + "         \"confirmations\" : 88029,\n"
                + "         \"size\" : 189,\n"
                + "         \"height\" : 227252,\n"
                + "         \"version\" : 2,\n"
                + "         \"merkleroot\" : \"c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a\",\n"
                + "         \"tx\" : [\n"
                + "             \"c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a\"\n"
                + "         ],\n"
                + "         \"time\" : 1398824312,\n"
                + "         \"nonce\" : 1883462912,\n"
                + "         \"bits\" : \"1d00ffff\",\n"
                + "         \"difficulty\" : 1.00000000,\n"
                + "         \"chainwork\" : \"000000000000000000000000000000000000000000000000083ada4a4009841a\",\n"
                + "         \"previousblockhash\" : "
                + "             \"00000000c7f4990e6ebf71ad7e21a47131dfeb22c759505b3998d7a814c011df\",\n"
                + "         \"nextblockhash\" : \"00000000afe1928529ac766f1237657819a11cfcc8ca6d67f119e868ed5b6188\"\n"
                + "     },\n"
                + "    \"error\": null,\n"
                + "    \"id\": \"foo\"\n"
                + "}";

    }

    @Test
    public void testGetContext() throws Exception {
        ObjectMapper context = provider.getContext(BitcoindRpcResponse.class);
        BitcoindRpcResponse resultResponse = context.readValue(rpcResultResponseJson, ImmutableBitcoindRpcResponse.class);
        assertTrue(resultResponse.validateResult(BlockHeader.class));
    }
}