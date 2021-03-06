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
import org.drausin.bitflow.bitcoin.api.responses.utils.BitcoinNodeClientFactory;
import org.drausin.bitflow.blockchain.api.BlockchainService;
import org.drausin.bitflow.client.BitflowClientFactory;
import org.junit.Before;

/**
 * Abstract class handling setup and configuration for all integration tests.
 * <p>
 * This integration test framework assumes that docker-compose environment is up and running via something like
 * <p>
 * <pre>
 * docker-compose -f docker-compose.yml --x-networking -p bitflow up -d
 * </pre>
 *
 * @author dwulsin
 */
public abstract class AbstractIntegrationTest {

    // can get IP via `docker-machine ip boot2docker-vm`
    private static final String BITCOIN_NODE_URI = "http://bitcoin-node.bitflow.bfl:8332";
    private static final String BLOCKCHAIN_URI = "http://blockchain.bitflow.bfl:8100/api/";

    private static final BitcoinNodeClientConfig BITCOIN_NODE_CONFIG = BitcoinNodeClientConfig.of(
            BITCOIN_NODE_URI, "someuser", "somepasswordtochange");
    private static final BitcoinNodeClientFactory BITCOIN_NODE_CLIENT_FACTORY = new BitcoinNodeClientFactory();
    private static final BitflowClientFactory BITFLOW_CLIENT_FACTORY = new BitflowClientFactory();

    private final BitcoinNodeService bitcoinNode;
    private final BlockchainService blockchain;

    public AbstractIntegrationTest() {
        this(
                BITCOIN_NODE_CLIENT_FACTORY.createClient(BITCOIN_NODE_CONFIG),
                BITFLOW_CLIENT_FACTORY.createClient(BlockchainService.class, BLOCKCHAIN_URI));
    }

    public AbstractIntegrationTest(BitcoinNodeService bitcoinNode, BlockchainService blockchain) {
        this.bitcoinNode = bitcoinNode;
        this.blockchain = blockchain;
    }

    public final BitcoinNodeService getBitcoinNode() {
        return bitcoinNode;
    }

    public final BlockchainService getBlockchain() {
        return blockchain;
    }

    @Before
    public final void setUp() {

        //TODO(dwulsin): confirm that docker-compose bitflow env is up and running
    }
}
