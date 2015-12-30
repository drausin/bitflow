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

import com.codahale.metrics.health.HealthCheck;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;

public class BitcoindRpcHealthCheck extends HealthCheck {

    private final BitcoinNodeService bitcoinNodeService;

    public BitcoindRpcHealthCheck(BitcoinNodeService bitcoinNodeService) {
        this.bitcoinNodeService = bitcoinNodeService;
    }

    @Override
    protected final Result check() throws Exception {
        try {
            BlockchainInfo blockchainInfo = bitcoinNodeService.getBlockchainInfo();
            if (blockchainInfo.getNumBlocks() > 0) {
                return Result.healthy(String.format("%d blocks in local best chain", blockchainInfo.getNumBlocks()));
            } else {
                return Result.unhealthy("zero blocks in local best chain");
            }
        } catch (Exception ex) {
            return Result.unhealthy(ex);
        }
    }
}
