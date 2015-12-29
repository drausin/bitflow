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

import static org.junit.Assert.assertEquals;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.client.config.BitcoindRpcServiceConfig;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcExampleResponses;
import org.drausin.bitflow.blockchain.client.objects.BitcoindRpcRequest;
import org.drausin.bitflow.blockchain.client.providers.BitcoindRpcRequestMapperProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;
import org.junit.Test;

public class BitcoindRpcResourceTest extends JerseyTest {

    private BitcoindRpcServiceConfig bitcoindRpcServiceConfig;
    private BitcoindRpcResource bitcoindRpcResource;

    @Path("/")
    public static class BitcoindRpcTestResource {

        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("")
        public final String makeRpc(BitcoindRpcRequest request) throws IOException {

            if (request.getMethod().equals(BitcoindRpcResource.BLOCKCHAIN_INFO_RPC_METHOD)) {
                return BitcoindRpcExampleResponses.getBlockchainInfoJsonResponse();
            } else if (request.getMethod().equals(BitcoindRpcResource.BLOCK_HEADER_RPC_METHOD)) {
                return BitcoindRpcExampleResponses.getBlockHeaderJsonResponse();
            } else {
                throw new IllegalArgumentException("%s is not a valid RPC request method".format(
                        request.getMethod().toString()));
            }
        }
    }

    @Override
    protected final TestContainerFactory getTestContainerFactory() {
        return new GrizzlyTestContainerFactory();
    }

    @Override
    protected final Application configure() {
        return new ResourceConfig(BitcoindRpcTestResource.class)
                .register(BitcoindRpcRequestMapperProvider.class)
                .register(JacksonFeature.class);
    }

    @Before
    @Override
    public final void setUp() throws Exception {

        bitcoindRpcServiceConfig = new BitcoindRpcServiceConfig(getBaseUri().toString(), "testrpcuser",
                "testrpcpassword");
        bitcoindRpcResource = new BitcoindRpcResource(bitcoindRpcServiceConfig);

        super.setUp();
    }

    @Test
    public final void testGetConfig() {
        assertEquals(bitcoindRpcServiceConfig, bitcoindRpcResource.getConfig());
    }

    @Test
    public final void testGetBlockchainInfo() throws Exception {

        BlockchainInfo result = bitcoindRpcResource.getBlockchainInfo();
        assertEquals(
                JsonPath.read(BitcoindRpcExampleResponses.getBlockchainInfoJsonResponse(), "$.result.chain"),
                result.getChain());
    }

    @Test
    public final void testGetBlockHeader() throws Exception {
        Sha256Hash headerHash = Sha256Hash.wrap(JsonPath.read(BitcoindRpcExampleResponses.getBlockHeaderJsonResponse(),
                "$.result.hash").toString());
        assertEquals(headerHash, bitcoindRpcResource.getBlockHeader(headerHash).getHeaderHash());
    }
}
