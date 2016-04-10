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

import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.Before;
import org.junit.Test;

public final class KafkaConsumerProducerFactoryImplTest {

    private KafkaConsumerProducerFactory kafkaConsumerProducerFactory;
    private Properties kafkaConsumerProperties = new Properties();
    private Properties kafkaProducerProperties = new Properties();

    @Before
    public void setUp() throws Exception {
        kafkaConsumerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConsumerProperties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "TestConsumer");

        kafkaProducerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaProducerProperties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "TestProducer");

        kafkaConsumerProducerFactory = new KafkaConsumerProducerFactoryImpl(kafkaConsumerProperties,
                kafkaProducerProperties);
    }

    @Test
    public void createBlockHeaderConsumer() throws Exception {
        kafkaConsumerProducerFactory.createBlockHeaderConsumer();
    }

    @Test
    public void createBlockHeaderProducer() throws Exception {
        kafkaConsumerProducerFactory.createBlockHeaderProducer();
    }
}
