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

package org.drausin.bitflow.client;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.palantir.remoting.http.FeignClientFactory;
import com.palantir.remoting.http.errors.SerializableErrorErrorDecoder;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.OptionalAwareDecoder;
import feign.Request;
import feign.RequestInterceptor;
import feign.TextDelegateDecoder;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import org.drausin.bitflow.serde.BitflowMapperFactory;
import org.slf4j.LoggerFactory;

public abstract class AbstractBitflowClientFactory<C> {

    public AbstractBitflowClientFactory() {}

    public final <T> T createClient(Class<T> type, C config) {
        Feign.Builder builder = Feign.builder()
                .logger(getLogger())
                .contract(getContract())
                .encoder(getEncoder())
                .decoder(getDecoder())
                .errorDecoder(getErrorDecoder())
                .client(getClient())
                .options(getOptions())
                .requestInterceptors(getRequestInterceptors(config));

        if (LoggerFactory.getLogger(Logger.class).isDebugEnabled()) {
            builder = builder.logLevel(Logger.Level.FULL);
        }

        return builder.target(type, getUri(config));
    }

    protected abstract String getUri(C config);

    protected static Logger getLogger() {
        return new Slf4jLogger();
    }

    protected static Contract getContract() {
        return new JAXRSContract();
    }

    protected static Encoder getEncoder() {
        return new JacksonEncoder(BitflowMapperFactory.createMapper());
    }

    protected static Decoder getDecoder() {
        return new OptionalAwareDecoder(
                new TextDelegateDecoder(new JacksonDecoder(BitflowMapperFactory.createMapper())));
    }

    protected static ErrorDecoder getErrorDecoder() {
        return SerializableErrorErrorDecoder.INSTANCE;
    }

    protected static Client getClient() {
        return FeignClientFactory.okHttpClient().apply(Optional.absent());
    }

    protected static Request.Options getOptions() {
        return new Request.Options();
    }

    @SuppressWarnings("checkstyle:designforextension")
    protected Iterable<RequestInterceptor> getRequestInterceptors(C config) {
        return ImmutableList.of();
    }
}
