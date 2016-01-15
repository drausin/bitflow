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

import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.config.BitcoinNodeClientConfig;
import org.drausin.bitflow.bitcoin.api.responses.utils.BitcoinNodeFeignClientFactory;
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
    private static final BitcoinNodeClientConfig bitcoinNodeConfig =
            BitcoinNodeClientConfig.of("http://192.168.99.100:8332", "someuser", "somepasswordtochange");

    private final BitcoinNodeService bitcoinNode;

    public AbstractIntegrationTest() {
        this(BitcoinNodeFeignClientFactory.createClient(bitcoinNodeConfig));
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
