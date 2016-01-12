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

package org.drausin.bitflow.bitcoin.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.IOException;
import org.drausin.bitflow.bitcoin.BitcoinNodeServer;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class BitcoinNodeServerCommandTest {

    private BitcoinNodeServerCommand bitcoinNodeServerCommand;
    private BitcoinNodeServerCommand bitcoinNodeServerCommandSpy;

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BitcoinNodeServer.class,
            "src/test/resources/bitflow-bitcoin-node-test.yml");

    @Before
    public final void setUp() throws IOException, InterruptedException {
        bitcoinNodeServerCommand = new BitcoinNodeServerCommand(APP.getApplication());
        bitcoinNodeServerCommandSpy = spy(bitcoinNodeServerCommand);

        BitcoindExecutable bitcoindExecutable = mock(BitcoindExecutable.class);
        when(bitcoindExecutable.run()).thenReturn(0);
        doReturn(bitcoindExecutable).when(bitcoinNodeServerCommandSpy).getBitcoindExecutable(any(ServerConfig.class));
    }

    @Test
    public final void testGetConfigurationClass() throws Exception {
        assertEquals(ServerConfig.class, bitcoinNodeServerCommand.getConfigurationClass());
    }

    @Test
    public final void testGetBitcoindExecutable() {
        assertFalse(bitcoinNodeServerCommand.getBitcoindExecutable(
                APP.getConfiguration()).getBitcoindConfigurationFile().getAbsolutePath().isEmpty());
    }

    @Test
    public final void testRun() throws Exception {
        bitcoinNodeServerCommandSpy.run(APP.getEnvironment(), null, APP.getConfiguration());
    }
}
