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

package org.drausin.bitflow.bitcoin.api.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Optional;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class BlockchainInfoResponseTest {

    private String resultJsonResponse;
    private String errorJsonResponse;
    private BlockchainInfoResponse resultResponse;
    private BlockchainInfoResponse errorResponse;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {
        resultJsonResponse = BitcoinNodeExampleResponses.getBlockchainInfoJsonResponse();
        errorJsonResponse = BitcoinNodeExampleResponses.getErrorJsonResponse();
        resultResponse = BitcoinNodeExampleResponses.getBlockchainInfoResponse();
        errorResponse = BitcoinNodeExampleResponses.getBlockchainInfoErrorResponse();
    }

    @Test
    public final void testValidateResult() throws Exception {
        assertTrue(resultResponse.validateResult());
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateResultAbsentResultException() throws Exception {
        BitcoinNodeResponse testResultResponse = BlockchainInfoResponse.of(Optional.absent(), Optional.absent(),
                resultResponse.getId());
        testResultResponse.validateResult();
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateErrorPresentErrorException() throws Exception {
        BitcoinNodeResponse testResultResponse = BlockchainInfoResponse.of(resultResponse.getResult(),
                errorResponse.getError(), resultResponse.getId());
        testResultResponse.validateResult();
    }

    @Test
    public final void testGetResult() throws Exception {
        assertEquals(
                JsonPath.read(resultJsonResponse, "$.result.chain"),
                resultResponse.getResult().get().getChain());
        assertEquals(
                ((Integer) JsonPath.read(resultJsonResponse, "$.result.blocks"))
                        .longValue(),
                resultResponse.getResult().get().getNumBlocks());
        assertEquals(
                ((Integer) JsonPath.read(resultJsonResponse, "$.result.headers"))
                        .longValue(),
                resultResponse.getResult().get().getNumHeaders());
        assertEquals(
                JsonPath.read(resultJsonResponse, "$.result.bestblockhash"),
                resultResponse.getResult().get().getBestBlockHash().toString());
        assertEquals(
                (double) JsonPath.read(resultJsonResponse, "$.result.difficulty"),
                resultResponse.getResult().get().getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(resultJsonResponse,
                        "$.result.verificationprogress"),
                resultResponse.getResult().get().getVerificationProgress(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                StringUtils.stripStart(JsonPath.read(resultJsonResponse,
                        "$.result.chainwork").toString(), "0"),
                resultResponse.getResult().get().getChainwork().toString(16));
    }

    @Test
    public final void testValidError() throws Exception {
        assertTrue(errorResponse.validateError());
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateErrorAbsentErrorException() throws Exception {
        BitcoinNodeResponse testErrorResponse = BlockchainInfoResponse.of(Optional.absent(), Optional.absent(),
                resultResponse.getId());
        testErrorResponse.validateError();
    }

    @Test(expected = IllegalStateException.class)
    public final void testValidateErrorPresentResultException() throws Exception {
        BitcoinNodeResponse testErrorResponse = BlockchainInfoResponse.of(resultResponse.getResult(),
                errorResponse.getError(), resultResponse.getId());
        testErrorResponse.validateError();
    }

    @Test
    public final void testGetError() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(errorJsonResponse, "$.error.code")).longValue(),
                errorResponse.getError().get().getCode());
        assertEquals(
                JsonPath.read(errorJsonResponse, "$.error.message"),
                errorResponse.getError().get().getMessage());
    }

    @Test
    public final void testGetId() throws Exception {
        assertTrue(resultResponse.getId().isPresent());
        assertEquals(
                JsonPath.read(resultJsonResponse, "$.id"),
                resultResponse.getId().get());
    }

    @Test
    public final void testOf() throws Exception {
        BlockchainInfoResponse testResultResponse = BlockchainInfoResponse.of(resultResponse.getResult(),
                resultResponse.getError(), resultResponse.getId());
        assertEquals(resultResponse, testResultResponse);

        BitcoinNodeResponse testErrorResponse = BlockchainInfoResponse.of(errorResponse.getResult(),
                errorResponse.getError(), errorResponse.getId());
        assertEquals(errorResponse, testErrorResponse);
    }
}
