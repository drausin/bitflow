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
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
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

        // available blocks [228183, 230815]
        long from = 228185;
        long to = 228195;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServce(from, to);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);

        List<BlockHeader> subchain = blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to);
        assertEquals(to - from + 1, subchain.size());
        for (int c = 1; c < subchain.size(); c++) {
            assertEquals(subchain.get(c).getHeaderHash(), subchain.get(c - 1).getNextBlockHash().get());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testGetBlockHeaderHeightSubchainUnavailableStart() throws Exception {

        // available blocks [228183, 230815]
        long from = 228180;
        long to = 228190;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServce(from, to);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);
        blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to);
    }

    @Test(expected = RuntimeException.class)
    public void testGetBlockHeaderHeightSubchainUnavailableEnd() throws Exception {

        // available blocks [228183, 230815]
        long from = 230810;
        long to = 230820;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServce(from, to);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);
        blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBlockHeaderHeightSubchainChanged() throws Exception {

        // available blocks [228183, 230815]
        long from = 228185;
        long to = 228195;
        BitcoinNodeService bitcoinNodeService = mockBitcoinNodeServce(from, to, true);
        BlockchainResource blockchainResource = new BlockchainResource(bitcoinNodeService);
        blockchainResource.getBlockHeaderHeightSubchain(authHeader, from, to);
    }

    private BitcoinNodeService mockBitcoinNodeServce(long from, long to) throws Exception {
        return mockBitcoinNodeServce(from, to, false);
    }

    private BitcoinNodeService mockBitcoinNodeServce(long from, long to, boolean useDifferentToHash)
            throws Exception {

        Map<Long, Sha256Hash> blockHeightHashes = getTestBlockHeightHashes(from, to);
        BitcoinNodeService bitcoinNodeService = mock(BitcoinNodeService.class);

        // mock getBlockchainInfo() call
        when(bitcoinNodeService.getBlockchainInfo(any(BitcoinNodeRequest.class))).thenReturn(
                BitcoinNodeExampleResponses.getBlockchainInfoResponse());

        // mock getBlockHeaderHash() calls for to & from
        when(bitcoinNodeService.getBlockHeaderHash(BitcoinNodeRequestFactory.createBlockHeaderHashRequest(from)))
                .thenReturn(BlockHeaderHashResponse.of(Optional.of(blockHeightHashes.get(from)), Optional.absent(),
                        Optional.absent()));
        Sha256Hash toHash = blockHeightHashes.get(to);
        if (useDifferentToHash) {
            toHash = blockHeightHashes.get(from); // not important what we're getting, just that it's different
        }
        when(bitcoinNodeService.getBlockHeaderHash(BitcoinNodeRequestFactory.createBlockHeaderHashRequest(to)))
                .thenReturn(BlockHeaderHashResponse.of(Optional.of(toHash), Optional.absent(),
                        Optional.absent()));

        // mock getBlockHeader() calls for every block hash in subchain
        for (long h = from; h <= to; h++) {
            BlockHeader blockHeader = mock(BlockHeader.class);
            when(blockHeader.getNextBlockHash()).thenReturn(Optional.of(blockHeightHashes.get(h + 1)));
            when(blockHeader.getHeaderHash()).thenReturn(blockHeightHashes.get(h));
            Sha256Hash blockHash = blockHeightHashes.get(h);
            when(bitcoinNodeService.getBlockHeader(BitcoinNodeRequestFactory.createBlockHeaderRequest(blockHash)))
                    .thenReturn(BlockHeaderResponse.of(Optional.of(blockHeader), Optional.absent(), Optional.absent()));
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
