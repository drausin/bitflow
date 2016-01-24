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

package org.drausin.bitflow.blockchain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import io.dropwizard.jersey.params.DateTimeParam;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequest;
import org.drausin.bitflow.bitcoin.api.requests.BitcoinNodeRequestFactory;
import org.drausin.bitflow.bitcoin.api.responses.BitcoinNodeExampleResponses;
import org.drausin.bitflow.bitcoin.api.responses.BlockHeaderHashResponse;
import org.drausin.bitflow.bitcoin.api.responses.BlockHeaderResponse;
import org.drausin.bitflow.bitcoin.api.responses.BlockchainInfoResponse;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.blockchain.validation.SubchainValidator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;

public final class BlockchainResourceTest {

    private static String authHeader = "dummy auth header";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetBlockchainInfo() throws Exception {

        BitcoinNodeService bitcoinNodeService = mock(BitcoinNodeService.class);
        when(bitcoinNodeService.getBlockchainInfo(any(BitcoinNodeRequest.class))).thenReturn(
                BitcoinNodeExampleResponses.getBlockchainInfoResponse());
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);

        assertEquals(
                BitcoinNodeExampleResponses.getBlockchainInfoResponse().getResult().get(),
                blockchainResource.getBlockchainInfo(authHeader));
    }

    @Test
    public void testGetBlockHeader() throws Exception {

        BitcoinNodeService bitcoinNodeService = mock(BitcoinNodeService.class);
        when(bitcoinNodeService.getBlockHeader(any(BitcoinNodeRequest.class))).thenReturn(
                BitcoinNodeExampleResponses.getBlockHeaderResponse());
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);

        Sha256Hash headerHash = BitcoinNodeExampleResponses.getBlockHeaderResponse().getResult().get().getHeaderHash();
        assertEquals(
                BitcoinNodeExampleResponses.getBlockHeaderResponse().getResult().get(),
                blockchainResource.getBlockHeader(authHeader, headerHash));
    }

    @Test
    public void testGetBlockHeaderHeightSubchain() throws Exception {

        long from = 228185;
        long to = 228195;
        long buffer = 15;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServceForSubchain(from - buffer, to + buffer);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);

        List<BlockHeader> subchain = blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to);
        SubchainValidator.validateSubchain(subchain, to - from + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBlockHeaderHeightSubchainUnavailableStart() throws Exception {

        long from = 228185;
        long to = 228195;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServceForSubchain(from, to);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);
        blockchainResource.getBlockHeaderHeightSubchain(authHeader, from - 5, to);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBlockHeaderHeightSubchainUnavailableEnd() throws Exception {

        long from = 228185;
        long to = 228195;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServceForSubchain(from, to);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);
        blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to + 5);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBlockHeaderHeightSubchainChanged() throws Exception {

        long from = 228185;
        long to = 228195;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServceForSubchain(from, to, true);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);
        blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to);
    }

    @Test
    public void testGetBlockHeaderTimeSubchain() throws Exception {

        long from = 228185;
        long to = 228195;
        long buffer = 6 * 25; // 25 hrs
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServceForSubchain(from - buffer, to + buffer);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);

        testGetBlockHeaderTimeSubchain(
                formatDateTime(new DateTime(from * 10 * 60 * 1000 + 500, DateTimeZone.UTC)),
                formatDateTime(new DateTime(to * 10 * 60 * 1000 - 500, DateTimeZone.UTC)),
                blockchainResource);

        testGetBlockHeaderTimeSubchain(
                formatDateTime(new DateTime(from * 10 * 60 * 1000 + 500, DateTimeZone.UTC)),
                formatDateTime(new DateTime(to * 10 * 60 * 1000 + 500, DateTimeZone.UTC)),
                blockchainResource);

        testGetBlockHeaderTimeSubchain(
                formatDateTime(new DateTime(from * 10 * 60 * 1000 - 500, DateTimeZone.UTC)),
                formatDateTime(new DateTime(to * 10 * 60 * 1000 - 500, DateTimeZone.UTC)),
                blockchainResource);

        testGetBlockHeaderTimeSubchain(
                formatDateTime(new DateTime(from * 10 * 60 * 1000, DateTimeZone.UTC)),
                formatDateTime(new DateTime(to * 10 * 60 * 1000 - 500, DateTimeZone.UTC)),
                blockchainResource);

        testGetBlockHeaderTimeSubchain(
                formatDateTime(new DateTime(from * 10 * 60 * 1000 + 500, DateTimeZone.UTC)),
                formatDateTime(new DateTime(to * 10 * 60 * 1000, DateTimeZone.UTC)),
                blockchainResource);

        testGetBlockHeaderTimeSubchain(
                formatDateTime(new DateTime(from * 10 * 60 * 1000, DateTimeZone.UTC)),
                formatDateTime(new DateTime(to * 10 * 60 * 1000, DateTimeZone.UTC)),
                blockchainResource);

        testGetBlockHeaderTimeSubchain(
                formatDate(new DateTime(from * 10 * 60 * 1000 + 500, DateTimeZone.UTC)),
                formatDate(new DateTime(to * 10 * 60 * 1000 - 500, DateTimeZone.UTC)),
                blockchainResource);
    }

    private void testGetBlockHeaderTimeSubchain(DateTimeParam fromTime, DateTimeParam toTime,
            BlockchainResource blockchainResource) {
        List<BlockHeader> subchain = blockchainResource.getBlockHeaderTimeSubchain(authHeader, fromTime,
                toTime);
        SubchainValidator.validateSubchain(subchain, fromTime.get(), toTime.get());
    }

    private DateTimeParam formatDateTime(DateTime time) {
        return new DateTimeParam(time.toString(ISODateTimeFormat.dateTime()));
    }

    private DateTimeParam formatDate(DateTime time) {
        return new DateTimeParam(time.toString(ISODateTimeFormat.date()));
    }

    private BitcoinNodeService mockBitcoinNodeServceForSubchain(long from, long to) throws Exception {
        return mockBitcoinNodeServceForSubchain(from, to, false);
    }

    private BitcoinNodeService mockBitcoinNodeServceForSubchain(long from, long to, boolean useDifferentToHash)
            throws Exception {

        Map<Long, Sha256Hash> blockHeightHashes = getTestBlockHeightHashes(from, to);
        BitcoinNodeService bitcoinNodeService = mock(BitcoinNodeService.class);

        // mock getBlockchainInfo() call
        BlockchainInfo blockchainInfo = mock(BlockchainInfo.class);
        when(blockchainInfo.getNumBlocks()).thenReturn(to);
        when(blockchainInfo.getPruneHeight()).thenReturn(from);
        when(bitcoinNodeService.getBlockchainInfo(any(BitcoinNodeRequest.class))).thenReturn(
                BlockchainInfoResponse.of(Optional.of(blockchainInfo), Optional.absent(), Optional.absent()));

        // mock getBlockHeader() calls for every block hash in subchain
        for (long h = from; h <= to; h++) {

            BlockHeader blockHeader = mock(BlockHeader.class);
            when(blockHeader.getNextBlockHash()).thenReturn(Optional.of(blockHeightHashes.get(h + 1)));
            if (h > from) {
                when(blockHeader.getPreviousBlockHash()).thenReturn(Optional.of(blockHeightHashes.get(h - 1)));
            }
            when(blockHeader.getHeaderHash()).thenReturn(blockHeightHashes.get(h));
            when(blockHeader.getHeight()).thenReturn(h);
            when(blockHeader.getCreatedTime()).thenReturn(h * 10 * 60); // each block is ~10 mins

            Sha256Hash blockHash = blockHeightHashes.get(h);
            when(bitcoinNodeService.getBlockHeader(BitcoinNodeRequestFactory.createBlockHeaderRequest(blockHash)))
                    .thenReturn(BlockHeaderResponse.of(blockHeader));

            if (h == to && useDifferentToHash) {
                // not important what we're getting, just that it's different
                when(bitcoinNodeService.getBlockHeaderHash(BitcoinNodeRequestFactory.createBlockHeaderHashRequest(to)))
                        .thenReturn(BlockHeaderHashResponse.of(blockHeightHashes.get(from)));
            } else {
                when(bitcoinNodeService.getBlockHeaderHash(BitcoinNodeRequestFactory.createBlockHeaderHashRequest(h)))
                        .thenReturn(BlockHeaderHashResponse.of(blockHeightHashes.get(h)));
            }
        }

        return bitcoinNodeService;
    }

    private Map<Long, Sha256Hash> getTestBlockHeightHashes(long from, long to) {
        Map<Long, Sha256Hash> hashes = Maps.newHashMap();
        for (long h = from; h <= to + 1; h++) {
            byte[] bytes = ByteBuffer.allocate(Long.BYTES).putLong(h).array();
            hashes.put(h, Sha256Hash.of(bytes));
        }
        return hashes;
    }
}
