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

package org.drausin.bitflow.blockchain.api.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.bitcoinj.core.Sha256Hash;

/**
 * Immutable implementation of {@link BlockHeader}.
 *
 * @author dwulsin
 */
@Immutable
public final class ImmutableBlockHeader implements BlockHeader {

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
    public ImmutableBlockHeader(
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

    @Override
    public Sha256Hash getHeaderHash() {
        return headerHash;
    }

    @Override
    public long getNumConfirmations() {
        return numConfirmations;
    }

    @Override
    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    public long getHeight() {
        return height;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public Sha256Hash getMerkleRoot() {
        return merkleRoot;
    }

    @Override
    public List<Sha256Hash> getTransactionIds() {
        return transactionIds;
    }

    @Override
    public long getCreatedTime() {
        return createdTime;
    }

    @Override
    public long getNonce() {
        return nonce;
    }

    @Override
    public BigInteger getDifficultyTarget() {
        return difficultyTarget;
    }

    @Override
    public double getDifficulty() {
        return difficulty;
    }

    @Override
    public BigInteger getChainwork() {
        return chainwork;
    }

    @Override
    public Sha256Hash getPreviousBlockHash() {
        return previousBlockHash;
    }

    @Override
    public Sha256Hash getNextBlockHash() {
        return nextBlockHash;
    }
}