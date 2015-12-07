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

import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.BlockchainInfo;


/**
 * MixIn to map Bitcoind RPC json fields to object fields
 */
public abstract class BlockchainInfoRpcMixIn implements BlockchainInfo {

    @JsonCreator
    public BlockchainInfoRpcMixIn(
            @JsonProperty("chain") String chain,
            @JsonProperty("blocks") long numBlocks,
            @JsonProperty("headers") long numHeaders,
            @JsonProperty("bestblockhash") Sha256Hash bestBlockHash,
            @JsonProperty("difficulty") double difficulty,
            @JsonProperty("verificationprogress") double verificationProgress,
            @JsonProperty("chainwork") BigInteger chainwork) {}

    /**
     * {@inheritDoc}
     */
    @JsonProperty("chain")
    @Override
    public abstract String getChain();

    /**
     * {@inheritDoc}
     */
    @JsonProperty("blocks")
    @Override
    public abstract long getNumBlocks();

    /**
     * {@inheritDoc}
     */
    @JsonProperty("headers")
    @Override
    public abstract long getNumHeaders();

    /**
     * {@inheritDoc}
     */
    @JsonProperty("bestblockhash")
    @Override
    public abstract Sha256Hash getBestBlockHash();

    /**
     * {@inheritDoc}
     */
    @JsonProperty("difficulty")
    @Override
    public abstract double getDifficulty();

    /**
     * {@inheritDoc}
     */
    @JsonProperty("verificationprogress")
    @Override
    public abstract double getVerificationProgress();

    /**
     * {@inheritDoc}
     */
    @JsonProperty("chainwork")
    @Override
    public abstract BigInteger getChainwork();
}
