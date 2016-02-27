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

package org.drausin.bitflow.streams;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.drausin.bitflow.streams.blocks.BlockHeaderStream;

@Path("/streams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StreamsService {

    @GET
    @Path("/block-headers/list")
    List<BlockHeaderStream> listBlockHeaderStreams(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader);

    @POST
    @Path("block-headers/create")
    BlockHeaderStream createBlockHeaderStream(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @QueryParam("name") String name);

    @POST
    @Path("block-headers/delete")
    BlockHeaderStream deleteBlockHeaderStream(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @QueryParam("name") String name);
}
