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

package org.drausin.bitflow.blockchain.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;
import org.drausin.bitflow.bitcoin.api.config.BitcoinNodeClientConfig;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBlockchainServerConfig.class)
@JsonDeserialize(as = ImmutableBlockchainServerConfig.class)
public abstract class BlockchainServerConfig extends Configuration {

    @Value.Parameter
    @JsonProperty
    public abstract String getInstance();

    @Value.Parameter
    @JsonProperty
    public abstract BitcoinNodeClientConfig getBitcoinNode();

    public static BlockchainServerConfig of(String instance, BitcoinNodeClientConfig bitcoinNode) {
        return ImmutableBlockchainServerConfig.of(instance, bitcoinNode);
    }
}
