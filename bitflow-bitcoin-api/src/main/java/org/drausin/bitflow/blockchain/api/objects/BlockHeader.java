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

package org.drausin.bitflow.blockchain.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigInteger;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;

/**
 * Information about a block as returned by the Bitcoind
 * <a href="https://bitcoin.org/en/developer-reference#getblock">GetBlock() RPC</a>.
 *
 * @author dwulsin
 */
public interface BlockHeader extends BlockchainResult {

    /**
     * Get the hash of the block header.
     */
    @JsonProperty
    Sha256Hash getHeaderHash();

    /**
     * Get the number of confirmations the transactions in this block have. This value starts at 1 when this block is
     * at the tip of the best block chain. It will be -1 if the the block is not part of the best block chain.
     */
    @JsonProperty
    long getNumConfirmations();

    /**
     * Get the number of bytes of this block in serialized block format.
     */
    @JsonProperty
    long getSizeBytes();

    /**
     * Get the height of this block on its block chain.
     */
    @JsonProperty
    long getHeight();

    /**
     * Get this block’s version number.
     * @see <a href="https://bitcoin.org/en/developer-reference#block-versions">Block Versions</a>
     */
    @JsonProperty
    long getVersion();

    /**
     * Get the merkle root for this block.
     */
    @JsonProperty
    Sha256Hash getMerkleRoot();

    /**
     * Get a list of the IDs of the transactions in this block. The transactions appear in the array in the same
     * order they appear in the serialized block. While not technically part of the block header, this list is handy
     * and reasonably lightweight, so we include it.
     */
    @JsonProperty
    List<Sha256Hash> getTransactionIds();

    /**
     * Get the approximate time when the block was created, store as seconds since the the 1970 epoch.
     */
    @JsonProperty
    long getCreatedTime();

    /**
     * Get the nonce which was successful at turning this particular block into one that could be added to the best.
     * block chain.
     */
    @JsonProperty
    long getNonce();

    /**
     * Get the target threshhold (a.k.a., nBits) the block header had to pass.
     */
    @JsonProperty
    BigInteger getDifficultyTarget();

    /**
     * Get the estimated amount of work done to find this block relative to the estimated amount of work done to find
     * block 0.
     */
    @JsonProperty
    double getDifficulty();

    /**
     * Get the estimated number of block header hashes miners had to check from the genesis block to this block.
     */
    @JsonProperty
    BigInteger getChainwork();

    /**
     * Get the header hash of the previous block.
     */
    @JsonProperty
    Sha256Hash getPreviousBlockHash();

    /**
     * Get the header hash of the next block.
     */
    @JsonProperty
    Sha256Hash getNextBlockHash();
}
