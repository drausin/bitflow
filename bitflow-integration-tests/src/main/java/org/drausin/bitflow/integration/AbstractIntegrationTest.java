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

package org.drausin.bitflow.integration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.palantir.remoting.http.FeignClientFactory;
import com.palantir.remoting.http.errors.SerializableErrorErrorDecoder;
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
import org.drausin.bitflow.bitcoin.api.responses.utils.BitcoinNodeErrorDecoder;
import org.junit.Before;

/**
 * Abstract class handling setup and configuration for all integration tests.
 *
 * This integration test framework assumes that docker-compose environment is up and running via something like
 *
 * <pre>
 * docker-compose -f docker-compose.yml --x-networking -p bitflow up -d
 * </pre>
 *
 * @author dwulsin
 */
public abstract class AbstractIntegrationTest {

    // can get IP via `docker-machine ip boot2docker-vm`
    private static final String BITCOIN_NODE_URI = "http://192.168.99.100:8332";
    private static final String BITCOIN_NODE_USER = "someuser";
    private static final String BITCOIN_NODE_PASSWORD = "somepasswordtochange";

    //private static final FeignClientFactory CLIENT_FACTORY = FeignClients.withMapper(
    //        BitcoinNodeMapperProvider.getMapper());

    private final BitcoinNodeService bitcoinNode;

    public AbstractIntegrationTest() {
        //this(CLIENT_FACTORY.createProxy(Optional.absent(), BITCOIN_NODE_URI, BitcoinNodeService.class));

        // TODO(dwulsin): abstract this away
        Function<Optional<SSLSocketFactory>, Client> clientSupplier = FeignClientFactory.okHttpClient();
        this.bitcoinNode = Feign.builder()
                .contract(new JAXRSContract())
                .encoder(new JacksonEncoder(BitcoinNodeMapperProvider.getMapper()))
                .decoder(new OptionalAwareDecoder(new TextDelegateDecoder(
                        new JacksonDecoder(BitcoinNodeMapperProvider.getMapper()))))
                .errorDecoder(BitcoinNodeErrorDecoder.INSTANCE)
                .client(clientSupplier.apply(Optional.absent()))
                .options(new Request.Options())
                .requestInterceptor(new BasicAuthRequestInterceptor(BITCOIN_NODE_USER, BITCOIN_NODE_PASSWORD))
                .target(BitcoinNodeService.class, BITCOIN_NODE_URI);
    }

    public AbstractIntegrationTest(BitcoinNodeService bitcoinNode) {
        this.bitcoinNode = bitcoinNode;
    }

    public final BitcoinNodeService getBitcoinNode() {
        return bitcoinNode;
    }

    @Before
    public final void setUp() {

        //TODO(dwulsin): confirm that docker-compose bitflow env is up and running
    }
}
