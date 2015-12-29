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

package org.drausin.bitflow.blockchain.health;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.client.BitcoindRpcService;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcExampleResponses;
import org.junit.Before;
import org.junit.Test;

public class BitcoindRpcHealthCheckTest {

    private BitcoindRpcHealthCheck bitcoindRpcHealthCheck;
    private BitcoindRpcService bitcoindRpcService;

    @Before
    public final void setUp() throws Exception {
        bitcoindRpcService = mock(BitcoindRpcService.class);
        bitcoindRpcHealthCheck = new BitcoindRpcHealthCheck(bitcoindRpcService);
    }

    @Test
    public final void testCheckHealthy() throws Exception {
        when(bitcoindRpcService.getBlockchainInfo())
                .thenReturn(BitcoindRpcExampleResponses.getBlockchainInfoResult());
        assertTrue(bitcoindRpcHealthCheck.check().isHealthy());
    }

    @Test
    public final void testCheckUnhealthyZeroBlocks() throws Exception {
        BlockchainInfo blockchainInfo = mock(BlockchainInfo.class);
        when(blockchainInfo.getNumBlocks()).thenReturn(0L);
        when(bitcoindRpcService.getBlockchainInfo()).thenReturn(blockchainInfo);
        assertFalse(bitcoindRpcHealthCheck.check().isHealthy());
    }

    @Test
    public final void testCheckUnhealthyException() throws Exception {
        doThrow(new IllegalStateException("dummy message")).when(bitcoindRpcService).getBlockchainInfo();
        assertFalse(bitcoindRpcHealthCheck.check().isHealthy());
    }
}
