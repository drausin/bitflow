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

package org.drausin.bitflow.bitcoin.api.requests;

import com.google.common.collect.ImmutableList;
import org.bitcoinj.core.Sha256Hash;

/**
 * Factory for generating BitcoinNodeRequest objects for various RPC methods.
 *
 * @author dwulsin
 */
public final class BitcoinNodeRequestFactory {

    private static final String BLOCKCHAIN_INFO_RPC_METHOD = "getblockchaininfo";
    private static final String BLOCK_HEADER_HASH_RPC_METHOD = "getblockhash";
    private static final String BLOCK_HEADER_RPC_METHOD = "getblock";
    private static final String STOP_RPC_METHOD = "stop";

    private BitcoinNodeRequestFactory() {}

    /**
     * Create a BlockchainInfo request.
     *
     * @return the request
     */
    public static BitcoinNodeRequest createBlockchainInfoRequest() {
        return BitcoinNodeRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of());
    }

    /**
     * Create a BlockchainInfo request with a supplied RPC call ID.
     *
     * @param id the RPC ID to use
     * @return the request
     */
    public static BitcoinNodeRequest createBlockchainInfoRequest(String id) {
        return BitcoinNodeRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of(), id);
    }

    /**
     * Create a BlockHeaderHash request.
     * @param blockHeight the height of the block
     * @return the request
     */
    public static BitcoinNodeRequest createBlockHeaderHashRequest(long blockHeight) {
        return BitcoinNodeRequest.of(BLOCK_HEADER_HASH_RPC_METHOD, ImmutableList.of(blockHeight));
    }

    /**
     * Create a BlockHeaderHash request.
     * @param blockHeight the height of the block
     * @param id the RPC ID to use
     * @return the request
     */
    public static BitcoinNodeRequest createBlockHeaderHashRequest(long blockHeight, String id) {
        return BitcoinNodeRequest.of(BLOCK_HEADER_HASH_RPC_METHOD, ImmutableList.of(blockHeight), id);
    }

    /**
     * Create a BlockHeader request.
     *
     * @param headerHash the header hash of the block to get
     * @return the request
     */
    public static BitcoinNodeRequest createBlockHeaderRequest(Sha256Hash headerHash) {
        return BitcoinNodeRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash));
    }

    /**
     * Create a BlockHeader request.
     *
     * @param headerHash the header hash of the block to get
     * @param id the RPC ID to use
     * @return the request
     */
    public static BitcoinNodeRequest createBlockHeaderRequest(Sha256Hash headerHash, String id) {
        return BitcoinNodeRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash), id);
    }

    /**
     * Create a Stop request.
     * @return the request
     */
    public static BitcoinNodeRequest createStopRequest() {
        return BitcoinNodeRequest.of(STOP_RPC_METHOD, ImmutableList.of());
    }

    /**
     * Create a Stop request with a supplied RPC call ID.
     * @param id the RPC ID to use
     * @return the request
     */
    public static BitcoinNodeRequest createStopRequest(String id) {
        return BitcoinNodeRequest.of(STOP_RPC_METHOD, ImmutableList.of(), id);
    }
}
