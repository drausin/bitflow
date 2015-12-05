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

package org.drausin.bitflow.blockchain.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;

/**
 * MixIn to map Bitcoind RPC json fields to object fields
 *
 * Created by dwulsin on 12/4/15.
 */
public abstract class BlockRpcMixIn {

    @JsonCreator
    public BlockRpcMixIn(
            @JsonProperty("hash") Sha256Hash headerHash,
            @JsonProperty("confirmations") long numConfirmations,
            @JsonProperty("size") long sizeBytes,
            @JsonProperty("height") long height,
            @JsonProperty("version") long version,
            @JsonProperty("merkleroot") Sha256Hash merkleRoot,
            @JsonProperty("tx") List<Sha256Hash> transactionIds,
            @JsonProperty("time") long createdTime,
            @JsonProperty("nonce") long nonce,
            @JsonProperty("bits") BigInteger difficultyTarget,
            @JsonProperty("difficulty") double difficulty,
            @JsonProperty("chainwork") BigInteger chainwork,
            @JsonProperty("previousblockhash") Sha256Hash previousBlockHash,
            @JsonProperty("nextblockhash") Sha256Hash nextBlockHash) {}

    @JsonProperty("hash")
    abstract Sha256Hash getHeaderHash();

    @JsonProperty("confirmations")
    abstract long getNumConfirmations();

    @JsonProperty("size")
    abstract long getSizeBytes();

    @JsonProperty("height")
    abstract long getHeight();

    @JsonProperty("version")
    abstract long getVersion();

    @JsonProperty("merkleroot")
    abstract Sha256Hash getMerkleRoot();

    @JsonProperty("tx")
    abstract List<Sha256Hash> getTransactionIds();

    @JsonProperty("time")
    abstract long getCreatedTime();

    @JsonProperty("nonce")
    abstract long getNonce();

    @JsonProperty("bits")
    abstract BigInteger getDifficultyTarget();

    @JsonProperty("difficulty")
    abstract double getDifficulty();

    @JsonProperty("chainwork")
    abstract BigInteger getChainwork();

    @JsonProperty("previousblockhash")
    abstract Sha256Hash getPreviousBlockHash();

    @JsonProperty("nextblockhash")
    abstract Sha256Hash getNextBlockHash();
}
