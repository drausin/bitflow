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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import javax.annotation.concurrent.Immutable;
import org.bitcoinj.core.Sha256Hash;

/**
 * Immutable implementation of {@link BlockchainInfo}. This is implemented manually because we need a @JsonCreator
 * constructor in order to work with {@link org.drausin.bitflow.blockchain.api.objects.mixin.BlockchainInfoRpcMixIn}.
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

    @Override
    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ImmutableBlockchainInfo that = (ImmutableBlockchainInfo) obj;

        if (getNumBlocks() != that.getNumBlocks()) {
            return false;
        }
        if (getNumHeaders() != that.getNumHeaders()) {
            return false;
        }
        if (Double.compare(that.getDifficulty(), getDifficulty()) != 0) {
            return false;
        }
        if (Double.compare(that.getVerificationProgress(), getVerificationProgress()) != 0) {
            return false;
        }
        if (!getChain().equals(that.getChain())) {
            return false;
        }
        if (!getBestBlockHash().equals(that.getBestBlockHash())) {
            return false;
        }
        return getChainwork().equals(that.getChainwork());

    }

    @Override
    @SuppressWarnings("checkstyle:variabledeclarationusagedistance")
    public int hashCode() {
        int result;
        long temp;
        result = getChain().hashCode();
        result = 31 * result + (int) (getNumBlocks() ^ (getNumBlocks() >>> 32));
        result = 31 * result + (int) (getNumHeaders() ^ (getNumHeaders() >>> 32));
        result = 31 * result + getBestBlockHash().hashCode();
        temp = Double.doubleToLongBits(getDifficulty());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getVerificationProgress());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getChainwork().hashCode();
        return result;
    }
}
