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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;
import org.mockito.Mock;

public final class Sha256HashDeserializerTest {

    private static Sha256HashDeserializer deserializer = new Sha256HashDeserializer();

    @Mock
    private JsonParser parser = mock(JsonParser.class);

    @Test
    public void testDeserialize() throws Exception {
        String hexHash = "000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f";
        when(parser.getValueAsString()).thenReturn(hexHash);
        assertEquals(Sha256Hash.wrap(hexHash), deserializer.deserialize(parser, null));
    }

    @Test
    public void testHandledType() throws Exception {
        assertEquals(Sha256Hash.class, deserializer.handledType());
    }
}
