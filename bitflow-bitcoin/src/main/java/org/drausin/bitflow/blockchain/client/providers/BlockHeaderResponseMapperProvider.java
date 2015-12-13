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

package org.drausin.bitflow.blockchain.client.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javax.ws.rs.ext.Provider;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.drausin.bitflow.blockchain.api.objects.ImmutableBlockHeader;
import org.drausin.bitflow.blockchain.api.objects.mixin.BlockHeaderRpcMixIn;
import org.drausin.bitflow.blockchain.api.serde.BlockHeaderDeserializer;

/**
 * {@link ObjectMapper} for a response with a {@link BlockHeader} result.
 *
 * @author dwulsin
 */
@Provider
public class BlockHeaderResponseMapperProvider extends BitcoindRpcResponseMapperProvider {
    private final ObjectMapper mapper;

    public BlockHeaderResponseMapperProvider() {
        this.mapper = getBlockHeaderMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return this.mapper;
    }

    private static SimpleModule getBlockHeaderModule(){
        return getCommonModule().addDeserializer(BlockchainResult.class, new BlockHeaderDeserializer());
    }

    private static ObjectMapper getBlockHeaderMapper() {
        return getCommonMapper()
                .registerModule(getBlockHeaderModule())
                .addMixIn(ImmutableBlockHeader.class, BlockHeaderRpcMixIn.class);
    }
}
