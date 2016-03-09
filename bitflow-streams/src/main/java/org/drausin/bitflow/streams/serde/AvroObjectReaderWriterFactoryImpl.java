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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import org.drausin.bitflow.serde.BitflowMapperFactory;

public final class AvroObjectReaderWriterFactoryImpl implements AvroObjectReaderWriterFactory {

    private final ObjectMapper mapper = BitflowMapperFactory.createMapper(new AvroFactory());

    public AvroObjectReaderWriterFactoryImpl() {}

    public ObjectWriter createWriter(Class<?> type)
            throws JsonMappingException {
        return mapper.writer(generateAvroSchema(type));
    }

    public ObjectReader createReader(Class<?> type)
            throws JsonMappingException {
        return mapper.readerFor(type).with(generateAvroSchema(type));
    }

    private AvroSchema generateAvroSchema(Class<?> type) throws JsonMappingException {
        AvroSchemaGenerator gen = new AvroSchemaGenerator();
        mapper.acceptJsonFormatVisitor(type, gen);
        return gen.getGeneratedSchema();
    }
}
