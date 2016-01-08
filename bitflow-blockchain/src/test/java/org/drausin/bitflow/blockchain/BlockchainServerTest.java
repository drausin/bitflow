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

import static org.junit.Assert.assertTrue;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.drausin.bitflow.blockchain.config.ServerConfig;
import org.junit.ClassRule;
import org.junit.Test;

public class BlockchainServerTest {

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BlockchainServer.class,
            "src/test/resources/bitflow-blockchain-test.yml");

    @Test
    public final void testServerStart() {
        assertTrue(APP.getApplication() instanceof BlockchainServer);
    }

    @Test
    public final void testMainEntryPoint() throws Exception {
        BlockchainServer.main(new String[] {});
    }
}
