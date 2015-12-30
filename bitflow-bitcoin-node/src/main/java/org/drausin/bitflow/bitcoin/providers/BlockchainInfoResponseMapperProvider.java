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

package org.drausin.bitflow.bitcoin.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javax.ws.rs.ext.Provider;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.api.objects.BlockchainResult;
import org.drausin.bitflow.blockchain.api.objects.ImmutableBlockchainInfo;
import org.drausin.bitflow.blockchain.api.objects.mixin.BlockchainInfoRpcMixIn;
import org.drausin.bitflow.blockchain.api.serde.BlockchainInfoDeserializer;

/**
 * {@link ObjectMapper} for a response with a {@link BlockchainInfo} result.
 *
 * @author dwulsin
 */
@Provider
public class BlockchainInfoResponseMapperProvider extends BitcoinNodeMapperProvider {

    private final ObjectMapper mapper;

    public BlockchainInfoResponseMapperProvider() {
        this.mapper = getBlockchainInfoMapper();
    }

    @Override
    public final ObjectMapper getContext(Class<?> type) {
        return this.mapper;
    }

    public static ObjectMapper getBlockchainInfoMapper() {
        return getCommonMapper()
                .registerModule(getBlockchainInfoModule())
                .addMixIn(ImmutableBlockchainInfo.class, BlockchainInfoRpcMixIn.class);
    }

    private static SimpleModule getBlockchainInfoModule() {
        return getCommonModule().addDeserializer(BlockchainResult.class, new BlockchainInfoDeserializer());
    }

}
