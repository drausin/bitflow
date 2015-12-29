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

package org.drausin.bitflow.blockchain.client;

import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;

/**
 * Client service for bitcoinc RPCs.
 *
 * @author dwulsin
 */
public interface BitcoindRpcService {

    /**
     * Get the current information about the blockchain.
     */
    BlockchainInfo getBlockchainInfo();

    /**
     * Get the header for a given block header hash.
     */
    BlockHeader getBlockHeader(Sha256Hash headerHash);
}
