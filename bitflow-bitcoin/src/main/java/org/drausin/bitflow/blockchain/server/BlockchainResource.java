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

package org.drausin.bitflow.blockchain.server;

import java.util.List;
import javax.annotation.CheckForNull;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import org.drausin.bitflow.blockchain.api.BlockchainService;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.joda.time.DateTime;

/**
 * Created by dwulsin on 12/7/15.
 */
public final class BlockchainResource implements BlockchainService {

    public BlockchainResource() {

    }

    @Override
    public BlockchainInfo getBlockchainInfo(String authHeader) {
        return null;
    }

    @Override
    public BlockHeader getBlockHeader(String authHeader, String hash) {
        return null;
    }

    @Override
    public List<BlockHeader> getBlockHeaderTimeSubchain(String authHeader, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public List<BlockHeader> getBlockHeaderHeightSubchain(String authHeader, DateTime from, DateTime to) {
        return null;
    }
}
