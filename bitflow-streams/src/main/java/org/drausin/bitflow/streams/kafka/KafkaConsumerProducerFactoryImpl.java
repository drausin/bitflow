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

package org.drausin.bitflow.streams.kafka;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.streams.serde.AvroObjectReaderWriterFactory;
import org.drausin.bitflow.streams.serde.AvroObjectReaderWriterFactoryImpl;
import org.drausin.bitflow.streams.serde.BlockHeaderKafkaSerde;
import org.drausin.bitflow.streams.serde.Sha256HashKafkaSerde;

public final class KafkaConsumerProducerFactoryImpl implements KafkaConsumerProducerFactory {

    private final Sha256HashKafkaSerde sha256HashKafkaSerde;
    private final BlockHeaderKafkaSerde blockHeaderKafkaSerde;
    private final Properties kafkaConsumerProperties;
    private final Properties kafkaProducerProperties;

    public KafkaConsumerProducerFactoryImpl(Properties kafkaConsumerProperties, Properties kafkaProducerProperties)
            throws JsonMappingException {

        AvroObjectReaderWriterFactory avroObjectReaderWriterFactory = new AvroObjectReaderWriterFactoryImpl();
        ObjectWriter writer = avroObjectReaderWriterFactory.createWriter(BlockHeader.class);
        ObjectReader reader = avroObjectReaderWriterFactory.createReader(BlockHeader.class);

        this.blockHeaderKafkaSerde = new BlockHeaderKafkaSerde(writer, reader);
        this.sha256HashKafkaSerde = new Sha256HashKafkaSerde();
        this.kafkaConsumerProperties = kafkaConsumerProperties;
        this.kafkaProducerProperties = kafkaProducerProperties;
    }

    @Override
    public KafkaConsumer<Sha256Hash, BlockHeader> createBlockHeaderConsumer() {
        return new KafkaConsumer<>(kafkaConsumerProperties, sha256HashKafkaSerde, blockHeaderKafkaSerde);
    }

    @Override
    public KafkaProducer<Sha256Hash, BlockHeader> createBlockHeaderProducer() {
        return new KafkaProducer<>(kafkaProducerProperties, sha256HashKafkaSerde, blockHeaderKafkaSerde);
    }
}
