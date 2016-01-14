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

package org.drausin.bitflow.bitcoin.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.responses.BlockHeaderResponse;
import org.drausin.bitflow.bitcoin.api.responses.BlockchainInfoResponse;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BitcoinNodeService {

    /**
     * Gets current information about the blockchain.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BlockchainInfoResponse getBlockchainInfo(BitcoinNodeRequest request);

    /**
     * Gets the block header for a given block hash.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BlockHeaderResponse getBlockHeader(BitcoinNodeRequest request);
}
