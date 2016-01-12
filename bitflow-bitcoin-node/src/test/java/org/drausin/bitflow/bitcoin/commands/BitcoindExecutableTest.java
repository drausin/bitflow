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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.io.IOException;
import org.drausin.bitflow.bitcoin.BitcoinNodeServer;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class BitcoindExecutableTest {

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BitcoinNodeServer.class,
            "src/test/resources/bitflow-bitcoin-node-test.yml");

    private BitcoindExecutable bitcoindExecutable;
    private BitcoindExecutable bitcoindExecutableSpy;

    @Before
    public final void setUp() throws Exception {
        bitcoindExecutable = new BitcoindExecutable(APP.getConfiguration().getBitcoinNode().getConfigFile());
        bitcoindExecutableSpy = spy(bitcoindExecutable);

        Process processMock = mock(Process.class);
        when(processMock.waitFor()).thenReturn(0);
        doReturn(processMock).when(bitcoindExecutableSpy).getProcess(anyListOf(String.class));

        doReturn(true).when(bitcoindExecutableSpy).pathExists(any(String.class));
    }

    @Test
    public final void testPathExists() throws IOException {
        File temp = File.createTempFile("/tmp/", null);
        assertTrue(bitcoindExecutable.pathExists(temp.getPath()));
        temp.deleteOnExit();
    }

    @Test
    public final void testGetBitcoinPath() {
        assertTrue(bitcoindExecutableSpy.getBitcoindPath().contains("bitcoind"));
    }

    @Test
    public final void testRun() throws InterruptedException, IOException {
        assertEquals(0, bitcoindExecutableSpy.run());
    }
}
