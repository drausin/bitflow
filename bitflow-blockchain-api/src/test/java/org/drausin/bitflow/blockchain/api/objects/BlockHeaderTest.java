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

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.jayway.jsonpath.JsonPath;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.serde.BigIntegerDeserializer;
import org.drausin.bitflow.serde.BigIntegerSerializer;
import org.drausin.bitflow.serde.Sha256HashSerializer;
import org.junit.Before;
import org.junit.Test;

public class BlockHeaderTest {

    private ObjectMapper mapper;
    private BlockHeader blockHeader;
    private String rpcGetBlockJson;
    private String blockJson;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {

        SimpleModule blockModule = new SimpleModule("TestModule", Version.unknownVersion());
        blockModule.addSerializer(Sha256Hash.class, new Sha256HashSerializer());
        blockModule.addSerializer(BigInteger.class, new BigIntegerSerializer());
        blockModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());

        // mapper from RPC json schema
        mapper = new ObjectMapper()
                .registerModule(blockModule)
                .registerModule(new GuavaModule());


        // from https://bitcoin.org/en/developer-reference#getblock
        rpcGetBlockJson = "{\n"
                + "    \"hash\" : \"000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39\",\n"
                + "    \"confirmations\" : 88029,\n"
                + "    \"size\" : 189,\n"
                + "    \"height\" : 227252,\n"
                + "    \"version\" : 2,\n"
                + "    \"merkleroot\" : \"c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a\",\n"
                + "    \"tx\" : [\n"
                + "        \"c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a\"\n"
                + "    ],\n"
                + "    \"time\" : 1398824312,\n"
                + "    \"nonce\" : 1883462912,\n"
                + "    \"bits\" : \"1d00ffff\",\n"
                + "    \"difficulty\" : 1.00000000,\n"
                + "    \"chainwork\" : \"000000000000000000000000000000000000000000000000083ada4a4009841a\",\n"
                + "    \"previousblockhash\" : \"00000000c7f4990e6ebf71ad7e21a47131dfeb22c759505b3998d7a814c011df\",\n"
                + "    \"nextblockhash\" : \"00000000afe1928529ac766f1237657819a11cfcc8ca6d67f119e868ed5b6188\"\n"
                + "}";

        blockHeader = mapper.readValue(rpcGetBlockJson, ImmutableBlockHeader.class);
        blockJson = mapper.writeValueAsString(blockHeader);
    }

    @Test
    public final void testGetHeaderHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.hash"), blockHeader.getHeaderHash().toString());
        assertEquals(blockHeader.getHeaderHash().toString(), JsonPath.read(blockJson, "$.hash"));
    }

    @Test
    public final void testGetNumConfirmations() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetBlockJson, "$.confirmations")).longValue(),
                blockHeader.getNumConfirmations());
        assertEquals(
                blockHeader.getNumConfirmations(),
                ((Integer) JsonPath.read(blockJson, "$.confirmations")).longValue());
    }

    @Test
    public final void testGetSizeBytes() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.size")).longValue(), blockHeader.getSizeBytes());
        assertEquals(blockHeader.getSizeBytes(), ((Integer) JsonPath.read(blockJson, "$.size")).longValue());
    }

    @Test
    public final void testGetHeight() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.height")).longValue(), blockHeader.getHeight());
        assertEquals(blockHeader.getHeight(), ((Integer) JsonPath.read(blockJson, "$.height")).longValue());
    }

    @Test
    public final void testGetVersion() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.version")).longValue(), blockHeader.getVersion());
        assertEquals(blockHeader.getVersion(), ((Integer) JsonPath.read(blockJson, "$.version")).longValue());
    }

    @Test
    public final void testGetMerkleRoot() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.merkleroot"), blockHeader.getMerkleRoot().toString());
        assertEquals(blockHeader.getMerkleRoot().toString(), JsonPath.read(blockJson, "$.merkleroot"));
    }

    @Test
    public final void testGetTransactionIds() throws Exception {
        assertEquals(
                JsonPath.read(rpcGetBlockJson, "$.tx").toString(),
                mapper.writeValueAsString(blockHeader.getTransactionIds()));
        assertEquals(
                mapper.writeValueAsString(blockHeader.getTransactionIds()),
                JsonPath.read(blockJson, "$.tx").toString());
    }

    @Test
    public final void testGetCreatedTime() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.time")).longValue(), blockHeader.getCreatedTime());
        assertEquals(blockHeader.getCreatedTime(), ((Integer) JsonPath.read(blockJson, "$.time")).longValue());
    }

    @Test
    public final void testGetNonce() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.nonce")).longValue(), blockHeader.getNonce());
        assertEquals(blockHeader.getNonce(), ((Integer) JsonPath.read(blockJson, "$.nonce")).longValue());
    }

    @Test
    public final void testGetDifficultyTarget() throws Exception {
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcGetBlockJson, "$.bits"), "0"),
                blockHeader.getDifficultyTarget().toString(16));
        assertEquals(
                blockHeader.getDifficultyTarget().toString(16),
                StringUtils.stripStart(JsonPath.read(blockJson, "$.bits"), "0"));
    }

    @Test
    public final void testGetDifficulty() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetBlockJson, "$.difficulty"),
                blockHeader.getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                blockHeader.getDifficulty(),
                JsonPath.read(blockJson, "$.difficulty"), ASSERT_EQUALS_PRECISION);
    }

    @Test
    public final void testGetChainwork() throws Exception {
        assertEquals(StringUtils.stripStart(JsonPath.read(rpcGetBlockJson, "$.chainwork").toString(), "0"),
                blockHeader.getChainwork().toString(16));
        assertEquals(blockHeader.getChainwork().toString(16), JsonPath.read(blockJson, "$.chainwork"));
    }

    @Test
    public final void testGetPreviousBlockHash() throws Exception {
        assertEquals(JsonPath.read(
                rpcGetBlockJson, "$.previousblockhash"),
                blockHeader.getPreviousBlockHash().toString());
        assertEquals(blockHeader.getPreviousBlockHash().toString(), JsonPath.read(blockJson, "$.previousblockhash"));
    }

    @Test
    public final void testGetNextBlockHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.nextblockhash"), blockHeader.getNextBlockHash().toString());
        assertEquals(blockHeader.getNextBlockHash().toString(), JsonPath.read(blockJson, "$.nextblockhash"));
    }

    @Test
    public final void testOf() {
        BlockHeader testBlockHeader = BlockHeader.of(blockHeader.getHeaderHash(), blockHeader.getNumConfirmations(),
                blockHeader.getSizeBytes(), blockHeader.getHeight(), blockHeader.getVersion(),
                blockHeader.getMerkleRoot(), blockHeader.getTransactionIds(), blockHeader.getCreatedTime(),
                blockHeader.getNonce(), blockHeader.getDifficultyTarget(), blockHeader.getDifficulty(),
                blockHeader.getChainwork(), blockHeader.getPreviousBlockHash(), blockHeader.getNextBlockHash());
        assertEquals(blockHeader, testBlockHeader);
    }
}
