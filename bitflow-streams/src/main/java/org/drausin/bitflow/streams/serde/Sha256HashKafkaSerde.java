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

package org.drausin.bitflow.streams.serde;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.bitcoinj.core.Sha256Hash;

public final class Sha256HashKafkaSerde implements Deserializer<Sha256Hash>, Serializer<Sha256Hash> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, Sha256Hash data) {
        return data.getBytes();
    }

    @Override
    public Sha256Hash deserialize(String topic, byte[] data) {
        return Sha256Hash.wrap(data);
    }

    @Override
    public void close() {}
}
