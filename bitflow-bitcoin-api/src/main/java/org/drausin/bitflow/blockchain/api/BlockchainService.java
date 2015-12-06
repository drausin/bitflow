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
import org.drausin.bitflow.blockchain.BlockHeader;
import org.drausin.bitflow.blockchain.BlockchainInfo;
import org.joda.time.DateTime;

@Path("/blockchain")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BlockchainService {

    /**
     * Gets current information about the blockchain.
     */
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    BlockchainInfo getBlockchainInfo(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader);

    /**
     * Gets the block header for a given block hash.
     *
     * @param hash the hash of the block
     */
    @GET
    @Path("/block/header/{hash}")
    @Produces(MediaType.APPLICATION_JSON)
    BlockHeader getBlockHeader(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("hash") String hash);

    /**
     * Gets the block header subchain of blocks created within a given time window.
     *
     * @param from (optional) the time after (inclusive) which to get the first block; must be specified if to
     * parameter is not
     * @param to (optional) the time before (exclusive) which to get the last block
     */
    @GET
    @Path("/block/header/subchain/time")
    @Produces(MediaType.APPLICATION_JSON)
    List<BlockHeader> getBlockHeaderTimeSubchain(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @CheckForNull @QueryParam("from") DateTime from,
            @CheckForNull @QueryParam("to") DateTime to);

    /**
     * Gets the block header subchain of blocks created within a given height range, relative to the current best block.
     *
     * @param from the height above (inclusive) which to get the first block
     * @param to the height below (exclusive) which to get the last block
     */
    @GET
    @Path("/block/header/subchain/height")
    @Produces(MediaType.APPLICATION_JSON)
    List<BlockHeader> getBlockHeaderHeightSubchain(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @CheckForNull @QueryParam("from") DateTime from,
            @CheckForNull @QueryParam("to") DateTime to);
}
