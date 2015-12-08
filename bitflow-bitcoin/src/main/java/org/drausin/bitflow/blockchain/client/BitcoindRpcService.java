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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;

/**
 * Created by dwulsin on 12/7/15.
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public interface BitcoindRpcService {

    /**
     * Get the current information about the blockchain.
     */
    @POST
    BitcoindRpcResponse<BlockchainInfo> getBlockchainInfo();

    /**
     * Get the header for a given block header hash.
     */
    @POST
    BitcoindRpcResponse<BlockHeader> getBlockHeader(
            Sha256Hash headerHash);
}
