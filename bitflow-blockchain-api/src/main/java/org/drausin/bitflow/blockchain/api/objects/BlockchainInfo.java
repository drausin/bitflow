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

package org.drausin.bitflow.blockchain.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigInteger;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.immutables.value.Value;

/**
 * Current information about the blockchain as returned by the Bitcoind <a href="https://bitcoin.org/en/developer-reference#getblockchaininfo">GetInfo()
 * RPC</a>.
 *
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBlockchainInfo.class)
@JsonDeserialize(as = ImmutableBlockchainInfo.class)
public abstract class BlockchainInfo implements BlockchainResult {

    /**
     * Get the name of the blockchain (i.e., one of {'main', 'test', 'regtest'}.
     */
    @Value.Parameter
    @JsonProperty("chain")
    public abstract String getChain();

    /**
     * Get the number of validated blocks in the local best block chain.
     */
    @Value.Parameter
    @JsonProperty("blocks")
    public abstract long getNumBlocks();

    /**
     * Get the number of validated headers in the local best headers chain.
     */
    @Value.Parameter
    @JsonProperty("headers")
    public abstract long getNumHeaders();

    /**
     * Get the hash of the header of the highest validated block in the best block chain.
     */
    @Value.Parameter
    @JsonProperty("bestblockhash")
    public abstract Sha256Hash getBestBlockHash();

    /**
     * Get the difficulty of the highest-height block in the best block chain.
     */
    @Value.Parameter
    @JsonProperty("difficulty")
    public abstract double getDifficulty();

    /**
     * Get the estimate of what percentage of the block chain transactions have been verified so far, starting at 0.0
     * and increasing to 1.0 for fully verified.
     */
    @Value.Parameter
    @JsonProperty("verificationprogress")
    public abstract double getVerificationProgress();

    /**
     * Get the estimated number of block header hashes checked from the genesis block to this block.
     */
    @Value.Parameter
    @JsonProperty("chainwork")
    public abstract BigInteger getChainwork();

    /**
     * Get whether the blocks are subject to pruning.
     */
    @Value.Parameter
    @JsonProperty("pruned")
    public abstract boolean getPruned();

    /**
     * Get the highest pruned block.
     */
    @Value.Parameter
    @JsonProperty("pruneheight")
    public abstract long getPruneHeight();

    /**
     * Get the status of softforks in progress.
     *
     * @see <a href=https://en.bitcoin.it/wiki/Softfork>Softfork</a>
     */
    @Value.Parameter
    @JsonProperty("softforks")
    public abstract List<SoftFork> getSoftForks();

    public static BlockchainInfo of(String chain, long numBlocks, long numHeaders, Sha256Hash bestBlockHash,
            double difficulty, double verificationProgress, BigInteger chainwork, boolean pruned, long pruneHeight,
            List<SoftFork> softForks) {
        return ImmutableBlockchainInfo.of(chain, numBlocks, numHeaders, bestBlockHash, difficulty, verificationProgress,
                chainwork, pruned, pruneHeight, (Iterable<? extends SoftFork>) softForks);
    }
}
