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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.BlockchainService;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.streams.kafka.KafkaConsumerProducerFactory;
import org.drausin.bitflow.streams.responses.HydrateBlockHeaderStreamResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class StreamResourceTest {

    private StreamResource streams;

    private static final Set<String> TOPIC_NAMES =
            ImmutableSet.of("block-headers.stream-1", "block-headers.stream-2", "block-headers.stream-3");

    @Mock
    private KafkaConsumerProducerFactory kafkaConsumerProducerFactory;

    @Mock
    private BlockchainService blockchain;

    @Mock
    private KafkaConsumer<Sha256Hash, BlockHeader> blockHeaderKafkaConsumer;

    @Mock
    private KafkaProducer<Sha256Hash, BlockHeader> blockHeaderKafkaProducer;

    @Mock
    private Map<String, List<PartitionInfo>> topics;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(kafkaConsumerProducerFactory.createBlockHeaderConsumer()).thenReturn(blockHeaderKafkaConsumer);
        when(kafkaConsumerProducerFactory.createBlockHeaderProducer()).thenReturn(blockHeaderKafkaProducer);
        streams = new StreamResource(kafkaConsumerProducerFactory, blockchain);
    }

    @Test
    public void listBlockHeaderStreams() throws Exception {
        when(topics.keySet()).thenReturn(TOPIC_NAMES);
        when(blockHeaderKafkaConsumer.listTopics()).thenReturn(topics);
        assertEquals(ImmutableList.of("stream-1", "stream-2", "stream-3"), streams.listBlockHeaderStreams());
    }

    @Test
    public void getLatestBlockHeader() throws Exception {
        Map<String, Long> recordsPerStream =
                ImmutableMap.of("stream-1", 97L, "stream-2", 50L, "stream-3", 4L);
        ConsumerRecords<Sha256Hash, BlockHeader> records = createBlockHeaderConsumerRecords(5, recordsPerStream);
        when(blockHeaderKafkaConsumer.poll(anyLong())).thenReturn(records);

        assertEquals("should be highest record for a stream with unequal number of records across partitions",
                recordsPerStream.get("stream-1") - 1, streams.getLatestBlockHeader("stream-1").get().getHeight());

        assertEquals("should be highest record for a stream with equal number of records across partitions",
                recordsPerStream.get("stream-2") - 1, streams.getLatestBlockHeader("stream-2").get().getHeight());

        // this assert fails because of https://issues.apache.org/jira/browse/KAFKA-3392
        //assertEquals("should be highest record for a stream with fewer records than partitions",
        //        recordsPerStream.get("stream-3") - 1, streams.getLatestBlockHeader("stream-3").get().getHeight());

        assertFalse("should be absent for a stream with no records",
                streams.getLatestBlockHeader("stream-4").isPresent());

    }

    @Test
    public void hydrateBlockHeaderStream() throws Exception {
        int numBlocks = 5;
        List<BlockHeader> blockHeaders = Lists.newArrayList();
        for (int c = 0; c < numBlocks; c++) {
            BlockHeader blockHeader = mock(BlockHeader.class);
            when(blockHeader.getHeaderHash()).thenReturn(Sha256Hash.of(String.valueOf(c).getBytes(Charsets.UTF_8)));
            blockHeaders.add(blockHeader);
        }
        when(blockchain.getBlockHeaderHeightSubchain(anyString(), anyLong(), anyLong())).thenReturn(blockHeaders);

        String stream = "stream-1";
        int from = 0;
        int to = from + numBlocks;
        HydrateBlockHeaderStreamResponse response = streams.hydrateBlockHeaderStream(stream, from, to);

        assertEquals("response should have correct stream name and number of blocks",
                response, HydrateBlockHeaderStreamResponse.of(stream, numBlocks));

        // verify that each blockHeader is sent to the producer
        String topic = StreamResource.blockHeaderTopicName(stream);
        for (BlockHeader blockHeader : blockHeaders) {
            verify(blockHeaderKafkaProducer).send(new ProducerRecord<>(topic, blockHeader.getHeaderHash(),
                    blockHeader));
        }
    }

    private static ConsumerRecords<Sha256Hash, BlockHeader> createBlockHeaderConsumerRecords(int numPartitions,
            Map<String, Long> recordsPerStream) {
        final long fakeFixedBlockBytes = 100;
        Map<TopicPartition, List<ConsumerRecord<Sha256Hash, BlockHeader>>> records = Maps.newHashMap();
        for (String streamName : recordsPerStream.keySet()) {
            for (int p = 0; p < numPartitions; p++) {
                TopicPartition partition = new TopicPartition(StreamResource.blockHeaderTopicName(streamName), p);
                List<ConsumerRecord<Sha256Hash, BlockHeader>> partitionRecords = Lists.newArrayList();
                final long numRecords = recordsPerStream.get(StreamResource.blockHeaderStreamName(partition.topic()));
                for (long r = partition.partition(); r < numRecords; r += numPartitions) {
                    final long fakeOffset = (r / numPartitions) * fakeFixedBlockBytes;
                    Sha256Hash blockHeaderHash = Sha256Hash.of((partition.topic() + String.valueOf(r))
                                .getBytes(Charsets.UTF_8));
                    BlockHeader blockHeader = mock(BlockHeader.class);
                    when(blockHeader.getHeaderHash()).thenReturn(blockHeaderHash);
                    when(blockHeader.getHeight()).thenReturn(r);
                    partitionRecords.add(new ConsumerRecord<>(partition.topic(), partition.partition(), fakeOffset,
                            blockHeaderHash, blockHeader));
                }
                // permute records based on (roughly random) header hash
                partitionRecords.sort(
                        (ConsumerRecord<Sha256Hash, BlockHeader> o1, ConsumerRecord<Sha256Hash, BlockHeader> o2) ->
                                o1.key().compareTo(o2.key())
                );
                records.put(partition, ImmutableList.copyOf(partitionRecords));
            }
        }
        return new ConsumerRecords<>(ImmutableMap.copyOf(records));
    }
}
