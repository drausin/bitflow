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

package org.drausin.bitflow.bitcoin.api.objects.responses;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import org.drausin.bitflow.bitcoin.api.objects.ImmutableBlockchainInfoResponse;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.immutables.value.Value;

/**
 * An RPC response with a BlockchainInfo result.
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBlockchainInfoResponse.class)
@JsonDeserialize(as = ImmutableBlockchainInfoResponse.class)
public abstract class BlockchainInfoResponse extends BitcoinNodeResponse<BlockchainInfo> {

    public static BlockchainInfoResponse of(Optional<BlockchainInfo> result, Optional<BitcoinNodeResponseError> error,
            Optional<String> id) {
        return ImmutableBlockchainInfoResponse.of(result, error, id);
    }
}
