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
import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;

/**
 * Created by dwulsin on 12/4/15.
 */
public class BlockchainInfoRpcMixInTest {

    private static class BlockchainInfoRpcMixInImpl extends BlockchainInfoRpcMixIn {

        BlockchainInfoRpcMixInImpl(
                @JsonProperty("chain") String chain,
                @JsonProperty("blocks") long numBlocks,
                @JsonProperty("headers") long numHeaders,
                @JsonProperty("bestblockhash") Sha256Hash bestBlockHash,
                @JsonProperty("difficulty") double difficulty,
                @JsonProperty("verificationprogress") double verificationProgress,
                @JsonProperty("chainwork") BigInteger chainwork) {
            super(chain, numBlocks, numHeaders, bestBlockHash, difficulty, verificationProgress, chainwork);
        }

        @Override
        String getName() {
            return null;
        }

        @Override
        long getNumBlocks() {
            return 0;
        }

        @Override
        long getNumHeaders() {
            return 0;
        }

        @Override
        Sha256Hash getBestBlockHash() {
            return null;
        }

        @Override
        double getDifficulty() {
            return 0;
        }

        @Override
        double getVerificationProgress() {
            return 0;
        }

        @Override
        BigInteger getChainwork() {
            return null;
        }
    }

    @Test
    public final void constructorTest()  throws Exception {

        // dirty, but not sure a better way
        assertEquals(0L, (new BlockchainInfoRpcMixInImpl("", 0L, 0L, Sha256Hash.ZERO_HASH, 0.0, 0.0,
                BigInteger.ZERO)).getNumBlocks());
    }

}
