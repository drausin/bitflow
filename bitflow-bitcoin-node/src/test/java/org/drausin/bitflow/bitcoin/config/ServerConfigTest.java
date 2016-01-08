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

package org.drausin.bitflow.bitcoin.config;

import static org.junit.Assert.assertTrue;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.IOException;
import org.drausin.bitflow.bitcoin.BitcoinNodeServer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

// TODO(dwulsin): remove this once we decide what to do with this test
@SuppressFBWarnings(value = "URF_UNREAD_FIELD")
public class ServerConfigTest {

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BitcoinNodeServer.class,
            "src/test/resources/bitflow-bitcoin-test.yml");

    private ServerConfig serverConfig;

    @Before
    public final void setUp() throws IOException {
        serverConfig = APP.getConfiguration();
    }

    @Test
    public final void testToString() {
        assertTrue(serverConfig.toString().contains(serverConfig.getInstance()));
        assertTrue(serverConfig.toString().contains(serverConfig.getRpcUri()));
        assertTrue(serverConfig.toString().contains(serverConfig.getMode().toString()));
        assertTrue(serverConfig.toString().contains(serverConfig.getBitcoinNode().toString()));
        assertTrue(serverConfig.toString().contains(serverConfig.getLoggingFactory().toString()));
        assertTrue(serverConfig.toString().contains(serverConfig.getServerFactory().toString()));

    }
}
