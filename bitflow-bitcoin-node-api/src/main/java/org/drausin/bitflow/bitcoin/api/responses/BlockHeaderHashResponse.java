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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import org.bitcoinj.core.Sha256Hash;
import org.immutables.value.Value;

/**
 * An RPC response with a {@link org.bitcoinj.core.Sha256Hash} block header hash result.
 *
 * @see <a href="https://bitcoin.org/en/developer-reference#getblockhash">GetBlockHash RPC</a>
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBlockHeaderHashResponse.class)
@JsonDeserialize(as = ImmutableBlockHeaderHashResponse.class)
public abstract class BlockHeaderHashResponse extends BitcoinNodeResponse<Sha256Hash> {

    public static BlockHeaderHashResponse of(Optional<Sha256Hash> result, Optional<BitcoinNodeResponseError> error,
            Optional<String> id) {
        return ImmutableBlockHeaderHashResponse.of(result, error, id);
    }
}
