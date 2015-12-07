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

package org.drausin.bitflow.blockchain.api.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.BlockHeader;

/**
 * MixIn to map Bitcoind RPC json fields to object fields.
 *
 * @author dwulsin
 */
public abstract class BlockHeaderRpcMixIn implements BlockHeader {

    @JsonCreator
    public BlockHeaderRpcMixIn(
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
    @Override
    public abstract Sha256Hash getHeaderHash();

    @JsonProperty("confirmations")
    @Override
    public abstract long getNumConfirmations();

    @JsonProperty("size")
    @Override
    public abstract long getSizeBytes();

    @JsonProperty("height")
    @Override
    public abstract long getHeight();

    @JsonProperty("version")
    @Override
    public abstract long getVersion();

    @JsonProperty("merkleroot")
    @Override
    public abstract Sha256Hash getMerkleRoot();

    @JsonProperty("tx")
    @Override
    public abstract List<Sha256Hash> getTransactionIds();

    @JsonProperty("time")
    @Override
    public abstract long getCreatedTime();

    @JsonProperty("nonce")
    @Override
    public abstract long getNonce();

    @JsonProperty("bits")
    @Override
    public abstract BigInteger getDifficultyTarget();

    @JsonProperty("difficulty")
    @Override
    public abstract double getDifficulty();

    @JsonProperty("chainwork")
    @Override
    public abstract BigInteger getChainwork();

    @JsonProperty("previousblockhash")
    @Override
    public abstract Sha256Hash getPreviousBlockHash();

    @JsonProperty("nextblockhash")
    @Override
    public abstract Sha256Hash getNextBlockHash();
}
