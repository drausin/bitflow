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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import org.drausin.bitflow.bitcoin.api.responses.BitcoinNodeExampleResponses;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.junit.Before;
import org.junit.Test;

public final class BlockHeaderKafkaSerdeTest extends KafkaSerdeTest<BlockHeaderKafkaSerde> {

    private BlockHeader value;

    @Before
    public void setUp() throws IOException {
        value = BitcoinNodeExampleResponses.getBlockHeaderResponse().getResult().get();
    }

    @Test
    public void configure() throws Exception {
        super.configure(new BlockHeaderKafkaSerde());
    }

    @Test
    public void serde() throws Exception {
        BlockHeaderKafkaSerde blockHeaderKafkaSerde = new BlockHeaderKafkaSerde();

        assertEquals("serializing and then deserializing should yield the same value", value,
                blockHeaderKafkaSerde.deserialize(null, blockHeaderKafkaSerde.serialize(null, value)));

        assertEquals("serializing does not depend on topic",
                blockHeaderKafkaSerde.deserialize(null, blockHeaderKafkaSerde.serialize("topic-0", value)),
                blockHeaderKafkaSerde.deserialize(null, blockHeaderKafkaSerde.serialize("topic-1", value)));

        assertEquals("deserializing does not depend on topic",
                blockHeaderKafkaSerde.deserialize("topic-0", blockHeaderKafkaSerde.serialize(null, value)),
                blockHeaderKafkaSerde.deserialize("topic-1", blockHeaderKafkaSerde.serialize(null, value)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void serializeError() throws Exception {
        BlockHeaderKafkaSerde blockHeaderKafkaSerde = new BlockHeaderKafkaSerde();
        BlockHeaderKafkaSerde blockHeaderKafkaSerdeSpy = spy(blockHeaderKafkaSerde);
        ObjectWriter writerMock = mock(ObjectWriter.class);
        when(writerMock.writeValueAsBytes(any(Object.class))).thenThrow(new JsonGenerationException("message"));
        doReturn(writerMock).when(blockHeaderKafkaSerdeSpy).getWriter();

        blockHeaderKafkaSerdeSpy.serialize("topic-0", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeError() throws Exception {
        BlockHeaderKafkaSerde blockHeaderKafkaSerde = new BlockHeaderKafkaSerde();
        BlockHeaderKafkaSerde blockHeaderKafkaSerdeSpy = spy(blockHeaderKafkaSerde);
        ObjectReader readerMock = mock(ObjectReader.class);
        when(readerMock.readValue(any(byte[].class))).thenThrow(new IOException("message"));
        doReturn(readerMock).when(blockHeaderKafkaSerdeSpy).getReader();

        blockHeaderKafkaSerdeSpy.deserialize("topic-0", new byte[]{});
    }

    @Test
    public void close() throws Exception {
        super.configure(new BlockHeaderKafkaSerde());
    }
}
