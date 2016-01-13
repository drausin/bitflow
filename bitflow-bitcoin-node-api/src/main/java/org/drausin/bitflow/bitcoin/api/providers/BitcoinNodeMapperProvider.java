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

package org.drausin.bitflow.bitcoin.api.providers;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import java.math.BigInteger;
import javax.ws.rs.ext.ContextResolver;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerSerializer;
import org.drausin.bitflow.blockchain.api.serde.Sha256HashSerializer;


public class BitcoinNodeMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public BitcoinNodeMapperProvider() {
        this.mapper = getMapper();
    }

    @Override
    public final ObjectMapper getContext(Class<?> type) {
        return this.mapper;
    }


    public static SimpleModule getModule() {
        return new SimpleModule("BitcoinNodeModule", Version.unknownVersion())
                .addSerializer(Sha256Hash.class, new Sha256HashSerializer())
                .addSerializer(BigInteger.class, new BigIntegerSerializer())
                .addDeserializer(BigInteger.class, new BigIntegerDeserializer());
    }

    public static ObjectMapper getMapper() {
        return new ObjectMapper()
                .registerModule(new GuavaModule())
                .registerModule(getModule());
    }
}
