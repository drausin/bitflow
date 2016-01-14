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

package org.drausin.bitflow.blockchain;

import io.dropwizard.jersey.params.DateTimeParam;
import java.util.List;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequestFactory;
import org.drausin.bitflow.blockchain.api.BlockchainService;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.service.utils.BitflowResource;

public class BlockchainResource extends BitflowResource implements BlockchainService {

    private BitcoinNodeService bitcoinNodeService;

    public BlockchainResource(BitcoinNodeService bitcoinNodeService) {
        this.bitcoinNodeService = bitcoinNodeService;
    }

    @Override
    public final BlockchainInfo getBlockchainInfo(String authHeader) {
        // TODO(dwulsin): what to do with authHeader?
        BitcoinNodeRequest request = BitcoinNodeRequestFactory.createBlockchainInfoRequest();
        return bitcoinNodeService.getBlockchainInfo(request).getResult().get();
    }

    @Override
    public final BlockHeader getBlockHeader(String authHeader, Sha256Hash hash) {
        // TODO(dwulsin): what to do with authHeader?
        BitcoinNodeRequest request = BitcoinNodeRequestFactory.createBlockHeaderRequest(hash);
        return bitcoinNodeService.getBlockHeader(request).getResult().get();
    }

    @Override
    public final List<BlockHeader> getBlockHeaderTimeSubchain(String authHeader, DateTimeParam from, DateTimeParam to) {
        // TODO(dwulsin)
        return null;
    }

    @Override
    public final List<BlockHeader> getBlockHeaderHeightSubchain(String authHeader, long from, long to) {
        // TODO(dwulsin)
        return null;
    }
}
