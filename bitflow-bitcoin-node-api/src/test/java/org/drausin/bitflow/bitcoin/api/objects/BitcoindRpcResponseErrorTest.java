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

package org.drausin.bitflow.bitcoin.api.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class BitcoindRpcResponseErrorTest {

    private ObjectMapper rpcMapper;
    private String errorJson;
    private BitcoindRpcResponseError responseError1;
    private String responseError1Json;

    @Before
    public final void setUp() throws Exception {

        rpcMapper = BitcoinNodeMapperProvider.getMapper();

        errorJson = BitcoindRpcExampleResponses.getErrorJson();
        responseError1 = BitcoindRpcExampleResponses.getError();
        responseError1Json = rpcMapper.writeValueAsString(responseError1);
    }

    @Test
    public final void testGetCode() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(errorJson, "$.code")).longValue(),
                responseError1.getCode());
        assertEquals(responseError1.getCode(), ((Integer) JsonPath.read(responseError1Json, "$.code")).longValue());
    }

    @Test
    public final void testGetMessage() throws Exception {
        assertEquals(JsonPath.read(errorJson, "$.message"), responseError1.getMessage());
        assertEquals(responseError1.getMessage(), JsonPath.read(responseError1Json, "$.message"));
    }

    @Test
    public final void testOf() throws Exception {
        assertThat(BitcoindRpcResponseError.of(-8, "Block height out of range"),
                CoreMatchers.instanceOf(ImmutableBitcoindRpcResponseError.class));
    }
}
