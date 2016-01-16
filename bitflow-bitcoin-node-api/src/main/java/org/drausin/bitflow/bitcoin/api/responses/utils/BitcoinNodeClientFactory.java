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

package org.drausin.bitflow.bitcoin.api.responses.utils;

import com.google.common.collect.ImmutableList;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.config.BitcoinNodeClientConfig;
import org.drausin.bitflow.client.AbstractBitflowClientFactory;

/**
 * Factory for creating {@link feign.Feign} {@link BitcoinNodeService} clients.
 */
public final class BitcoinNodeClientFactory extends AbstractBitflowClientFactory<BitcoinNodeClientConfig> {

    public BitcoinNodeService createClient(BitcoinNodeClientConfig config) {
        return createClient(BitcoinNodeService.class, config);
    }

    protected String getUri(BitcoinNodeClientConfig config) {
        return config.getUri();
    }

    protected Iterable<RequestInterceptor> getRequestInterceptors(BitcoinNodeClientConfig config) {
        return ImmutableList.of(new BasicAuthRequestInterceptor(config.getUser(), config.getPassword()));
    }
}
