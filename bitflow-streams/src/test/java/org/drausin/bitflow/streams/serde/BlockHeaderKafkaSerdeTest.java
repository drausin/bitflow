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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import org.drausin.bitflow.bitcoin.api.responses.BitcoinNodeExampleResponses;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.junit.Before;
import org.junit.Test;

public final class BlockHeaderKafkaSerdeTest extends KafkaSerdeTest<BlockHeaderKafkaSerde> {

    private BlockHeader value;

    private ObjectWriter objectWriterMock = mock(ObjectWriter.class);
    private ObjectReader objectReaderMock = mock(ObjectReader.class);

    @Before
    public void setUp() throws IOException {
        value = BitcoinNodeExampleResponses.getBlockHeaderResponse().getResult().get();
    }

    @Test
    public void configure() throws Exception {
        super.configure(new BlockHeaderKafkaSerde(objectWriterMock, objectReaderMock));
    }

    @Test
    public void serde() throws Exception {
        AvroObjectReaderWriterFactory avroObjectReaderWriterFactory = new AvroObjectReaderWriterFactoryImpl();
        BlockHeaderKafkaSerde blockHeaderKafkaSerde = new BlockHeaderKafkaSerde(
                avroObjectReaderWriterFactory.createWriter(BlockHeader.class),
                avroObjectReaderWriterFactory.createReader(BlockHeader.class));

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
        when(objectWriterMock.writeValueAsBytes(any(BlockHeader.class))).thenThrow(new JsonMappingException("error"));
        BlockHeaderKafkaSerde blockHeaderKafkaSerde = new BlockHeaderKafkaSerde(objectWriterMock, objectReaderMock);
        blockHeaderKafkaSerde.serialize("topic-0", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserializeError() throws Exception {
        when(objectReaderMock.readValue(any(byte[].class))).thenThrow(new IOException("error"));
        BlockHeaderKafkaSerde blockHeaderKafkaSerde = new BlockHeaderKafkaSerde(objectWriterMock, objectReaderMock);
        blockHeaderKafkaSerde.deserialize("topic-0", new byte[]{});
    }

    @Test
    public void close() throws Exception {
        super.close(new BlockHeaderKafkaSerde(objectWriterMock, objectReaderMock));
    }
}
