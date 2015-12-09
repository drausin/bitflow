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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;

public class BitcoindRpcRequestTest {

    private ObjectMapper rpcMapper;
    private BitcoindRpcRequest request1;
    private String rpcRequestJson;
    private String request1Json;

    @Before
    public void setUp() throws Exception {

        rpcMapper = new ObjectMapper();
        rpcMapper.registerModule(new GuavaModule()); // to handle (immutable) collections out of the box

        // from https://bitcoin.org/en/developer-reference#remote-procedure-calls-rpcs
        rpcRequestJson =
                "{\n"
                + "    \"method\": \"getblockhash\",\n"
                + "    \"params\": [0],\n"
                + "    \"id\": \"foo\"\n"
                + "}";

        request1 = rpcMapper.readValue(rpcRequestJson, ImmutableBitcoindRpcRequest.class);
        request1Json = rpcMapper.writeValueAsString(request1);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(JsonPath.read(rpcRequestJson, "$.method"), request1.getMethod());
        assertEquals(request1.getMethod(), JsonPath.read(request1Json, "$.method"));
    }

    @Test
    public void testGetMethod() throws Exception {
        assertEquals(JsonPath.read(rpcRequestJson, "$.params"), request1.getParams());
        assertEquals(request1.getParams(), JsonPath.read(request1Json, "$.params"));
    }

    @Test
    public void testGetParams() throws Exception {
        assertEquals(JsonPath.read(rpcRequestJson, "$.id"), request1.getId());
        assertEquals(request1.getId(), JsonPath.read(request1Json, "$.id"));
    }

    @Test
    public void testOf() throws Exception {
        assertThat(BitcoindRpcRequest.of("foo", "getblockhash", ImmutableList.of(0)),
                instanceOf(ImmutableBitcoindRpcRequest.class));
    }
}