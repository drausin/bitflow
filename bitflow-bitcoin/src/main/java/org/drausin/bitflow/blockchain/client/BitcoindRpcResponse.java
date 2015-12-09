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

package org.drausin.bitflow.blockchain.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.immutables.value.Value;

/**
 * The RPC request to the bitcoin daemon.
 *
 * @see <a href="https://bitcoin.org/en/developer-reference#remote-procedure-calls-rpcs">Bitcoin RPCs</a>
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE)
@JsonDeserialize(as = ImmutableBitcoindRpcResponse.class)
public abstract class BitcoindRpcResponse {

    @Value.Parameter
    @JsonProperty("result")
    public abstract BlockchainResult getResult();

    @Value.Parameter
    @JsonProperty("error")
    public abstract BitcoindRpcResponseError getError();

    @Value.Parameter
    @JsonProperty("id")
    public abstract String getId();

    public static BitcoindRpcResponse of(BlockchainResult result, BitcoindRpcResponseError error, String id) {
        return ImmutableBitcoindRpcResponse.of(result, error, id);
    }
}
