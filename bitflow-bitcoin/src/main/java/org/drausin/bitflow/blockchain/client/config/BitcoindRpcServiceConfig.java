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

package org.drausin.bitflow.blockchain.client.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.Validate;

/**
 * RPC service configuation.
 *
 * @author dwulsin
 */
public final class BitcoindRpcServiceConfig {

    private final String uri;
    private final String rpcUser;
    private final String rpcPassword;

    public BitcoindRpcServiceConfig(
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
}
