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

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;

/**
 * Created by dwulsin on 12/4/15.
 */
public class BlockRpcMixInTest {

    private static class BlockRpcMixInImpl extends BlockRpcMixIn {

        BlockRpcMixInImpl(
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
                @JsonProperty("nextblockhash") Sha256Hash nextBlockHash) {
            super(headerHash, numConfirmations, sizeBytes, height, version, merkleRoot, transactionIds, createdTime,
                    nonce, difficultyTarget, difficulty, chainwork, previousBlockHash, nextBlockHash);
        }

        @Override
        Sha256Hash getHeaderHash() {
            return null;
        }

        @Override
        long getNumConfirmations() {
            return 0;
        }

        @Override
        long getSizeBytes() {
            return 0;
        }

        @Override
        long getHeight() {
            return 0;
        }

        @Override
        long getVersion() {
            return 0;
        }

        @Override
        public Sha256Hash getMerkleRoot() {
            return null;
        }

        @Override
        List<Sha256Hash> getTransactionIds() {
            return null;
        }

        @Override
        long getCreatedTime() {
            return 0;
        }

        @Override
        long getNonce() {
            return 0;
        }

        @Override
        BigInteger getDifficultyTarget() {
            return null;
        }

        @Override
        double getDifficulty() {
            return 0;
        }

        @Override
        BigInteger getChainwork() {
            return null;
        }

        @Override
        Sha256Hash getPreviousBlockHash() {
            return null;
        }

        @Override
        Sha256Hash getNextBlockHash() {
            return null;
        }
    }

    @Test
    public final void constructorTest() throws Exception {

        // there must be some better way to test BlockRpcMixIn constructor
        assertEquals(0L, (new BlockRpcMixInImpl(Sha256Hash.ZERO_HASH, 0L, 0L, 0L, 0L,
                Sha256Hash.ZERO_HASH, new ArrayList<>(), 0L, 0L, BigInteger.ZERO, 0.0, BigInteger.ZERO,
                Sha256Hash.ZERO_HASH, Sha256Hash.ZERO_HASH)).getHeight());

    }

}
