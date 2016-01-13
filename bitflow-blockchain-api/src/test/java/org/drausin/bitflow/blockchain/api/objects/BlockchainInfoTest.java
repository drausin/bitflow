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
import org.drausin.bitflow.blockchain.api.serde.BigIntegerDeserializer;
import org.drausin.bitflow.blockchain.api.serde.BigIntegerSerializer;
import org.drausin.bitflow.blockchain.api.serde.Sha256HashSerializer;
import org.junit.Before;
import org.junit.Test;

public class BlockchainInfoTest {

    private ObjectMapper mapper;
    private BlockchainInfo blockchainInfo;
    private String rpcGetInfoJson;
    private String blockchainInfoJson;
    private static final double ASSERT_EQUALS_PRECISION = 1E-9;

    @Before
    public final void setUp() throws Exception {

        SimpleModule blockchainModule = new SimpleModule("TestModule", Version.unknownVersion());
        blockchainModule.addSerializer(Sha256Hash.class, new Sha256HashSerializer());
        blockchainModule.addSerializer(BigInteger.class, new BigIntegerSerializer());
        blockchainModule.addDeserializer(BigInteger.class, new BigIntegerDeserializer());

        // mapper from RPC json schema
        mapper = new ObjectMapper()
                .registerModule(blockchainModule)
                .registerModule(new GuavaModule());

        // from https://bitcoin.org/en/developer-reference#getblockchaininfo
        rpcGetInfoJson = "{\n"
                + "    \"chain\" : \"main\",\n"
                + "    \"blocks\" : 230815,\n"
                + "    \"headers\" : 392851,\n"
                + "    \"bestblockhash\" : \"0000000000000022df2e6aa2a391b09c13fcf26ad4d4de7ff89ff6f3d8b7e76e\",\n"
                + "    \"difficulty\" : 7672999.92016414,\n"
                + "    \"verificationprogress\" : 0.06962276,\n"
                + "    \"chainwork\" : \"000000000000000000000000000000000000000000000035d5077fe0b77b6820\",\n"
                + "    \"pruned\" : true,\n"
                + "    \"softforks\" : [\n"
                + "        {\n"
                + "            \"id\" : \"bip34\",\n"
                + "            \"version\" : 2,\n"
                + "            \"enforce\" : {\n"
                + "                \"status\" : true,\n"
                + "                \"found\" : 1000,\n"
                + "                \"required\" : 750,\n"
                + "                \"window\" : 1000\n"
                + "            },\n"
                + "            \"reject\" : {\n"
                + "                \"status\" : true,\n"
                + "                \"found\" : 1000,\n"
                + "                \"required\" : 950,\n"
                + "                \"window\" : 1000\n"
                + "            }\n"
                + "        },\n"
                + "        {\n"
                + "            \"id\" : \"bip66\",\n"
                + "            \"version\" : 3,\n"
                + "            \"enforce\" : {\n"
                + "                \"status\" : false,\n"
                + "                \"found\" : 0,\n"
                + "                \"required\" : 750,\n"
                + "                \"window\" : 1000\n"
                + "            },\n"
                + "            \"reject\" : {\n"
                + "                \"status\" : false,\n"
                + "                \"found\" : 0,\n"
                + "                \"required\" : 950,\n"
                + "                \"window\" : 1000\n"
                + "            }\n"
                + "        },\n"
                + "        {\n"
                + "            \"id\" : \"bip65\",\n"
                + "            \"version\" : 4,\n"
                + "            \"enforce\" : {\n"
                + "                \"status\" : false,\n"
                + "                \"found\" : 0,\n"
                + "                \"required\" : 750,\n"
                + "                \"window\" : 1000\n"
                + "            },\n"
                + "            \"reject\" : {\n"
                + "                \"status\" : false,\n"
                + "                \"found\" : 0,\n"
                + "                \"required\" : 950,\n"
                + "                \"window\" : 1000\n"
                + "            }\n"
                + "        }\n"
                + "    ],\n"
                + "    \"pruneheight\" : 228183\n"
                + "}";

        blockchainInfo = mapper.readValue(rpcGetInfoJson, ImmutableBlockchainInfo.class);
        blockchainInfoJson = mapper.writeValueAsString(blockchainInfo);
    }

