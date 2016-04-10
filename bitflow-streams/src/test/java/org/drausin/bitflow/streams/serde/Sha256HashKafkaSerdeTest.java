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

import static org.junit.Assert.assertEquals;

import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;

public final class Sha256HashKafkaSerdeTest extends KafkaSerdeTest<Sha256HashKafkaSerde> {

    private static final Sha256Hash value =
            Sha256Hash.wrap("000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f");

    @Test
    public void configure() throws Exception {
        super.configure(new Sha256HashKafkaSerde());
    }

    @Test
    public void serde() {
        Sha256HashKafkaSerde sha256HashKafkaSerde = new Sha256HashKafkaSerde();

        assertEquals("serializing and then deserializing should yield the same value", value,
                sha256HashKafkaSerde.deserialize(null, sha256HashKafkaSerde.serialize(null, value)));

        assertEquals("serializing does not depend on topic",
                sha256HashKafkaSerde.deserialize(null, sha256HashKafkaSerde.serialize("topic-0", value)),
                sha256HashKafkaSerde.deserialize(null, sha256HashKafkaSerde.serialize("topic-1", value)));

        assertEquals("deserializing does not depend on topic",
                sha256HashKafkaSerde.deserialize("topic-0", sha256HashKafkaSerde.serialize(null, value)),
                sha256HashKafkaSerde.deserialize("topic-1", sha256HashKafkaSerde.serialize(null, value)));
    }

    @Test
    public void close() throws Exception {
        super.close(new Sha256HashKafkaSerde());
    }

}
