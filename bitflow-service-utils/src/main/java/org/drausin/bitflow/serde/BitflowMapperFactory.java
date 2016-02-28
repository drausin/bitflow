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

package org.drausin.bitflow.serde;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.dropwizard.jackson.Jackson;
import java.math.BigInteger;
import org.bitcoinj.core.Sha256Hash;


public final class BitflowMapperFactory {

    private BitflowMapperFactory() {}

    public static SimpleModule createModule() {
        return new SimpleModule("BitflowModule", Version.unknownVersion())
                .addSerializer(Sha256Hash.class, new Sha256HashJsonSerializer())
                .addSerializer(BigInteger.class, new BigIntegerJsonSerializer())
                .addDeserializer(Sha256Hash.class, new Sha256HashJsonDeserializer())
                .addDeserializer(BigInteger.class, new BigIntegerJsonDeserializer());
    }

    public static ObjectMapper createMapper() {
        return Jackson.newObjectMapper()
                .registerModule(createModule());
    }


}
