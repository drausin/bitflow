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

package org.drausin.bitflow.blockchain.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.drausin.bitflow.blockchain.BlockchainServer;
import org.drausin.bitflow.blockchain.client.config.BitcoindRpcServiceConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings("unchecked")
public class ServerConfigTest {

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BlockchainServer.class,
            "src/test/resources/bitflow-bitcoin-test.yml");

    private ServerConfig serverConfig;
    Map<String, Object> configMap;

    @Before
    public final void setUp() throws IOException {

        serverConfig = APP.getConfiguration();

        InputStream configInputStream = getClass().getResourceAsStream("/bitflow-bitcoin-test.yml");
        Yaml yaml = new Yaml();
        configMap = (HashMap<String, Object>) yaml.load(configInputStream);
        configInputStream.close();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertThat(serverConfig.getInstance(), is(configMap.get("instance")));
    }

    @Test
    public void testGetBitcoindRpc() throws Exception {
        Map<String, Object> bitcoinRpcConfigMap = (HashMap<String, Object>) configMap.get("bitcoindRpc");
        BitcoindRpcServiceConfig expected = new BitcoindRpcServiceConfig(
                bitcoinRpcConfigMap.get("uri").toString(),
                bitcoinRpcConfigMap.get("rpcUser").toString(),
                bitcoinRpcConfigMap.get("rpcPassword").toString());
        assertThat(serverConfig.getBitcoindRpc(), is(expected));
    }

    @Test
    public void testGetBitcoindRpcNotEquals() throws Exception {
        Map<String, Object> bitcoinRpcConfigMap = (HashMap<String, Object>) configMap.get("bitcoindRpc");
        BitcoindRpcServiceConfig notExpected1 = new BitcoindRpcServiceConfig(
                "dummy",
                bitcoinRpcConfigMap.get("rpcUser").toString(),
                bitcoinRpcConfigMap.get("rpcPassword").toString());
        assertThat(serverConfig.getBitcoindRpc(), not(notExpected1));

        BitcoindRpcServiceConfig notExpected2 = new BitcoindRpcServiceConfig(
                bitcoinRpcConfigMap.get("uri").toString(),
                "dummy",
                bitcoinRpcConfigMap.get("rpcPassword").toString());
        assertThat(serverConfig.getBitcoindRpc(), not(notExpected2));

        BitcoindRpcServiceConfig notExpected3 = new BitcoindRpcServiceConfig(
                bitcoinRpcConfigMap.get("uri").toString(),
                bitcoinRpcConfigMap.get("rpcUser").toString(),
                "dummy");
        assertThat(serverConfig.getBitcoindRpc(), not(notExpected3));

        assertThat(serverConfig.getBitcoindRpc(), not("dummy"));
        assertFalse(serverConfig.getBitcoindRpc().equals(null));
    }

    @Test
    public void testGetIncludeStackTraceInErrors() throws Exception {
        assertThat(serverConfig.getIncludeStackTraceInErrors().get(), is(configMap.get("includeStackTraceInErrors")));
    }

}