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
import com.google.common.base.Optional;
import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;
import org.drausin.bitflow.bitcoin.api.config.BitcoinNodeClientConfig;

public class ServerConfig extends Configuration {

    private final String instance;
    private final BitcoinNodeClientConfig bitcoinNode;
    private final Optional<Boolean> includeStackTraceInErrors;

    public ServerConfig(
            @JsonProperty("instance") @NotNull String instance,
            @JsonProperty("bitcoinNode") @NotNull BitcoinNodeClientConfig bitcoinNode,
            @JsonProperty("includeStackTraceInErrors") @NotNull Optional<Boolean> includeStackTraceInErrors) {
        this.instance = instance;
        this.bitcoinNode = bitcoinNode;
        this.includeStackTraceInErrors = includeStackTraceInErrors;
    }

    public final String getInstance() {
        return instance;
    }

    public final BitcoinNodeClientConfig getBitcoinNode() {
        return bitcoinNode;
    }

    public final Optional<Boolean> getIncludeStackTraceInErrors() {
        return includeStackTraceInErrors;
    }
}