    @Test
    public final void testGetChain() throws Exception {
        assertEquals(JsonPath.read(rpcGetInfoJson, "$.chain"), blockchainInfo.getChain());
        assertEquals(blockchainInfo.getChain(), JsonPath.read(blockchainInfoJson, "$.chain"));
    }

    @Test
    public final void testGetNumBlocks() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetInfoJson, "$.blocks")).longValue(),
                blockchainInfo.getNumBlocks());
        assertEquals(
                blockchainInfo.getNumBlocks(),
                ((Integer) JsonPath.read(blockchainInfoJson, "$.blocks")).longValue());
    }

    @Test
    public final void testGetNumHeaders() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetInfoJson, "$.headers")).longValue(),
                blockchainInfo.getNumHeaders());
        assertEquals(
                blockchainInfo.getNumHeaders(),
                ((Integer) JsonPath.read(blockchainInfoJson, "$.headers")).longValue());
    }

    @Test
    public final void testGetBestBlockHash() throws Exception {
        assertEquals(JsonPath.read(rpcGetInfoJson, "$.bestblockhash"),
                blockchainInfo.getBestBlockHash().toString());
        assertEquals(
                blockchainInfo.getBestBlockHash().toString(),
                JsonPath.read(blockchainInfoJson, "$.bestblockhash"));
    }

    @Test
    public final void testGetDifficulty() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetInfoJson, "$.difficulty"),
                blockchainInfo.getDifficulty(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                blockchainInfo.getDifficulty(),
                JsonPath.read(blockchainInfoJson, "$.difficulty"), ASSERT_EQUALS_PRECISION);
    }

    @Test
    public final void testGetVerificationProgress() throws Exception {
        assertEquals(
                (double) JsonPath.read(rpcGetInfoJson, "$.verificationprogress"),
                blockchainInfo.getVerificationProgress(), ASSERT_EQUALS_PRECISION);
        assertEquals(
                blockchainInfo.getVerificationProgress(),
                JsonPath.read(blockchainInfoJson, "$.verificationprogress"), ASSERT_EQUALS_PRECISION);
    }


    @Test
    public final void testGetChainwork() throws Exception {
        assertEquals(
                StringUtils.stripStart(JsonPath.read(rpcGetInfoJson, "$.chainwork").toString(), "0"),
                blockchainInfo.getChainwork().toString(16));
        assertEquals(
                blockchainInfo.getChainwork().toString(16),
                StringUtils.stripStart(JsonPath.read(blockchainInfoJson, "$.chainwork"), "0"));
    }

    @Test
    public final void testGetPruned() throws Exception {
        assertEquals(JsonPath.read(rpcGetInfoJson, "$.pruned"), blockchainInfo.getPruned());
        assertEquals(blockchainInfo.getPruned(), JsonPath.read(blockchainInfoJson, "$.pruned"));
    }

    @Test
    public final void testGetPruneHeight() throws Exception {
        assertEquals(
                ((Integer) JsonPath.read(rpcGetInfoJson, "$.pruneheight")).longValue(),
                blockchainInfo.getPruneHeight());
        assertEquals(
                blockchainInfo.getPruneHeight(),
                ((Integer) JsonPath.read(blockchainInfoJson, "$.pruneheight")).longValue());
    }

    @Test
    public final void testGetSoftForks() throws Exception {
        assertEquals(
                JsonPath.read(rpcGetInfoJson, "$.softforks").toString(),
                mapper.writeValueAsString(blockchainInfo.getSoftForks()));
        assertEquals(
                mapper.writeValueAsString(blockchainInfo.getSoftForks()),
                JsonPath.read(blockchainInfoJson, "$.softforks").toString());
    }

    @Test
    public final void testOf() {
        BlockchainInfo testBlockchainInfo = BlockchainInfo.of(blockchainInfo.getChain(), blockchainInfo.getNumBlocks(),
                blockchainInfo.getNumHeaders(), blockchainInfo.getBestBlockHash(), blockchainInfo.getDifficulty(),
                blockchainInfo.getVerificationProgress(), blockchainInfo.getChainwork(), blockchainInfo.getPruned(),
                blockchainInfo.getPruneHeight(), blockchainInfo.getSoftForks());
        assertEquals(blockchainInfo, testBlockchainInfo);
    }
}
