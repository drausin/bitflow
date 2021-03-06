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

package org.drausin.bitflow.bitcoin.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import io.dropwizard.Configuration;
import org.immutables.value.Value;

/**
 * Bitcoin node configuration.
 *
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBitcoinNodeServerConfig.class)
@JsonDeserialize(as = ImmutableBitcoinNodeServerConfig.class)
public abstract class BitcoinNodeServerConfig extends Configuration {

    @JsonProperty
    public abstract String getInstance();

    @JsonProperty
    public abstract String getRpcUri();

    @JsonProperty
    public abstract NodeType getMode();

    @JsonProperty
    public abstract BitcoindExecutableConfig getBitcoinNode();

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("instance", getInstance())
                .add("rpcUri", getRpcUri())
                .add("mode", getMode())
                .add("bitcoinNode", getBitcoinNode())
                .add("server", getServerFactory())
                .add("logging", getLoggingFactory())
                .toString();
    }
}
