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

package org.drausin.bitflow.bitcoin.api.responses.utils;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.junit.Before;
import org.junit.Test;

public final class BitcoinNodeErrorDecoderTest {

    private final ErrorDecoder errorDecoder = BitcoinNodeErrorDecoder.INSTANCE;
    private String getBlockchainInfoconfigKey;
    private String getBlockHeaderMethodKey;

    @Before
    public void setUp() throws NoSuchMethodException {
        getBlockchainInfoconfigKey = Feign.configKey(BitcoinNodeService.class,
                BitcoinNodeService.class.getMethod("getBlockchainInfo", BitcoinNodeRequest.class));
        getBlockHeaderMethodKey = Feign.configKey(BitcoinNodeService.class,
                BitcoinNodeService.class.getMethod("getBlockHeader", BitcoinNodeRequest.class));
    }

    @Test
    public void testDecodeBlockchainInfoError() throws Exception {
        Map<String, Collection<String>> headers = ImmutableMap.of(HttpHeaders.CONTENT_TYPE,
                ImmutableList.of(MediaType.APPLICATION_JSON));
        String text = "{\"result\":null,\"error\":{\"code\":-28,\"message\":\"Activating best chain...\"},\"id\":null}";
        Response response = Response.create(500, "bad request", headers, text, Charset.forName("UTF-8"));

        assertThat(errorDecoder.decode(getBlockchainInfoconfigKey, response), instanceOf(RuntimeException.class));
    }

    @Test
    public void testDecodeBlockHeaderError() throws Exception {
        Map<String, Collection<String>> headers = ImmutableMap.of(HttpHeaders.CONTENT_TYPE,
                ImmutableList.of(MediaType.APPLICATION_JSON));
        String text = "{\"result\":null,\"error\":{\"code\":-32603,\"message\":\"Block not available (pruned data)\"},"
                + "\"id\":null}";
        Response response = Response.create(500, "bad request", headers, text, Charset.forName("UTF-8"));
        assertThat(errorDecoder.decode(getBlockHeaderMethodKey, response), instanceOf(RuntimeException.class));
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeNonJsonError() throws Exception {
        Map<String, Collection<String>> headers = ImmutableMap.of(HttpHeaders.CONTENT_TYPE,
                ImmutableList.of(MediaType.APPLICATION_XML));
        Response response = Response.create(500, "bad request", headers, "dummy text", Charset.forName("UTF-8"));
        errorDecoder.decode(getBlockchainInfoconfigKey, response);
    }

    @Test(expected = IllegalStateException.class)
    public void testDecodeBadResponseError() throws Exception {
        Map<String, Collection<String>> headers = ImmutableMap.of(HttpHeaders.CONTENT_TYPE,
                ImmutableList.of(MediaType.APPLICATION_JSON));
        String text = "{\"result\":null, \"id\":null}";
        Response response = Response.create(500, "bad request", headers, text, Charset.forName("UTF-8"));
        errorDecoder.decode(getBlockHeaderMethodKey, response);
    }
}
