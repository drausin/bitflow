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
public class BitcoinNodeServerConfigTest {

    @ClassRule
    public static final DropwizardAppRule<BitcoinNodeServerConfig> APP = new DropwizardAppRule<>(
            BitcoinNodeServer.class, "src/test/resources/bitflow-bitcoin-node-test.yml");

    private BitcoinNodeServerConfig bitcoinNodeServerConfig;

    @Before
    public final void setUp() throws IOException {
        bitcoinNodeServerConfig = APP.getConfiguration();
    }

    @Test
    public final void testToString() {
        assertTrue(bitcoinNodeServerConfig.toString().contains(bitcoinNodeServerConfig.getInstance()));
        assertTrue(bitcoinNodeServerConfig.toString().contains(bitcoinNodeServerConfig.getRpcUri()));
        assertTrue(bitcoinNodeServerConfig.toString().contains(bitcoinNodeServerConfig.getMode().toString()));
        assertTrue(bitcoinNodeServerConfig.toString().contains(bitcoinNodeServerConfig.getBitcoinNode().toString()));
        assertTrue(bitcoinNodeServerConfig.toString().contains(bitcoinNodeServerConfig.getLoggingFactory().toString()));
        assertTrue(bitcoinNodeServerConfig.toString().contains(bitcoinNodeServerConfig.getServerFactory().toString()));

    }
}
