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

package org.drausin.bitflow.blockchain.api;

import io.dropwizard.jersey.params.DateTimeParam;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.service.utils.BitflowService;

@Path("/blockchain")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BlockchainService extends BitflowService {

    /**
     * Gets current information about the blockchain.
     */
    @GET
    @Path("/info")
    BlockchainInfo getBlockchainInfo(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader);

    /**
     * Gets the block header for a given block hash.
     *
     * @param hash the hash of the block
     */
    @GET
    @Path("/block/header/hash/{hash}")
    BlockHeader getBlockHeader(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @CheckForNull @PathParam("hash") Sha256Hash hash);

    /**
     * Gets the block header at a given block height.
     *
     * @param height the height of the block in the blockchain
     */
    @GET
    @Path("/block/header/height/{height}")
    BlockHeader getBlockHeader(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @CheckForNull @PathParam("height") long height);

    /**
     * Gets the block header subchain of blocks created within a given time window.
     *
     * @param from (optional) the time after (inclusive) which to get the first block; must be specified if to
     * parameter is not
     * @param to (optional) the time before (exclusive) which to get the last block
     */
    @GET
    @Path("/block/header/subchain/time")
    List<BlockHeader> getBlockHeaderTimeSubchain(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @CheckForNull @QueryParam("from") DateTimeParam from,
            @CheckForNull @QueryParam("to") DateTimeParam to);

    /**
     * Gets the block header subchain of blocks created within a given height range, relative to the current best block.
     *
     * @param from the height above (inclusive) which to get the first block
     * @param to the height below (inclusive) which to get the last block
     */
    @GET
    @Path("/block/header/subchain/height")
    List<BlockHeader> getBlockHeaderHeightSubchain(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @CheckForNull @QueryParam("from") long from,
            @CheckForNull @QueryParam("to") long to);
}
