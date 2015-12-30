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
import org.apache.commons.lang3.Validate;

/**
 * Bitcoin node configuration.
 *
 * @author dwulsin
 */
public final class ServerConfig {

    private final String uri;
    private final String rpcUser;
    private final String rpcPassword;

    public ServerConfig(
            @JsonProperty("uri") String uri,
            @JsonProperty("rpcUser") String rpcUser,
            @JsonProperty("rpcPassword") String rpcPassword) {
        this.uri = Validate.notEmpty(uri, "Must specify an RPC service URI");
        this.rpcUser = Validate.notEmpty(rpcUser, "Must specify a non-empty RPC user");
        this.rpcPassword = rpcPassword;
    }

    public String getUri() {
        return uri;
    }

    public String getRpcUser() {
        return rpcUser;
    }

    public String getRpcPassword() {
        return rpcPassword;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ServerConfig that = (ServerConfig) obj;

        if (!getUri().equals(that.getUri())) {
            return false;
        }
        if (!getRpcUser().equals(that.getRpcUser())) {
            return false;
        }
        return getRpcPassword().equals(that.getRpcPassword());

    }

    @Override
    public int hashCode() {
        int result = getUri().hashCode();
        result = 31 * result + getRpcUser().hashCode();
        result = 31 * result + getRpcPassword().hashCode();
        return result;
    }
}
