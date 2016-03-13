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

package org.drausin.bitflow.streams;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.BlockchainService;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.streams.kafka.KafkaConsumerProducerFactory;
import org.drausin.bitflow.streams.responses.HydrateBlockHeaderStreamResponse;
import org.drausin.bitflow.streams.serde.AvroObjectReaderWriterFactoryImpl;
import org.drausin.bitflow.streams.serde.BlockHeaderKafkaSerde;
import org.drausin.bitflow.streams.serde.Sha256HashKafkaSerde;

public final class StreamResource implements StreamService {

    private static final long POLL_WAIT_MILLIS = 100;
    private static final String STREAM_TOPIC_SEPARATOR = ".";
    private static final String BLOCK_HEADERS_STREAM_PREFIX = "block-headers";
    private static final Pattern BLOCK_HEADERS_STREAM_PATTERN = streamPattern(BLOCK_HEADERS_STREAM_PREFIX);

    private final KafkaConsumer<Sha256Hash, BlockHeader> blockHeaderConsumer;
    private final KafkaProducer<Sha256Hash, BlockHeader> blockHeaderProducer;
    private final BlockchainService blockchain;

    public StreamResource(KafkaConsumerProducerFactory kafkaConsumerProducerFactory,
            BlockchainService blockchain) throws JsonMappingException {

        this.blockchain = blockchain;
        this.blockHeaderProducer = kafkaConsumerProducerFactory.createBlockHeaderProducer();
        this.blockHeaderConsumer = kafkaConsumerProducerFactory.createBlockHeaderConsumer();
        this.blockHeaderConsumer.subscribe(BLOCK_HEADERS_STREAM_PATTERN, new NoOpConsumerRebalanceListener());
    }

    @Override
    public List<String> listBlockHeaderStreams() {
        return blockHeaderConsumer.listTopics().keySet().stream()
                .map(topic -> blockHeaderStreamName(topic))
                .collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
    }

    @Override
    public Optional<BlockHeader> getLatestBlockHeader(String stream) {
        blockHeaderConsumer.seekToEnd();
        Iterator<ConsumerRecord<Sha256Hash, BlockHeader>> latestIter =
                blockHeaderConsumer.poll(POLL_WAIT_MILLIS).records(blockHeaderTopicName(stream)).iterator();
        BlockHeader latest = null;
        while (latestIter.hasNext()) {
            BlockHeader next = latestIter.next().value();
            if (latest == null || latest.getHeight() < next.getHeight()) {
                latest = next;
            }
        }
        return Optional.ofNullable(latest);
    }

    @Override
    public HydrateBlockHeaderStreamResponse hydrateBlockHeaderStream(String stream, long from, long to) {
        String topic = blockHeaderTopicName(stream);
        List<BlockHeader> blockHeaders = blockchain.getBlockHeaderHeightSubchain(null, from, to);
        for (BlockHeader blockHeader : blockHeaders) {
            blockHeaderProducer.send(new ProducerRecord<>(topic, blockHeader.getHeaderHash(), blockHeader));
        }
        return HydrateBlockHeaderStreamResponse.of(stream, blockHeaders.size());
    }

    public static String blockHeaderStreamName(String topic) {
        return streamName(BLOCK_HEADERS_STREAM_PREFIX, topic);
    }

    public static String blockHeaderTopicName(String stream) {
        return topicName(BLOCK_HEADERS_STREAM_PREFIX, stream);
    }

    private static String streamName(String prefix, String topic) {
        return topic.substring(prefix.length() + STREAM_TOPIC_SEPARATOR.length());
    }

    private static String topicName(String prefix, String stream) {
        return prefix + STREAM_TOPIC_SEPARATOR + stream;
    }

    private static Pattern streamPattern(String prefix) {
        return Pattern.compile(String.format("%s.+", prefix));
    }
}
