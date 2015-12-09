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

package org.drausin.bitflow.blockchain.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jayway.jsonpath.JsonPath;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.drausin.bitflow.blockchain.api.objects.ImmutableBlockchainInfo;
import org.drausin.bitflow.blockchain.api.objects.mixin.BlockchainInfoRpcMixIn;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.api.serde.BlockchainInfoDeserializer;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class BitcoindRpcBlockchainInfoResponseTest {

    private ObjectMapper rpcMapper;
    private String rpcResultResponseJson;
    private String rpcErrorResponseJson;
    private BitcoindRpcResponse resultResponse;
    private BitcoindRpcResponse errorResponse;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {

        SimpleModule testModule = new SimpleModule("TestModule", Version.unknownVersion());
        testModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());
        testModule.addDeserializer(BlockchainResult.class, new BlockchainInfoDeserializer());

        rpcMapper = new ObjectMapper();
        rpcMapper.registerModule(new GuavaModule());
        rpcMapper.registerModule(testModule);
        rpcMapper.addMixIn(ImmutableBlockchainInfo.class, BlockchainInfoRpcMixIn.class);

        // TODO(dwulsin): set error: null once figure out how to skip/modify builder().checkRequiredAttributes()
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
                + "    \"error\": {\"code\": 1, \"message\": \"test error message\"},\n"
                // + "    \"error\": null,\n"
                + "    \"id\": \"foo\"\n"
                + "}";

        // TODO(dwulsin): set result: null once figure out how to skip/modify builder().checkRequiredAttributes()
        rpcErrorResponseJson = "{\n"
                + "    \"result\": {\n"
                + "         \"chain\" : \"test\",\n"
                + "         \"blocks\" : 315280,\n"
                + "         \"headers\" : 315280,\n"
                + "         \"bestblockhash\" : \"000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f\",\n"
                + "         \"difficulty\" : 1.00000000,\n"
                + "         \"verificationprogress\" : 1.00000778,\n"
                + "         \"chainwork\" : \"0000000000000000000000000000000000000000000000015e984b4fb9f9b350\"\n"
                + "    },\n"
                // + "    \"result\": null,\n"
                + "    \"error\": {\"code\": 1, \"message\": \"test error message\"},\n"
                + "    \"id\": \"foo\"\n"
                + "}";

        resultResponse = rpcMapper.readValue(rpcResultResponseJson, ImmutableBitcoindRpcResponse.class);
        errorResponse = rpcMapper.readValue(rpcErrorResponseJson, ImmutableBitcoindRpcResponse.class);
    }

    @Test
    public final void testGetResult() throws Exception {
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.chain"),
                ((BlockchainInfo) resultResponse.getResult()).getChain());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.blocks")).longValue(),
                ((BlockchainInfo) resultResponse.getResult()).getNumBlocks());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.headers")).longValue(),
                ((BlockchainInfo) resultResponse.getResult()).getNumHeaders());
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.bestblockhash"),
                ((BlockchainInfo) resultResponse.getResult()).getBestBlockHash().toString());
        assertEquals(
                (double) JsonPath.read(rpcResultResponseJson, "$.result.difficulty"),
                ((BlockchainInfo) resultResponse.getResult()).getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(rpcResultResponseJson, "$.result.verificationprogress"),
                ((BlockchainInfo) resultResponse.getResult()).getVerificationProgress(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcResultResponseJson, "$.result.chainwork").toString(), "0"),
                ((BlockchainInfo) resultResponse.getResult()).getChainwork().toString(16));
    }

    @Test
    public final void testGetError() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcErrorResponseJson, "$.error.code")).longValue(),
                errorResponse.getError().getCode());
        assertEquals(
                JsonPath.read(rpcErrorResponseJson, "$.error.message"),
                errorResponse.getError().getMessage());
    }

    @Test
    public final void testGetId() throws Exception {
        assertEquals(JsonPath.read(rpcResultResponseJson, "$.id"), resultResponse.getId());
    }

    @Test
    public final void testOf() throws Exception {
        BitcoindRpcResponse testResponse = BitcoindRpcResponse.of(resultResponse.getResult(), resultResponse.getError(),
                resultResponse.getId());
        assertThat(testResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testResponse.getResult(), CoreMatchers.instanceOf(ImmutableBlockchainInfo.class));
        assertThat(testResponse.getError(), CoreMatchers.instanceOf(ImmutableBitcoindRpcResponseError.class));
    }
}
