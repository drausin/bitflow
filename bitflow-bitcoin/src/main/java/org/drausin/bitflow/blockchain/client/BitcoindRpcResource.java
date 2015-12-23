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

package org.drausin.bitflow.blockchain.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.client.config.BitcoindRpcServiceConfig;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcRequest;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcResponse;
import org.drausin.bitflow.blockchain.client.providers.BlockHeaderResponseMapperProvider;
import org.drausin.bitflow.blockchain.client.providers.BlockchainInfoResponseMapperProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

public final class BitcoindRpcResource implements BitcoindRpcService {

    public static final String BLOCKCHAIN_INFO_RPC_METHOD = "getinfo";
    public static final String BLOCK_HEADER_RPC_METHOD = "getblock";

    private final BitcoindRpcServiceConfig config;
    private final HttpAuthenticationFeature rpcAuth;

    private final WebTarget blockchainInfoTarget;
    private final WebTarget blockHeaderTarget;

    public BitcoindRpcResource(@JsonProperty BitcoindRpcServiceConfig config) {
        this.config = config;
        this.rpcAuth = HttpAuthenticationFeature.basic(config.getRpcUser(), config.getRpcPassword());
        this.blockchainInfoTarget = getBlockchainInfoClient().target(config.getUri());
        this.blockHeaderTarget = getBlockHeaderClient().target(config.getUri());
    }

    public BitcoindRpcServiceConfig getConfig() {
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

    public Client getBlockchainInfoClient() {
        return getBlockchainInfoClient(ClientBuilder.newClient());
    }

    public Client getBlockchainInfoClient(Client current) {
        return getCommonClient(current).register(BlockchainInfoResponseMapperProvider.class);
    }

    public Client getBlockHeaderClient() {
        return getBlockHeaderClient(ClientBuilder.newClient());
    }

    public Client getBlockHeaderClient(Client current) {
        return getCommonClient(current).register(BlockHeaderResponseMapperProvider.class);
    }

    private Client getCommonClient(Client current) {
        return current
                .register(getRpcAuth())
                .register(JacksonFeature.class);
    }

    @Override
    public BlockchainInfo getBlockchainInfo() throws IllegalStateException {
        BitcoindRpcRequest request = BitcoindRpcRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of());
        BitcoindRpcResponse response = readResponse(getBlockchainInfoTarget(), request);
        response.validateResult(BlockchainInfo.class);
        return (BlockchainInfo) response.getResult().get();
    }

    @Override
    public BlockHeader getBlockHeader(Sha256Hash headerHash) throws IllegalStateException {
        BitcoindRpcRequest request = BitcoindRpcRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash));
        BitcoindRpcResponse response = readResponse(getBlockHeaderTarget(), request);
        response.validateResult(BlockHeader.class);
        return (BlockHeader) response.getResult().get();
    }

    private static BitcoindRpcResponse readResponse(WebTarget target, BitcoindRpcRequest request) {
        return target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), BitcoindRpcResponse.class);
    }
}
