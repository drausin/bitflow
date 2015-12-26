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

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.drausin.bitflow.blockchain.client.BitcoindRpcResource;
import org.drausin.bitflow.blockchain.config.ServerConfig;
import org.drausin.bitflow.blockchain.health.BitcoindRpcHealthCheck;

public class BlockchainServer extends Application<ServerConfig> {

    @Override
    public final void run(ServerConfig config, Environment env) throws Exception {

        BitcoindRpcResource bitcoindRpcResource = new BitcoindRpcResource(config.getBitcoindRpc());
        BlockchainResource blockchainResource = new BlockchainResource(bitcoindRpcResource);

        env.jersey().register(blockchainResource);

        env.healthChecks().register("bitcoindRpc", new BitcoindRpcHealthCheck(bitcoindRpcResource));

        //boolean includeStackTrace = config.getIncludeStackTraceInErrors().or(true);
        // TODO(dwulsin): need to figure out how to handle Exceptions (via ExceptionMappers?)
    }

    public static void main(String[] args) throws Exception {
        new BlockchainServer().run(args);
    }
}
