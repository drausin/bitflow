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

package org.drausin.bitflow.bitcoin.api.responses.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.providers.BitflowMapperFactory;
import org.drausin.bitflow.bitcoin.api.responses.BitcoinNodeResponse;
import org.drausin.bitflow.bitcoin.api.responses.BitcoinNodeResponseError;

/**
 * A feign {@link ErrorDecoder} that handles an error returned from a bitcoin RPC.
 */
public enum BitcoinNodeErrorDecoder implements ErrorDecoder {
    INSTANCE;

    @Override
    public Exception decode(String methodKey, Response response) {

        Collection<String> contentType = response.headers().get(HttpHeaders.CONTENT_TYPE);
        if (!contentType.contains(MediaType.APPLICATION_JSON)) {
            throw new RuntimeException("Response content type is not JSON, but that is expected");
        }

        BitcoinNodeResponseError error = getErrorFromBody(response.body(), methodKey);
        String message = String.format("HTTP error: %s, RPC error: %s, message:  %s", response.status(),
                error.getCode(), error.getMessage());
        return new RuntimeException(message);
    }

    private static BitcoinNodeResponseError getErrorFromBody(Response.Body body, String methodKey) {
        Class methodReturnType = getMethodReturnTypes(BitcoinNodeService.class).get(methodKey);
        BitcoinNodeResponse rpcResponse = getResponseFromBody(body, methodReturnType);
        rpcResponse.validateError();
        return (BitcoinNodeResponseError) rpcResponse.getError().get();
    }

    private static BitcoinNodeResponse getResponseFromBody(Response.Body body, Class methodReturnType) {
        ObjectMapper mapper = BitflowMapperFactory.createMapper();
        try {
            BitcoinNodeResponse rpcResponse = (BitcoinNodeResponse) mapper.readValue(body.asInputStream(),
                    methodReturnType);
            return rpcResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Class> getMethodReturnTypes(Class clazz) {
        Map<String, Class> returnTypes = Maps.newHashMap();
        for (Method method : clazz.getMethods()) {
            String configKey = Feign.configKey(clazz, method);
            returnTypes.put(configKey, method.getReturnType());
        }
        return ImmutableMap.copyOf(returnTypes);
    }
}
