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

package org.drausin.bitflow.blockchain.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * The RPC response error from the bitcoind daemon.
 *
 * @see <a href="https://bitcoin.org/en/developer-reference#remote-procedure-calls-rpcs">Bitcoin RPCs</a>
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBitcoindRpcResponseError.class)
@JsonDeserialize(as = ImmutableBitcoindRpcResponseError.class)
public abstract class BitcoindRpcResponseError {

    @Value.Parameter
    @JsonProperty("code")
    public abstract long getCode();

    @Value.Parameter
    @JsonProperty("message")
    public abstract String getMessage();

    public static BitcoindRpcResponseError of(long code, String message) {
        return ImmutableBitcoindRpcResponseError.of(code, message);
    }
}
