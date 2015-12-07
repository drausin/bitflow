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

package org.drausin.bitflow.blockchain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import javax.annotation.concurrent.Immutable;
import org.bitcoinj.core.Sha256Hash;

/**
 * Immutable implementation of {@link BlockchainInfo}.
 *
 * @author dwulsin
 */
@Immutable
public final class ImmutableBlockchainInfo implements BlockchainInfo {
    private final String chain;
    private final long numBlocks;
    private final long numHeaders;
    private final Sha256Hash bestBlockHash;
    private final double difficulty;
    private final double verificationProgress;
    private final BigInteger chainwork;

    @JsonCreator
    public ImmutableBlockchainInfo(
            @JsonProperty("chain") String chain,
            @JsonProperty("numBlocks") long numBlocks,
            @JsonProperty("numHeaders") long numHeaders,
            @JsonProperty("bestBlockHash") Sha256Hash bestBlockHash,
            @JsonProperty("difficulty") double difficulty,
            @JsonProperty("verificationProgress") double verificationProgress,
            @JsonProperty("chainwork") BigInteger chainwork) {
        this.chain = chain;
        this.numBlocks = numBlocks;
        this.numHeaders = numHeaders;
        this.bestBlockHash = bestBlockHash;
        this.difficulty = difficulty;
        this.verificationProgress = verificationProgress;
        this.chainwork = chainwork;
    }

    @Override
    public String getChain() {
        return chain;
    }

    @Override
    public long getNumBlocks() {
        return numBlocks;
    }

    @Override
    public long getNumHeaders() {
        return numHeaders;
    }

    @Override
    public Sha256Hash getBestBlockHash() {
        return bestBlockHash;
    }

    @Override
    public double getDifficulty() {
        return difficulty;
    }

    @Override
    public double getVerificationProgress() {
        return verificationProgress;
    }

    @Override
    public BigInteger getChainwork() {
        return chainwork;
    }
}
