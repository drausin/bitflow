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

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequestFactory;
import org.drausin.bitflow.bitcoin.api.responses.utils.BitcoinNodeClientFactory;
import org.drausin.bitflow.blockchain.config.ServerConfig;


/**
 * Server for blockchain data, relying under the hood on on bitcoin node RPCs.
 *
 * @author dwulsin
 */
public final class BlockchainServer extends Application<ServerConfig> {

    @Override
    public void run(ServerConfig config, Environment env) throws Exception {

        BitcoinNodeService bitcoinNode = (new BitcoinNodeClientFactory()).createClient(config.getBitcoinNode());

        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNode);
        env.jersey().register(blockchainResource);

        env.healthChecks().register("bitcoinNode", createBitcoinNodeHealthCheck(bitcoinNode));

        //boolean includeStackTrace = config.getIncludeStackTraceInErrors().or(true);
        // TODO(dwulsin): need to figure out how to handle Exceptions (via ExceptionMappers?)
    }

    public static void main(String[] args) throws Exception {
        new BlockchainServer().run(args);
    }

    protected static HealthCheck createBitcoinNodeHealthCheck(BitcoinNodeService bitcoinNode) {
        return new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                try {
                    bitcoinNode.getBlockchainInfo(
                            BitcoinNodeRequestFactory.createBlockchainInfoRequest()).validateResult();
                    return Result.healthy();
                } catch (RuntimeException e) {
                    return Result.unhealthy(e.getMessage());
                }
            }
        };
    }
}
