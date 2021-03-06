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

package org.drausin.bitflow.bitcoin.api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import java.util.List;
import org.immutables.value.Value;

/**
 * The RPC request to the bitcoin daemon.
 *
 * @see <a href="https://bitcoin.org/en/developer-reference#remote-procedure-calls-rpcs">Bitcoin RPCs</a>
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBitcoinNodeRequest.class)
@JsonDeserialize(as = ImmutableBitcoinNodeRequest.class)
public abstract class BitcoinNodeRequest {

    /**
     * Get the RPC method name.
     */
    @Value.Parameter
    @JsonProperty("method")
    public abstract String getMethod();

    /**
     * Get the list of positional parameter values for the RPC.
     */
    @Value.Parameter
    @JsonProperty("params")
    public abstract List<Object> getParams();

    /**
     * Get the arbitrary string that will be returned with the response.
     */
    @Value.Parameter
    @JsonProperty("id")
    public abstract Optional<String> getId();

    public static BitcoinNodeRequest of(String method, List<Object> params) {
        return ImmutableBitcoinNodeRequest.of(method, params, Optional.absent());
    }

    public static BitcoinNodeRequest of(String method, List<Object> params, String id) {
        return ImmutableBitcoinNodeRequest.of(method, params, Optional.of(id));
    }
}
