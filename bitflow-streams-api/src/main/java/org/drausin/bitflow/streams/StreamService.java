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
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.streams.responses.HydrateBlockHeaderStreamResponse;

@Path("/streams")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StreamService {

    @GET
    @Path("/block-headers/list")
    List<String> listBlockHeaderStreams();

    @GET
    @Path("/block-headers/get-latest")
    Optional<BlockHeader> getLatestBlockHeader(@QueryParam("stream") String stream);

    @POST
    @Path("/block-headers/hydrate")
    HydrateBlockHeaderStreamResponse hydrateBlockHeaderStream(
            @QueryParam("stream") String stream,
            @QueryParam("from") long from,
            @QueryParam("to") long to);
}
