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

package org.drausin.bitflow.bitcoin.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.immutables.value.Value;

/**
 * The RPC request to the bitcoin daemon.
 *
 * @author dwulsin
 * @see <a href="https://bitcoin.org/en/developer-reference#remote-procedure-calls-rpcs">Bitcoin RPCs</a>
 */
@SuppressWarnings("checkstyle:designforextension")
public abstract class BitcoinNodeResponse<T extends BlockchainResult> {

    @Value.Parameter
    @JsonProperty("result")
    public abstract Optional<T> getResult();

    @Value.Parameter
    @JsonProperty("error")
    public abstract Optional<BitcoinNodeResponseError> getError();

    @Value.Parameter
    @JsonProperty("id")
    public abstract Optional<String> getId();

    public boolean validateResult() {
        if (!getResult().isPresent()) {
            throw new IllegalStateException("result value is absent");
        }
        if (getError().isPresent()) {
            throw new IllegalStateException(String.format("error value is present: %s", getError().get().toString()));
        }
        return true;
    }

    public boolean validateError() {
        if (!getError().isPresent()) {
            throw new IllegalStateException("error value is absent");
        }
        if (getResult().isPresent()) {
            throw new IllegalStateException(String.format("result value is present: %s", getError().get().toString()));
        }
        return true;
    }
}
