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

package org.drausin.bitflow.blockchain;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;
import org.drausin.bitflow.blockchain.config.ServerConfig;
import org.drausin.bitflow.service.utils.BitflowServiceHealthCheck;


/**
 * Server for blockchain data, relying under the hood on on bitcoin node RPCs.
 *
 * @author dwulsin
 */
public class BlockchainServer extends Application<ServerConfig> {

    @Override
    public final void run(ServerConfig config, Environment env) throws Exception {

        BitcoinNodeService bitcoinNodeService = Feign.builder()
                .encoder(new JacksonEncoder(BitcoinNodeMapperProvider.getMapper()))
                .decoder(new JacksonDecoder(BitcoinNodeMapperProvider.getMapper()))
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .target(BitcoinNodeService.class, config.getBitcoinNodeUri());

        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);

        env.jersey().register(blockchainResource);

        env.healthChecks().register("bitcoinNode", new BitflowServiceHealthCheck(bitcoinNodeService));

        //boolean includeStackTrace = config.getIncludeStackTraceInErrors().or(true);
        // TODO(dwulsin): need to figure out how to handle Exceptions (via ExceptionMappers?)
    }

    public static void main(String[] args) throws Exception {
        new BlockchainServer().run(args);
    }
}
