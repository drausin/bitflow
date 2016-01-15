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
    private static final String BLOCK_HEADER_RPC_METHOD = "getblock";

    private BitcoinNodeRequestFactory() {}

    public static BitcoinNodeRequest createBlockchainInfoRequest() {
        return BitcoinNodeRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of());
    }

    public static BitcoinNodeRequest createBlockchainInfoRequest(String id) {
        return BitcoinNodeRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of(), id);
    }

    public static BitcoinNodeRequest createBlockHeaderRequest(Sha256Hash headerHash) {
        return BitcoinNodeRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash));
    }

    public static BitcoinNodeRequest createBlockHeaderRequest(Sha256Hash headerHash, String id) {
        return BitcoinNodeRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash), id);
    }
}
