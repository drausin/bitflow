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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.IOException;
import java.math.BigInteger;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.responses.BlockchainInfoResponse;
import org.drausin.bitflow.blockchain.config.ServerConfig;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public final class BlockchainServerTest {

    private ObjectMapper mapper;

    @ClassRule
    public static final DropwizardAppRule<ServerConfig> APP = new DropwizardAppRule<>(BlockchainServer.class,
            "src/test/resources/bitflow-blockchain-test.yml");

    @Before
    public void setUp() {
        mapper = APP.getEnvironment().getObjectMapper();
    }

    @Test
    public void testServerStart() {
        assertTrue(APP.getApplication() instanceof BlockchainServer);
    }

    @Test
    public void testMainEntryPoint() throws Exception {
        BlockchainServer.main(new String[] {});
    }

    @Test
    public void testCreateBitcoinNodeHealthCheckHealthy() {

        BlockchainInfoResponse response = mock(BlockchainInfoResponse.class);
        when(response.validateResult()).thenReturn(true);
        BitcoinNodeService bitcoinNode = mock(BitcoinNodeService.class);
        when(bitcoinNode.getBlockchainInfo(any(BitcoinNodeRequest.class))).thenReturn(response);

        HealthCheck healthCheck = BlockchainServer.createBitcoinNodeHealthCheck(bitcoinNode);
        assertTrue(healthCheck.execute().isHealthy());

    }

    @Test
    public void testCreateBitcoinNodeHealthCheckUnhealthy() {

        BlockchainInfoResponse response = mock(BlockchainInfoResponse.class);
        when(response.validateResult()).thenThrow(new IllegalStateException("result value is absent"));
        BitcoinNodeService bitcoinNode = mock(BitcoinNodeService.class);
        when(bitcoinNode.getBlockchainInfo(any(BitcoinNodeRequest.class))).thenReturn(response);

        HealthCheck healthCheck = BlockchainServer.createBitcoinNodeHealthCheck(bitcoinNode);
        assertFalse(healthCheck.execute().isHealthy());

    }

    @Test
    public void testObjectMapperSha256Hash() throws IOException {

        String hexHash = "000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f";
        String hexHashSerialized = String.format("\"%s\"", hexHash);
        Sha256Hash hash = Sha256Hash.wrap(hexHash);

        assertEquals(hexHashSerialized, mapper.writeValueAsString(hash));
        assertEquals(hash, mapper.readValue(hexHashSerialized, Sha256Hash.class));

    }

    @Test
    public void testObjectMapperBigInteger() throws IOException {

        BigInteger value = new BigInteger("256", 10);
        String valueSerialized = "\"100\""; // in base 16

        assertEquals(valueSerialized, mapper.writeValueAsString(value));
        assertEquals(value, mapper.readValue(valueSerialized, BigInteger.class));
    }
}
