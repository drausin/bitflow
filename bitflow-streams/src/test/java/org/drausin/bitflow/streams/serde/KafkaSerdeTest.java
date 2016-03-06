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

import org.apache.kafka.common.serialization.Serializer;

public class KafkaSerdeTest<T extends Serializer> {

    @SuppressWarnings("unchecked")
    public final void configure(T serde) throws Exception {
        int origHash = serde.hashCode();
        serde.configure(null, false);
        int finalHash = serde.hashCode();

        assertEquals("configure() should have no effect on object", origHash, finalHash);
    }

    public final void close(T serde) throws Exception {
        int origHash = serde.hashCode();
        serde.close();
        int finalHash = serde.hashCode();

        assertEquals("close() should have no effect on object", origHash, finalHash);
    }
}
