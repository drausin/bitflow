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

package org.drausin.bitflow.bitcoin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import io.dropwizard.cli.Cli;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.io.IOException;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

public class BitcoinNodeServerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BitcoinNodeServer.class,
            "src/test/resources/bitflow-bitcoin-node-test.yml");

    private BitcoinNodeServer bitcoinNodeServer;
    private BitcoinNodeServer bitcoinNodeServerSpy;

    @Before
    public final void setUp() {
        bitcoinNodeServer = new BitcoinNodeServer();
    }

    @Test
    public final void testServerStart() {
        assertTrue(APP.getApplication() instanceof BitcoinNodeServer);
    }

    @Test
    public final void testMainEntryPoint() throws Exception {
        BitcoinNodeServer.main(new String[] {});
    }

    @Test
    public final void testCreateDataDirectorySuccess() throws IOException {
        assertTrue(bitcoinNodeServer.createDataDirectory(APP.getConfiguration()));
    }

    @Test
    public final void testCreateDataDirectoryFailure() throws IOException {

        expectedException.expect(IOException.class);

        bitcoinNodeServerSpy = spy(bitcoinNodeServer);
        File dataDirMock = mock(File.class);
        when(dataDirMock.mkdirs()).thenReturn(false);
        doReturn(dataDirMock).when(bitcoinNodeServerSpy).getDataDirectory(any(ServerConfig.class));

        bitcoinNodeServerSpy.createDataDirectory(APP.getConfiguration());
    }

    @Test
    public final void testCliRunSuccess() throws Exception {

        bitcoinNodeServerSpy = spy(bitcoinNodeServer);
        Cli cliMock = mock(Cli.class);
        doReturn(true).when(cliMock).run(Matchers.<String>anyVararg());
        doReturn(cliMock).when(bitcoinNodeServerSpy).getCli(Matchers.<Bootstrap<ServerConfig>>any());

        assertTrue(cliMock.run());
        bitcoinNodeServerSpy.run();
    }

    @Test
    public final void testCliRunFailure() throws Exception {

        expectedException.expect(IllegalArgumentException.class);

        bitcoinNodeServerSpy = spy(bitcoinNodeServer);
        Cli cliMock = mock(Cli.class);
        doReturn(false).when(cliMock).run(Matchers.<String>anyVararg());
        doReturn(cliMock).when(bitcoinNodeServerSpy).getCli(Matchers.<Bootstrap<ServerConfig>>any());

        assertFalse(cliMock.run());
        bitcoinNodeServerSpy.run();
    }
}
