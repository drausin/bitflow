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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.palantir.remoting.http.FeignClientFactory;
import feign.Client;
import feign.Feign;
import feign.OptionalAwareDecoder;
import feign.Request;
import feign.TextDelegateDecoder;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import javax.net.ssl.SSLSocketFactory;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;

/**
 * Factory for creating {@link feign.Feign} {@link BitcoinNodeService} clients.
 */
public final class BitcoinNodeFeignClientFactory {

    private BitcoinNodeFeignClientFactory() {}

    public static BitcoinNodeService createClient(String uri, String user, String password) {
        Function<Optional<SSLSocketFactory>, Client> clientSupplier = FeignClientFactory.okHttpClient();
        return Feign.builder()
                .contract(new JAXRSContract())
                .encoder(new JacksonEncoder(BitcoinNodeMapperProvider.getMapper()))
                .decoder(new OptionalAwareDecoder(new TextDelegateDecoder(
                        new JacksonDecoder(BitcoinNodeMapperProvider.getMapper()))))
                .errorDecoder(BitcoinNodeErrorDecoder.INSTANCE)
                .client(clientSupplier.apply(Optional.absent()))
                .options(new Request.Options())
                .requestInterceptor(new BasicAuthRequestInterceptor(user, password))
                .target(BitcoinNodeService.class, uri);
    }
}
