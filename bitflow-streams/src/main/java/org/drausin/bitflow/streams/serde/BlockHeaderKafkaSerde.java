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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;

public final class BlockHeaderKafkaSerde implements Deserializer<BlockHeader>, Serializer<BlockHeader> {

    private final ObjectWriter writer;
    private final ObjectReader reader;

    public BlockHeaderKafkaSerde(AvroObjectReaderWriterFactory avroObjectReaderWriterFactory)
            throws JsonMappingException {
        writer = avroObjectReaderWriterFactory.createWriter(BlockHeader.class);
        reader = avroObjectReaderWriterFactory.createReader(BlockHeader.class);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, BlockHeader data) {
        try {
            return writer.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public BlockHeader deserialize(String topic, byte[] data) {
        try {
            return reader.readValue(data);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void close() {}
}
