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

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.drausin.bitflow.blockchain.api.objects.ImmutableBlockchainInfo;
import org.drausin.bitflow.blockchain.api.objects.mixin.BlockchainInfoRpcMixIn;
import org.drausin.bitflow.blockchain.api.serde.BlockchainInfoDeserializer;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcResponse;
import org.drausin.bitflow.blockchain.client.objects.ImmutableBitcoindRpcResponse;
import org.junit.Before;
import org.junit.Test;

public class BlockchainInfoResponseMapperProviderTest {

    private BlockchainInfoResponseMapperProvider provider;
    private String rpcResultResponseJson;

    @Before
    public void setUp() throws Exception {
        provider = new BlockchainInfoResponseMapperProvider();

        rpcResultResponseJson = "{\n"
                + "    \"result\": {\n"
                + "         \"chain\" : \"test\",\n"
                + "         \"blocks\" : 315280,\n"
                + "         \"headers\" : 315280,\n"
                + "         \"bestblockhash\" : \"000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f\",\n"
                + "         \"difficulty\" : 1.00000000,\n"
                + "         \"verificationprogress\" : 1.00000778,\n"
                + "         \"chainwork\" : \"0000000000000000000000000000000000000000000000015e984b4fb9f9b350\"\n"
                + "    },\n"
                + "    \"error\": null,\n"
                + "    \"id\": \"foo\"\n"
                + "}";
    }

    @Test
    public void testGetContext() throws Exception {
        ObjectMapper context = provider.getContext(BitcoindRpcResponse.class);
        BitcoindRpcResponse resultResponse = context.readValue(rpcResultResponseJson, ImmutableBitcoindRpcResponse.class);
        assertTrue(resultResponse.validateResult(BlockchainInfo.class));
    }
}