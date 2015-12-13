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
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
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

        resultResponse = rpcMapper.readValue(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT,
                ImmutableBitcoindRpcResponse.class);
        errorResponse = rpcMapper.readValue(BitcoindRpcJsonResponses.ERROR_RESPONSE,
                ImmutableBitcoindRpcResponse.class);
    }

    @Test
    public final void testValidateResult() throws Exception {
        assertTrue(resultResponse.validateResult(BlockchainInfo.class));
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateResultWrongClassException() throws Exception {
        resultResponse.validateResult(BlockHeader.class);
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateResultAbsentResultException() throws Exception {
        BitcoindRpcResponse testResultResponse = BitcoindRpcResponse.of(null, null, resultResponse.getId().get());
        testResultResponse.validateResult(BlockchainInfo.class);
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateErrorPresentErrorException() throws Exception {
        BitcoindRpcResponse testResultResponse = BitcoindRpcResponse.of(resultResponse.getResult().get(),
                errorResponse.getError().get(), resultResponse.getId().get());
        testResultResponse.validateResult(BlockchainInfo.class);
    }

    @Test
    public final void testGetResult() throws Exception {
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.result.chain"),
                ((BlockchainInfo) resultResponse.getResult().get()).getChain());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.result.blocks"))
                        .longValue(),
                ((BlockchainInfo) resultResponse.getResult().get()).getNumBlocks());
        assertEquals(
                ((Integer) JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.result.headers"))
                        .longValue(),
                ((BlockchainInfo) resultResponse.getResult().get()).getNumHeaders());
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.result.bestblockhash"),
                ((BlockchainInfo) resultResponse.getResult().get()).getBestBlockHash().toString());
        assertEquals(
                (double) JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.result.difficulty"),
                ((BlockchainInfo) resultResponse.getResult().get()).getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.result.verificationprogress"),
                ((BlockchainInfo) resultResponse.getResult().get()).getVerificationProgress(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                StringUtils.stripStart(JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT,
                        "$.result.chainwork").toString(), "0"),
                ((BlockchainInfo) resultResponse.getResult().get()).getChainwork().toString(16));
    }

    @Test
    public final void testValidError() throws Exception {
        assertTrue(errorResponse.validateError());
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateErrorAbsentErrorException() throws Exception {
        BitcoindRpcResponse testErrorResponse = BitcoindRpcResponse.of(null, null, resultResponse.getId().get());
        testErrorResponse.validateError();
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateErrorPresentResultException() throws Exception {
        BitcoindRpcResponse testErrorResponse = BitcoindRpcResponse.of(resultResponse.getResult().get(),
                errorResponse.getError().get(), resultResponse.getId().get());
        testErrorResponse.validateError();
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
        assertTrue(resultResponse.getId().isPresent());
        assertEquals(
                JsonPath.read(BitcoindRpcJsonResponses.BLOCKCHAIN_INFO_RESULT, "$.id"),
                resultResponse.getId().get());
    }

    @Test
    public final void testOfWithOptionalArguments() throws Exception {
        BitcoindRpcResponse testResultResponse = BitcoindRpcResponse.of(resultResponse.getResult(),
                resultResponse.getError(), resultResponse.getId());
        assertThat(testResultResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testResultResponse.getResult().get(), CoreMatchers.instanceOf(ImmutableBlockchainInfo.class));

        BitcoindRpcResponse testErrorResponse = BitcoindRpcResponse.of(errorResponse.getResult(),
                errorResponse.getError(), errorResponse.getId());
        assertThat(testErrorResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testErrorResponse.getError().get(),
                CoreMatchers.instanceOf(ImmutableBitcoindRpcResponseError.class));
    }

    @Test
    public final void testOfWithBaseArguments() throws Exception {
        BitcoindRpcResponse testResultResponse = BitcoindRpcResponse.of(resultResponse.getResult().get(),
                null, resultResponse.getId().get());
        assertThat(testResultResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testResultResponse.getResult().get(), CoreMatchers.instanceOf(ImmutableBlockchainInfo.class));

        BitcoindRpcResponse testErrorResponse = BitcoindRpcResponse.of(null, errorResponse.getError().get(),
                errorResponse.getId().get());
        assertThat(testErrorResponse, CoreMatchers.instanceOf(ImmutableBitcoindRpcResponse.class));
        assertThat(testErrorResponse.getError().get(),
                CoreMatchers.instanceOf(ImmutableBitcoindRpcResponseError.class));
    }
}
