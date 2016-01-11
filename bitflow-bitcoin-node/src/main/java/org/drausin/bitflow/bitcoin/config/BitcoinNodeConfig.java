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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.File;
import java.util.Map;
import org.immutables.value.Value;

/**
 * Bitcoin node configuration.
 *
 * @author dwulsin
 */
@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableBitcoinNodeConfig.class)
@JsonDeserialize(as = ImmutableBitcoinNodeConfig.class)
public abstract class BitcoinNodeConfig {

    @JsonProperty("datadir")
    public abstract String getDataDirectory();

    @JsonProperty("conf")
    public abstract String getConfigFilePath();

    @JsonIgnore
    public final File getConfigFile() {
        return new File(getConfigFilePath());
    }

    @JsonProperty("port")
    public abstract long getNetworkPort();

    @JsonProperty("rpcport")
    public abstract long getRpcPort();

    @JsonProperty("rpcallowip")
    public abstract String rpcAllowIp();

    @JsonProperty("disablewallet")
    public abstract boolean isDisableWallet();

    @JsonProperty("printtoconsole")
    public abstract boolean isPrintToConsole();

    @JsonProperty("logtimestamps")
    public abstract boolean isLogTimestamps();

    @JsonProperty("shrinkdebugfile")
    public abstract boolean isShrinkDebugFile();

    @JsonProperty("server")
    public abstract boolean isRunAsServer();

    @JsonProperty("checkblocks")
    public abstract Map<NodeType, Long> getNumBlockToCheck();

    @JsonProperty("checklevel")
    public abstract Map<NodeType, Long> getCheckLevel();

    @JsonProperty("txindex")
    public abstract Map<NodeType, Boolean> getKeepTransactionIndex();

    @JsonProperty("prune")
    public abstract Map<NodeType, Long> getMaxDiskSize();

    @JsonProperty("reindex")
    public abstract Map<NodeType, Boolean> getReindexOnStart();

    @JsonProperty("rpcuser")
    public abstract String getRpcUser();

    @JsonProperty("rpcpassword")
    public abstract String getRpcPassword();

    public final String getConfFileValue(JsonNode value, NodeType mode) {
        if (value.isTextual()) {
            return value.asText();
        } else if (value.isNumber()) {
            return String.valueOf(value.asLong());
        } else if (value.isBoolean()) {
            return String.valueOf(value.asBoolean() ? 1 : 0);
        } else if (value.isObject()) {
            return getConfFileValue(value.findValue(mode.toString()), mode);
        } else {
            throw new IllegalStateException(
                    String.format("unrecognized type for JsonNode value: %s", value.toString()));
        }
    }
}
