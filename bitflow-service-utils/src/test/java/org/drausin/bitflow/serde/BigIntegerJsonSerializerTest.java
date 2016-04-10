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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonGenerator;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BigIntegerJsonSerializerTest {

    private BigIntegerJsonSerializer bigIntegerJsonSerializer;

    @Mock
    private JsonGenerator gen;

    @Before
    public final void setUp() throws Exception {
        bigIntegerJsonSerializer = new BigIntegerJsonSerializer();
    }

    @Test
    public final void testSerialize() throws Exception {
        bigIntegerJsonSerializer.serialize(new BigInteger("256", 10), gen, null);
        verify(gen, times(1)).writeString("100");
    }

    @Test
    public final void testHandledType() throws Exception {
        assertEquals(BigInteger.class, bigIntegerJsonSerializer.handledType());
    }
}
