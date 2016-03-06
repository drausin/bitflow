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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.serde.BitflowMapperFactory;

@SuppressWarnings("checkstyle:designforextension")
public class BlockHeaderKafkaSerde implements Deserializer<BlockHeader>, Serializer<BlockHeader> {

    private final ObjectWriter writer;
    private final ObjectReader reader;

    public BlockHeaderKafkaSerde() throws JsonMappingException {
        ObjectMapper mapper = BitflowMapperFactory.createMapper(new AvroFactory());
        AvroSchemaGenerator gen = new AvroSchemaGenerator();
        mapper.acceptJsonFormatVisitor(BlockHeader.class, gen);
        AvroSchema avroSchema = gen.getGeneratedSchema();
        writer = mapper.writer(avroSchema);
        reader = mapper.readerFor(BlockHeader.class).with(avroSchema);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, BlockHeader data) {
        try {
            return getWriter().writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public BlockHeader deserialize(String topic, byte[] data) {
        try {
            return getReader().readValue(data);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @VisibleForTesting
    ObjectWriter getWriter() {
        return writer;
    }

    @VisibleForTesting
    ObjectReader getReader() {
        return reader;
    }

    @Override
    public void close() {}
}
