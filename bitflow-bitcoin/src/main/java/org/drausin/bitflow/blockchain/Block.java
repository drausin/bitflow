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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.bitcoinj.core.Sha256Hash;

/**
 * Information about a block as returned by the Bitcoind GetBlock() RPC
 *
 * Created by dwulsin on 12/3/15.
 */
@Immutable
public final class Block {

    private final Sha256Hash headerHash;
    private final long numConfirmations;
    private final long sizeBytes;
    private final long height;
    private final long version;
    private final Sha256Hash merkleRoot;
    private final List<Sha256Hash> transactionIds;
    private final long createdTime;
    private final long nonce;
    private final BigInteger difficultyTarget;
    private final double difficulty;
    private final BigInteger chainwork;
    private final Sha256Hash previousBlockHash;
    private final Sha256Hash nextBlockHash;

    @JsonCreator
    public Block(
            @JsonProperty("headerHash") Sha256Hash headerHash,
            @JsonProperty("numConfirmations") long numConfirmations,
            @JsonProperty("sizeBytes") long sizeBytes,
            @JsonProperty("height") long height,
            @JsonProperty("version") long version,
            @JsonProperty("merkleRoot") Sha256Hash merkleRoot,
            @JsonProperty("transactionIds") List<Sha256Hash> transactionIds,
            @JsonProperty("createdTime") long createdTime,
            @JsonProperty("nonce") long nonce,
            @JsonProperty("difficultyTarget") BigInteger difficultyTarget,
            @JsonProperty("difficulty") double difficulty,
            @JsonProperty("chainwork") BigInteger chainwork,
            @JsonProperty("previousBlockHash") Sha256Hash previousBlockHash,
            @JsonProperty("nextBlockHash") Sha256Hash nextBlockHash) {
        this.headerHash = headerHash;
        this.numConfirmations = numConfirmations;
        this.sizeBytes = sizeBytes;
        this.height = height;
        this.version = version;
        this.merkleRoot = merkleRoot;
        this.transactionIds = transactionIds;
        this.createdTime = createdTime;
        this.nonce = nonce;
        this.difficultyTarget = difficultyTarget;
        this.difficulty = difficulty;
        this.chainwork = chainwork;
        this.previousBlockHash = previousBlockHash;
        this.nextBlockHash = nextBlockHash;
    }

    @JsonProperty("headerHash")
    public Sha256Hash getHeaderHash() {
        return headerHash;
    }

    @JsonProperty("numConfirmations")
    public long getNumConfirmations() {
        return numConfirmations;
    }

    @JsonProperty("sizeBytes")
    public long getSizeBytes() {
        return sizeBytes;
    }

    @JsonProperty("height")
    public long getHeight() {
        return height;
    }

    @JsonProperty("version")
    public long getVersion() {
        return version;
    }

    @JsonProperty("merkleRoot")
    public Sha256Hash getMerkleRoot() {
        return merkleRoot;
    }

    @JsonProperty("transactionIds")
    public List<Sha256Hash> getTransactionIds() {
        return transactionIds;
    }

    @JsonProperty("createdTime")
    public long getCreatedTime() {
        return createdTime;
    }

    @JsonProperty("nonce")
    public long getNonce() {
        return nonce;
    }

    @JsonProperty("difficultyTarget")
    public BigInteger getDifficultyTarget() {
        return difficultyTarget;
    }

    @JsonProperty("difficulty")
    public double getDifficulty() {
        return difficulty;
    }

    @JsonProperty("chainwork")
    public BigInteger getChainwork() {
        return chainwork;
    }

    @JsonProperty("previousBlockHash")
    public Sha256Hash getPreviousBlockHash() {
        return previousBlockHash;
    }

    @JsonProperty("nextBlockHash")
    public Sha256Hash getNextBlockHash() {
        return nextBlockHash;
    }
}
