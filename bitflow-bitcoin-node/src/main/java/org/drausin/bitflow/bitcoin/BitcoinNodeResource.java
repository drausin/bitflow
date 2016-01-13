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

package org.drausin.bitflow.bitcoin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.objects.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.objects.BlockHeaderResponse;
import org.drausin.bitflow.bitcoin.api.objects.BlockchainInfoResponse;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.drausin.bitflow.service.utils.BitflowResource;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

public final class BitcoinNodeResource extends BitflowResource implements BitcoinNodeService {

    public static final String BLOCKCHAIN_INFO_RPC_METHOD = "getinfo";
    public static final String BLOCK_HEADER_RPC_METHOD = "getblock";

    private final ServerConfig config;
    private final HttpAuthenticationFeature rpcAuth;

    private final WebTarget blockchainInfoTarget;
    private final WebTarget blockHeaderTarget;

    public BitcoinNodeResource(@JsonProperty ServerConfig config) {
        this.config = config;
        this.rpcAuth = HttpAuthenticationFeature.basic(config.getBitcoinNode().getRpcUser(),
                config.getBitcoinNode().getRpcPassword());
        this.blockchainInfoTarget = getCommonClient().target(config.getRpcUri());
        this.blockHeaderTarget = getCommonClient().target(config.getRpcUri());
    }

    public ServerConfig getConfig() {
        return config;
    }

    public HttpAuthenticationFeature getRpcAuth() {
        return rpcAuth;
    }

    public WebTarget getBlockchainInfoTarget() {
        return blockchainInfoTarget;
    }

    public WebTarget getBlockHeaderTarget() {
        return blockHeaderTarget;
    }

    private Client getCommonClient() {
        return ClientBuilder.newClient()
                .register(getRpcAuth())
                .register(JacksonFeature.class)
                .register(BitcoinNodeMapperProvider.class);
    }

    @Override
    public BlockchainInfoResponse getBlockchainInfo() throws IllegalStateException {
        BitcoinNodeRequest request = BitcoinNodeRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of());
        BlockchainInfoResponse response = getBlockchainInfoTarget()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), BlockchainInfoResponse.class);
        response.validateResult();
        return response;
    }

    @Override
    public BlockHeaderResponse getBlockHeader(Sha256Hash headerHash) throws IllegalStateException {
        BitcoinNodeRequest request = BitcoinNodeRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash));
        BlockHeaderResponse response = getBlockHeaderTarget()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), BlockHeaderResponse.class);
        response.validateResult();
        return response;
    }
}
