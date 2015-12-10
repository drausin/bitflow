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

package org.drausin.bitflow.blockchain.client.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jayway.jsonpath.JsonPath;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.drausin.bitflow.blockchain.api.objects.ImmutableBlockHeader;
import org.drausin.bitflow.blockchain.api.objects.mixin.BlockHeaderRpcMixIn;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.api.serde.BlockHeaderDeserializer;
import org.drausin.bitflow.blockchain.api.serde.Sha256HashSerializer;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class BitcoindRpcBlockHeaderResponseTest {

    private ObjectMapper rpcMapper;
    private String rpcResultResponseJson;
    private String rpcErrorResponseJson;
    private BitcoindRpcResponse resultResponse;
    private BitcoindRpcResponse errorResponse;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {

        SimpleModule testModule = new SimpleModule("TestModule", Version.unknownVersion());
        testModule.addSerializer(Sha256Hash.class, new Sha256HashSerializer());
        testModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());
        testModule.addDeserializer(BlockchainResult.class, new BlockHeaderDeserializer());

        rpcMapper = new ObjectMapper();
        rpcMapper.registerModule(new GuavaModule());
        rpcMapper.registerModule(testModule);
        rpcMapper.addMixIn(ImmutableBlockHeader.class, BlockHeaderRpcMixIn.class);

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

        rpcErrorResponseJson = "{\n"
                + "    \"result\": null,\n"
                + "    \"error\": {\"code\": 1, \"message\": \"test error message\"},\n"
                + "    \"id\": \"foo\"\n"
                + "}";

        resultResponse = rpcMapper.readValue(rpcResultResponseJson, ImmutableBitcoindRpcResponse.class);
        errorResponse = rpcMapper.readValue(rpcErrorResponseJson, ImmutableBitcoindRpcResponse.class);
    }

    @Test
    public final void testValidResult() throws Exception {
        assertTrue(resultResponse.validateResult(BlockHeader.class));
    }

    @Test
    public final void testGetResult() throws Exception {
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.hash"),
                ((BlockHeader) resultResponse.getResult().get()).getHeaderHash().toString());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.confirmations")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getNumConfirmations());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.size")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getSizeBytes());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.height")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getHeight());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.version")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getVersion());
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.merkleroot"),
                ((BlockHeader) resultResponse.getResult().get()).getMerkleRoot().toString());
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.tx").toString(),
                rpcMapper.writeValueAsString(((BlockHeader) resultResponse.getResult().get()).getTransactionIds()));
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.time")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getCreatedTime());
        assertEquals(
                ((Integer) JsonPath.read(rpcResultResponseJson, "$.result.nonce")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getNonce());
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcResultResponseJson, "$.result.bits"), "0"),
                ((BlockHeader) resultResponse.getResult().get()).getDifficultyTarget().toString(16));
        assertEquals(
                (double) JsonPath.read(rpcResultResponseJson, "$.result.difficulty"),
                ((BlockHeader) resultResponse.getResult().get()).getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcResultResponseJson, "$.result.chainwork").toString(), "0"),
                ((BlockHeader) resultResponse.getResult().get()).getChainwork().toString(16));
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.previousblockhash"),
                ((BlockHeader) resultResponse.getResult().get()).getPreviousBlockHash().toString());
        assertEquals(
                JsonPath.read(rpcResultResponseJson, "$.result.nextblockhash"),
                ((BlockHeader) resultResponse.getResult().get()).getNextBlockHash().toString());
    }

    @Test
    public final void testValidError() throws Exception {
        assertTrue(errorResponse.validateError());
    }

    @Test
    public final void testGetError() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcErrorResponseJson, "$.error.code")).longValue(),
                errorResponse.getError().get().getCode());
        assertEquals(
                JsonPath.read(rpcErrorResponseJson, "$.error.message"),
                errorResponse.getError().get().getMessage());
    }

    @Test
    public final void testGetId() throws Exception {
        assertEquals(JsonPath.read(rpcResultResponseJson, "$.id"), resultResponse.getId().get());
    }

    @Test
    public final void testOf() throws Exception {
        BitcoindRpcResponse testResultResponse = BitcoindRpcResponse.of(resultResponse.getResult(),
                resultResponse.getError(), resultResponse.getId());
        assertThat(testResultResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testResultResponse.getResult().get(), CoreMatchers.instanceOf(ImmutableBlockHeader.class));

        BitcoindRpcResponse testErrorResponse = BitcoindRpcResponse.of(errorResponse.getResult(),
                errorResponse.getError(), errorResponse.getId());
        assertThat(testErrorResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testErrorResponse.getError().get(),
                CoreMatchers.instanceOf(ImmutableBitcoindRpcResponseError.class));
    }
}
