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

package org.drausin.bitflow.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import org.bitcoinj.core.Sha256Hash;

/**
 * Current information about the blockchain as returned by the Bitcoind
 * <a href="https://bitcoin.org/en/developer-reference#getblockchaininfo">GetInfo() RPC</a>
 */
public interface BlockchainInfo {

    /**
     * The name of the blockchain (i.e., one of {'main', 'test', 'regtest'}
     */
    String getChain();

    /**
     * The number of validated blocks in the local best block chain
     */
    long getNumBlocks();

    /**
     * The number of validated headers in the local best headers chain
     */
    long getNumHeaders();

    /**
     * The hash of the header of the highest validated block in the best block chain
     */
    Sha256Hash getBestBlockHash();

    /**
     * The difficulty of the highest-height block in the best block chain
     */
    double getDifficulty();

    /**
     * Estimate of what percentage of the block chain transactions have been verified so far, starting at 0.0 and
     * increasing to 1.0 for fully verified
     */
    double getVerificationProgress();

    /**
     * The estimated number of block header hashes checked from the genesis block to this block
     */
    BigInteger getChainwork();
}
