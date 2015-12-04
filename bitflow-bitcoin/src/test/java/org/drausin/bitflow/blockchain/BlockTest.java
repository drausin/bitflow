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
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.mixin.BlockRpcMixIn;
import org.drausin.bitflow.blockchain.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.serde.BigIntegerSerializer;
import org.drausin.bitflow.blockchain.serde.Sha256HashSerializer;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dwulsin on 12/4/15.
 */
public class BlockTest {

    private ObjectMapper rpcMapper;
    private ObjectMapper standardMapper;
    private Block block1;
    private Block block2;
    private String rpcGetBlockJson;
    private String block1Json;
    private final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public void setUp() throws Exception {

        SimpleModule blockModule = new SimpleModule("TestModule", Version.unknownVersion());
        blockModule.addSerializer(Sha256Hash.class, new Sha256HashSerializer());
        blockModule.addSerializer(BigInteger.class, new BigIntegerSerializer());
        blockModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());

        // mapper from RPC json schema
        rpcMapper = new ObjectMapper();
        rpcMapper.registerModule(blockModule);
        rpcMapper.setMixIns(ImmutableMap.<Class<?>, Class<?>>of(Block.class, BlockRpcMixIn.class));

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

        block1 = rpcMapper.readValue(rpcGetBlockJson, Block.class);
        block1Json = standardMapper.writeValueAsString(block1);
        block2 = standardMapper.readValue(block1Json, Block.class);
    }

    @Test
    public void testGetHeaderHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.hash"), block1.getHeaderHash().toString());
        assertEquals(block1.getHeaderHash().toString(), JsonPath.read(block1Json, "$.headerHash"));
        assertEquals(JsonPath.read(block1Json, "$.headerHash"), block2.getHeaderHash().toString());
    }

    @Test
    public void testGetNumConfirmations() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetBlockJson, "$.confirmations")).longValue(),
                block1.getNumConfirmations());
        assertEquals(
                block1.getNumConfirmations(),
                ((Integer) JsonPath.read(block1Json, "$.numConfirmations")).longValue());
        assertEquals(
                ((Integer) JsonPath.read(block1Json, "$.numConfirmations")).longValue(),
                block2.getNumConfirmations());
    }

    @Test
    public void testGetSizeBytes() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.size")).longValue(), block1.getSizeBytes());
        assertEquals(block1.getSizeBytes(), ((Integer) JsonPath.read(block1Json, "$.sizeBytes")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.sizeBytes")).longValue(), block2.getSizeBytes());
    }

    @Test
    public void testGetHeight() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.height")).longValue(), block1.getHeight());
        assertEquals(block1.getHeight(), ((Integer) JsonPath.read(block1Json, "$.height")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.height")).longValue(), block2.getHeight());
    }

    @Test
    public void testGetVersion() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.version")).longValue(), block1.getVersion());
        assertEquals(block1.getVersion(), ((Integer) JsonPath.read(block1Json, "$.version")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.version")).longValue(), block2.getVersion());
    }

    @Test
    public void testGetMerkleRoot() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.merkleroot"), block1.getMerkleRoot().toString());
        assertEquals(block1.getMerkleRoot().toString(), JsonPath.read(block1Json, "$.merkleRoot"));
        assertEquals(JsonPath.read(block1Json, "$.merkleRoot"), block2.getMerkleRoot().toString());
    }

    @Test
    public void testGetTransactionIds() throws Exception {

        assertEquals(
                JsonPath.read(rpcGetBlockJson, "$.tx").toString(),
                standardMapper.writeValueAsString(block1.getTransactionIds()));
        assertEquals(
                standardMapper.writeValueAsString(block1.getTransactionIds()),
                JsonPath.read(block1Json, "$.transactionIds").toString());
        assertEquals(
                JsonPath.read(block1Json, "$.transactionIds").toString(),
                standardMapper.writeValueAsString(block2.getTransactionIds()));
    }

    @Test
    public void testGetCreatedTime() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.time")).longValue(), block1.getCreatedTime());
        assertEquals(block1.getCreatedTime(), ((Integer) JsonPath.read(block1Json, "$.createdTime")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.createdTime")).longValue(), block2.getCreatedTime());
    }

    @Test
    public void testGetNonce() throws Exception {
        assertEquals(((Integer) JsonPath.read(rpcGetBlockJson, "$.nonce")).longValue(), block1.getNonce());
        assertEquals(block1.getNonce(), ((Integer) JsonPath.read(block1Json, "$.nonce")).longValue());
        assertEquals(((Integer) JsonPath.read(block1Json, "$.nonce")).longValue(), block2.getNonce());
    }

    @Test
    public final void testGetDifficultyTarget() throws Exception {
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcGetBlockJson, "$.bits"), "0"),
                block1.getDifficultyTarget().toString(16));
        assertEquals(
                block1.getDifficultyTarget().toString(16),
                StringUtils.stripStart(JsonPath.read(block1Json, "$.difficultyTarget"), "0"));
        assertEquals(
                StringUtils.stripStart(JsonPath.read(block1Json, "$.difficultyTarget"), "0"),
                block2.getDifficultyTarget().toString(16));
    }

    @Test
    public final void testGetDifficulty() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetBlockJson, "$.difficulty"),
                block1.getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                block1.getDifficulty(),
                JsonPath.read(block1Json, "$.difficulty"), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(block1Json, "$.difficulty"),
                block2.getDifficulty(), ASSERT_EQUALS_PRECISION);
    }

    @Test
    public final void testGetChainwork() throws Exception {
        assertEquals(StringUtils.stripStart(JsonPath.read(rpcGetBlockJson, "$.chainwork").toString(), "0"),
                block1.getChainwork().toString(16));
        assertEquals(block1.getChainwork().toString(16), JsonPath.read(block1Json, "$.chainwork"));
        assertEquals(JsonPath.read(block1Json, "$.chainwork"), block2.getChainwork().toString(16));
    }

    @Test
    public void testGetPreviousBlockHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.previousblockhash"), block1.getPreviousBlockHash().toString());
        assertEquals(block1.getPreviousBlockHash().toString(), JsonPath.read(block1Json, "$.previousBlockHash"));
        assertEquals(JsonPath.read(block1Json, "$.previousBlockHash"), block2.getPreviousBlockHash().toString());
    }

    @Test
    public void testGetNextBlockHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetBlockJson, "$.nextblockhash"), block1.getNextBlockHash().toString());
        assertEquals(block1.getNextBlockHash().toString(), JsonPath.read(block1Json, "$.nextBlockHash"));
        assertEquals(JsonPath.read(block1Json, "$.nextBlockHash"), block2.getNextBlockHash().toString());
    }
}