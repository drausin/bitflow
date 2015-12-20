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

        resultResponse = rpcMapper.readValue(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE,
                ImmutableBitcoindRpcResponse.class);
        errorResponse = rpcMapper.readValue(BitcoindRpcJsonResponses.ERROR_RESPONSE,
                ImmutableBitcoindRpcResponse.class);
    }

    @Test
    public final void testValidResult() throws Exception {
        assertTrue(resultResponse.validateResult(BlockHeader.class));
    }

    @Test
    public final void testGetResult() throws Exception {
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.hash"),
                ((BlockHeader) resultResponse.getResult().get()).getHeaderHash().toString());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.confirmations"))
                        .longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getNumConfirmations());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.size")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getSizeBytes());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.height")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getHeight());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.version")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getVersion());
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.merkleroot"),
                ((BlockHeader) resultResponse.getResult().get()).getMerkleRoot().toString());
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.tx").toString(),
                rpcMapper.writeValueAsString(((BlockHeader) resultResponse.getResult().get()).getTransactionIds()));
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.time")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getCreatedTime());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.nonce")).longValue(),
                ((BlockHeader) resultResponse.getResult().get()).getNonce());
        assertEquals(
                StringUtils.stripStart(JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.bits"),
                        "0"),
                ((BlockHeader) resultResponse.getResult().get()).getDifficultyTarget().toString(16));
        assertEquals(
                (double) JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.difficulty"),
                ((BlockHeader) resultResponse.getResult().get()).getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                StringUtils.stripStart(JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE,
                        "$.result.chainwork").toString(), "0"),
                ((BlockHeader) resultResponse.getResult().get()).getChainwork().toString(16));
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.previousblockhash"),
                ((BlockHeader) resultResponse.getResult().get()).getPreviousBlockHash().toString());
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.result.nextblockhash"),
                ((BlockHeader) resultResponse.getResult().get()).getNextBlockHash().toString());
    }

    @Test
    public final void testValidError() throws Exception {
        assertTrue(errorResponse.validateError());
    }

    @Test
    public final void testGetError() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.ERROR_RESPONSE, "$.error.code")).longValue(),
                errorResponse.getError().get().getCode());
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.ERROR_RESPONSE, "$.error.message"),
                errorResponse.getError().get().getMessage());
    }

    @Test
    public final void testGetId() throws Exception {
        assertEquals(JsonPath.read(BitcoindRpcJsonResponses.BLOCK_HEADER_RESPONSE, "$.id"), resultResponse.getId().get());
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
