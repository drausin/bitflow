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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.jayway.jsonpath.JsonPath;
import org.bitcoinj.core.Sha256Hash;
import org.junit.Before;
import org.junit.Test;

public final class BlockHeaderHashResponseTest {

    private String resultJsonResponse;
    private String errorJsonResponse;
    private BlockHeaderHashResponse resultResponse;
    private BlockHeaderHashResponse errorResponse;

    @Before
    public void setUp() throws Exception {
        resultJsonResponse = BitcoinNodeExampleResponses.getBlockHeaderHashJsonResponse();
        errorJsonResponse = BitcoinNodeExampleResponses.getErrorJsonResponse();
        resultResponse = BitcoinNodeExampleResponses.getBlockHeaderHashResponse();
        errorResponse = BitcoinNodeExampleResponses.getBlockHeaderHashErrorResponse();
    }

    @Test
    public void testValidResult() throws Exception {
        assertTrue(resultResponse.validateResult());
    }

    @Test
    public void testGetResult() throws Exception {
        assertEquals(
                JsonPath.read(resultJsonResponse, "$.result"),
                resultResponse.getResult().get().toString());
    }

    @Test
    public void testValidError() throws Exception {
        assertTrue(errorResponse.validateError());
    }

    @Test
    public void testGetError() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(errorJsonResponse, "$.error.code")).longValue(),
                errorResponse.getError().get().getCode());
        assertEquals(
                JsonPath.read(errorJsonResponse, "$.error.message"),
                errorResponse.getError().get().getMessage());
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(
                JsonPath.read(resultJsonResponse, "$.id"),
                resultResponse.getId().get());
    }

    @Test
    public void testOf() throws Exception {
        BitcoinNodeResponse testResultResponse = BlockHeaderHashResponse.of(resultResponse.getResult(),
                resultResponse.getError(), resultResponse.getId());
        assertThat(testResultResponse, instanceOf(ImmutableBlockHeaderHashResponse.class));
        assertThat(testResultResponse.getResult().get(), instanceOf(Sha256Hash.class));

        BitcoinNodeResponse testErrorResponse = BlockHeaderHashResponse.of(errorResponse.getResult(),
                errorResponse.getError(), errorResponse.getId());
        assertThat(testErrorResponse, instanceOf(ImmutableBlockHeaderHashResponse.class));
        assertThat(testErrorResponse.getError().get(),
                instanceOf(ImmutableBitcoinNodeResponseError.class));
    }
}
