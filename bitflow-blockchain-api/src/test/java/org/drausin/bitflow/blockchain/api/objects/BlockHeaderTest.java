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
import com.jayway.jsonpath.JsonPath;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.mixin.BlockHeaderRpcMixIn;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerSerializer;
import org.drausin.bitflow.blockchain.api.serde.Sha256HashSerializer;
import org.junit.Before;
import org.junit.Test;

public class BlockHeaderTest {

    private ObjectMapper rpcMapper;
    private ObjectMapper standardMapper;
    private BlockHeader blockHeader1;
    private BlockHeader blockHeader2;
    private String rpcGetBlockJson;
    private String block1Json;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {

        SimpleModule blockModule = new SimpleModule("TestModule", Version.unknownVersion());
        blockModule.addSerializer(Sha256Hash.class, new Sha256HashSerializer());
        blockModule.addSerializer(BigInteger.class, new BigIntegerSerializer());
        blockModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());

        // mapper from RPC json schema
        rpcMapper = new ObjectMapper();
        rpcMapper.registerModule(blockModule);
        rpcMapper.addMixIn(ImmutableBlockHeader.class, BlockHeaderRpcMixIn.class);

        // mapper from standard json properties
        standardMapper = new ObjectMapper();
        standardMapper.registerModule(blockModule);

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

        blockHeader1 = rpcMapper.readValue(rpcGetBlockJson, ImmutableBlockHeader.class);
        block1Json = standardMapper.writeValueAsString(blockHeader1);
        blockHeader2 = standardMapper.readValue(block1Json, ImmutableBlockHeader.class);
    }

    @Test
    public final void testGetHeaderHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.hash"), blockHeader1.getHeaderHash().toString());
        assertEquals(blockHeader1.getHeaderHash().toString(), JsonPath.read(block1Json, "$.headerHash"));
        assertEquals(JsonPath.read(block1Json, "$.headerHash"), blockHeader2.getHeaderHash().toString());
    }

    @Test
    public final void testGetNumConfirmations() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetBlockJson, "$.confirmations")).longValue(),
                blockHeader1.getNumConfirmations());
        assertEquals(
                blockHeader1.getNumConfirmations(),
                ((Integer) JsonPath.read(block1Json, "$.numConfirmations")).longValue());
        assertEquals(
                ((Integer) JsonPath.read(block1Json, "$.numConfirmations")).longValue(),
                blockHeader2.getNumConfirmations());
    }

    @Test
    public final void testGetSizeBytes() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.size")).longValue(), blockHeader1.getSizeBytes());
        assertEquals(blockHeader1.getSizeBytes(), ((Integer) JsonPath.read(block1Json, "$.sizeBytes")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.sizeBytes")).longValue(), blockHeader2.getSizeBytes());
    }

    @Test
    public final void testGetHeight() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.height")).longValue(), blockHeader1.getHeight());
        assertEquals(blockHeader1.getHeight(), ((Integer) JsonPath.read(block1Json, "$.height")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.height")).longValue(), blockHeader2.getHeight());
    }

    @Test
    public final void testGetVersion() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.version")).longValue(), blockHeader1.getVersion());
        assertEquals(blockHeader1.getVersion(), ((Integer) JsonPath.read(block1Json, "$.version")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.version")).longValue(), blockHeader2.getVersion());
    }

    @Test
    public final void testGetMerkleRoot() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.merkleroot"), blockHeader1.getMerkleRoot().toString());
        assertEquals(blockHeader1.getMerkleRoot().toString(), JsonPath.read(block1Json, "$.merkleRoot"));
        assertEquals(JsonPath.read(block1Json, "$.merkleRoot"), blockHeader2.getMerkleRoot().toString());
    }

    @Test
    public final void testGetTransactionIds() throws Exception {

        assertEquals(
                JsonPath.read(rpcGetBlockJson, "$.tx").toString(),
                standardMapper.writeValueAsString(blockHeader1.getTransactionIds()));
        assertEquals(
                standardMapper.writeValueAsString(blockHeader1.getTransactionIds()),
                JsonPath.read(block1Json, "$.transactionIds").toString());
        assertEquals(
                JsonPath.read(block1Json, "$.transactionIds").toString(),
                standardMapper.writeValueAsString(blockHeader2.getTransactionIds()));
    }

    @Test
    public final void testGetCreatedTime() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.time")).longValue(), blockHeader1.getCreatedTime());
        assertEquals(blockHeader1.getCreatedTime(), ((Integer) JsonPath.read(block1Json, "$.createdTime")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.createdTime")).longValue(), blockHeader2.getCreatedTime());
    }

    @Test
    public final void testGetNonce() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.nonce")).longValue(), blockHeader1.getNonce());
        assertEquals(blockHeader1.getNonce(), ((Integer) JsonPath.read(block1Json, "$.nonce")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.nonce")).longValue(), blockHeader2.getNonce());
    }

    @Test
    public final void testGetDifficultyTarget() throws Exception {
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcGetBlockJson, "$.bits"), "0"),
                blockHeader1.getDifficultyTarget().toString(16));
        assertEquals(
                blockHeader1.getDifficultyTarget().toString(16),
                StringUtils.stripStart(JsonPath.read(block1Json, "$.difficultyTarget"), "0"));
        assertEquals(
                StringUtils.stripStart(JsonPath.read(block1Json, "$.difficultyTarget"), "0"),
                blockHeader2.getDifficultyTarget().toString(16));
    }

    @Test
    public final void testGetDifficulty() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetBlockJson, "$.difficulty"),
                blockHeader1.getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                blockHeader1.getDifficulty(),
                JsonPath.read(block1Json, "$.difficulty"), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(block1Json, "$.difficulty"),
                blockHeader2.getDifficulty(), ASSERT_EQUALS_PRECISION);
    }

    @Test
    public final void testGetChainwork() throws Exception {
        assertEquals(StringUtils.stripStart(JsonPath.read(rpcGetBlockJson, "$.chainwork").toString(), "0"),
                blockHeader1.getChainwork().toString(16));
        assertEquals(blockHeader1.getChainwork().toString(16), JsonPath.read(block1Json, "$.chainwork"));
        assertEquals(JsonPath.read(block1Json, "$.chainwork"), blockHeader2.getChainwork().toString(16));
    }

    @Test
    public final void testGetPreviousBlockHash() throws Exception {
        assertEquals(JsonPath.read(
                rpcGetBlockJson, "$.previousblockhash"),
                blockHeader1.getPreviousBlockHash().toString());
        assertEquals(blockHeader1.getPreviousBlockHash().toString(), JsonPath.read(block1Json, "$.previousBlockHash"));
        assertEquals(JsonPath.read(block1Json, "$.previousBlockHash"), blockHeader2.getPreviousBlockHash().toString());
    }

    @Test
    public final void testGetNextBlockHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.nextblockhash"), blockHeader1.getNextBlockHash().toString());
        assertEquals(blockHeader1.getNextBlockHash().toString(), JsonPath.read(block1Json, "$.nextBlockHash"));
        assertEquals(JsonPath.read(block1Json, "$.nextBlockHash"), blockHeader2.getNextBlockHash().toString());
    }
}
