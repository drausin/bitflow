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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.mixin.BlockchainInfoRpcMixIn;
import org.drausin.bitflow.blockchain.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.serde.BigIntegerSerializer;
import org.drausin.bitflow.blockchain.serde.Sha256HashSerializer;
import org.junit.Before;
import org.junit.Test;

public class BlockchainInfoTest {

    private ObjectMapper rpcMapper;
    private ObjectMapper standardMapper;
    private BlockchainInfo blockchainInfo1;
    private BlockchainInfo blockchainInfo2;
    private String rpcGetInfoJson;
    private String blockchainInfo1Json;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {

        SimpleModule blockchainModule = new SimpleModule("TestModule", Version.unknownVersion());
        blockchainModule.addSerializer(Sha256Hash.class, new Sha256HashSerializer());
        blockchainModule.addSerializer(BigInteger.class, new BigIntegerSerializer());
        blockchainModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());

        // mapper from RPC json schema
        rpcMapper = new ObjectMapper();
        rpcMapper.registerModule(blockchainModule);
        rpcMapper.addMixIn(ImmutableBlockchainInfo.class, BlockchainInfoRpcMixIn.class);


        // mapper from standard json properties
        standardMapper = new ObjectMapper();
        standardMapper.registerModule(blockchainModule);

        // from https://bitcoin.org/en/developer-reference#getblockchaininfo
        rpcGetInfoJson = "{\n"
                + "    \"chain\" : \"test\",\n"
                + "    \"blocks\" : 315280,\n"
                + "    \"headers\" : 315280,\n"
                + "    \"bestblockhash\" : \"000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f\",\n"
                + "    \"difficulty\" : 1.00000000,\n"
                + "    \"verificationprogress\" : 1.00000778,\n"
                + "    \"chainwork\" : \"0000000000000000000000000000000000000000000000015e984b4fb9f9b350\"\n"
                + "}";

        blockchainInfo1 = rpcMapper.readValue(rpcGetInfoJson, ImmutableBlockchainInfo.class);
        blockchainInfo1Json = standardMapper.writeValueAsString(blockchainInfo1);
        blockchainInfo2 = standardMapper.readValue(blockchainInfo1Json, ImmutableBlockchainInfo.class);
    }

    @Test
    public final void testGetChain() throws Exception {
        assertEquals(JsonPath.read(rpcGetInfoJson, "$.chain"), blockchainInfo1.getChain());
        assertEquals(blockchainInfo1.getChain(), JsonPath.read(blockchainInfo1Json, "$.chain"));
        assertEquals(JsonPath.read(blockchainInfo1Json, "$.chain"), blockchainInfo2.getChain());
    }

    @Test
    public final void testGetNumBlocks() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetInfoJson, "$.blocks")).longValue(),
                blockchainInfo1.getNumBlocks());
        assertEquals(
                blockchainInfo1.getNumBlocks(),
                ((Integer) JsonPath.read(blockchainInfo1Json, "$.numBlocks")).longValue());
        assertEquals(
                ((Integer) JsonPath.read(blockchainInfo1Json, "$.numBlocks")).longValue(),
                blockchainInfo2.getNumBlocks());
    }

    @Test
    public final void testGetNumHeaders() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetInfoJson, "$.headers")).longValue(),
                blockchainInfo1.getNumHeaders());
        assertEquals(
                blockchainInfo1.getNumHeaders(),
                ((Integer) JsonPath.read(blockchainInfo1Json, "$.numHeaders")).longValue());
        assertEquals(
                blockchainInfo2.getNumHeaders(),
                ((Integer) JsonPath.read(blockchainInfo1Json, "$.numHeaders")).longValue());
    }

    @Test
    public final void testGetBestBlockHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetInfoJson, "$.bestblockhash"),
                blockchainInfo1.getBestBlockHash().toString());
        assertEquals(
                blockchainInfo1.getBestBlockHash().toString(),
                JsonPath.read(blockchainInfo1Json, "$.bestBlockHash"));
        assertEquals(
                JsonPath.read(blockchainInfo1Json, "$.bestBlockHash"),
                blockchainInfo2.getBestBlockHash().toString());
    }

    @Test
    public final void testGetDifficulty() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetInfoJson, "$.difficulty"),
                blockchainInfo1.getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                blockchainInfo1.getDifficulty(),
                JsonPath.read(blockchainInfo1Json, "$.difficulty"), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(blockchainInfo1Json, "$.difficulty"),
                blockchainInfo2.getDifficulty(), ASSERT_EQUALS_PRECISION);
    }

    @Test
    public final void testGetVerificationProgress() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetInfoJson, "$.verificationprogress"),
                blockchainInfo1.getVerificationProgress(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                blockchainInfo1.getVerificationProgress(),
                JsonPath.read(blockchainInfo1Json, "$.verificationProgress"), ASSERT_EQUALS_PRECISION);
        assertEquals(
                (double) JsonPath.read(blockchainInfo1Json, "$.verificationProgress"),
                blockchainInfo2.getVerificationProgress(), ASSERT_EQUALS_PRECISION);
    }


    @Test
    public final void testGetChainwork() throws Exception {
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcGetInfoJson, "$.chainwork").toString(), "0"),
                blockchainInfo1.getChainwork().toString(16));
        assertEquals(
                blockchainInfo1.getChainwork().toString(16),
                StringUtils.stripStart(JsonPath.read(blockchainInfo1Json, "$.chainwork"), "0"));
        assertEquals(
                StringUtils.stripStart(JsonPath.read(blockchainInfo1Json, "$.chainwork"), "0"),
                blockchainInfo2.getChainwork().toString(16));
    }
}
