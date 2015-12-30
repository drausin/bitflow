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

package org.drausin.bitflow.blockchain.api.objects;

import com.google.common.collect.ImmutableList;
import java.math.BigInteger;
import org.bitcoinj.core.Sha256Hash;

public final class ExampleResults {

    public static BlockchainInfo getBlockchainInfo() {
        return new ImmutableBlockchainInfo("test", 315280, 315280,
                Sha256Hash.wrap("000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f"), 1.0, 1.00000778,
                new BigInteger("0000000000000000000000000000000000000000000000015e984b4fb9f9b350", 16));
    }

    public static BlockHeader getBlockHeader() {
        return new ImmutableBlockHeader(
                Sha256Hash.wrap("000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39"),
                88029L, 189L, 227252L, 2L,
                Sha256Hash.wrap("c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a"),
                ImmutableList.of(Sha256Hash.wrap("c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a")),
                1398824312L, 1883462912L, new BigInteger("1d00ffff", 16), 1.0,
                new BigInteger("000000000000000000000000000000000000000000000000083ada4a4009841a", 16),
                Sha256Hash.wrap("00000000c7f4990e6ebf71ad7e21a47131dfeb22c759505b3998d7a814c011df"),
                Sha256Hash.wrap("00000000afe1928529ac766f1237657819a11cfcc8ca6d67f119e868ed5b6188"));
    }

    private ExampleResults() {}
}
