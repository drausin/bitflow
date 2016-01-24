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
import com.google.common.base.Optional;
import java.math.BigInteger;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.immutables.value.Value;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Information about a block as returned by the Bitcoind <a href="https://bitcoin.org/en/developer-reference#getblock">GetBlock()
 * RPC</a>.
 *
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBlockHeader.class)
@JsonDeserialize(as = ImmutableBlockHeader.class)
public abstract class BlockHeader {

    /**
     * Get the hash of the block header.
     */
    @Value.Parameter
    @JsonProperty("hash")
    public abstract Sha256Hash getHeaderHash();

    /**
     * Get the number of confirmations the transactions in this block have. This value starts at 1 when this block is at
     * the tip of the best block chain. It will be -1 if the the block is not part of the best block chain.
     */
    @Value.Parameter
    @JsonProperty("confirmations")
    public abstract long getNumConfirmations();

    /**
     * Get the number of bytes of this block in serialized block format.
     */
    @Value.Parameter
    @JsonProperty("size")
    public abstract long getSizeBytes();

    /**
     * Get the height of this block on its block chain.
     */
    @Value.Parameter
    @JsonProperty("height")
    public abstract long getHeight();

    /**
     * Get this block’s version number.
     *
     * @see <a href="https://bitcoin.org/en/developer-reference#block-versions">Block Versions</a>
     */
    @Value.Parameter
    @JsonProperty("version")
    public abstract long getVersion();

    /**
     * Get the merkle root for this block.
     */
    @Value.Parameter
    @JsonProperty("merkleroot")
    public abstract Sha256Hash getMerkleRoot();

    /**
     * Get a list of the IDs of the transactions in this block. The transactions appear in the array in the same order
     * they appear in the serialized block. While not technically part of the block header, this list is handy and
     * reasonably lightweight, so we include it.
     */
    @Value.Parameter
    @JsonProperty("tx")
    public abstract List<Sha256Hash> getTransactionIds();

    /**
     * Get the approximate time when the block was created, stored as seconds since the the 1970 epoch.
     */
    @Value.Parameter
    @JsonProperty("time")
    public abstract long getCreatedTime();

    /**
     * Get the approximate time when the block was created.
     */
    public final DateTime getCreatedDateTime() {
        return new DateTime(getCreatedTime() * 1000, DateTimeZone.UTC);
    }

    /**
     * Get the nonce which was successful at turning this particular block into one that could be added to the best.
     * block chain.
     */
    @Value.Parameter
    @JsonProperty("nonce")
    public abstract long getNonce();

    /**
     * Get the target threshhold (a.k.a., nBits) the block header had to pass.
     */
    @Value.Parameter
    @JsonProperty("bits")
    public abstract BigInteger getDifficultyTarget();

    /**
     * Get the estimated amount of work done to find this block relative to the estimated amount of work done to find
     * block 0.
     */
    @Value.Parameter
    @JsonProperty("difficulty")
    public abstract double getDifficulty();

    /**
     * Get the estimated number of block header hashes miners had to check from the genesis block to this block.
     */
    @Value.Parameter
    @JsonProperty("chainwork")
    public abstract BigInteger getChainwork();

    /**
     * Get the header hash of the previous block.
     */
    @Value.Parameter
    @JsonProperty("previousblockhash")
    public abstract Optional<Sha256Hash> getPreviousBlockHash();

    /**
     * Get the header hash of the next block.
     */
    @Value.Parameter
    @JsonProperty("nextblockhash")
    public abstract Optional<Sha256Hash> getNextBlockHash();

    public static BlockHeader of(Sha256Hash headerHash, long numConfirmations, long sizeBytes, long height,
            long version, Sha256Hash merkleRoot, List<Sha256Hash> transactionIds, long createdTime, long nonce,
            BigInteger difficultyTarget, double difficulty, BigInteger chainwork,
            Optional<Sha256Hash> previousBlockHash, Optional<Sha256Hash> nextBlockHash) {
        return ImmutableBlockHeader.of(headerHash, numConfirmations, sizeBytes, height, version, merkleRoot,
                (Iterable<? extends Sha256Hash>) transactionIds, createdTime, nonce, difficultyTarget, difficulty,
                chainwork, previousBlockHash, nextBlockHash);
    }
}
