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

package org.drausin.bitflow.bitcoin.api.objects.responses;

import java.io.IOException;
import org.drausin.bitflow.bitcoin.api.objects.ImmutableBlockHeaderResponse;
import org.drausin.bitflow.bitcoin.api.objects.ImmutableBlockchainInfoResponse;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;

public final class BitcoinNodeExampleResponses {

    public static String getBlockchainInfoJsonResponse() {
        return "{\n"
                + "\"result\": {\n"
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
                + "},\n"
                + "\"error\": null,\n"
                + "\"id\": \"foo\"\n"
                + "}";
    }

    public static BlockchainInfoResponse getBlockchainInfoResponse() throws IOException {
        return BitcoinNodeMapperProvider.getMapper().readValue(getBlockchainInfoJsonResponse(),
                ImmutableBlockchainInfoResponse.class);
    }

    public static String getBlockHeaderJsonResponse() {
        return "{\n"
                + "    \"result\": {\n"
                + "         \"hash\" : \"000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39\",\n"
                + "         \"confirmations\" : 88029,\n"
                + "         \"size\" : 189,\n"
                + "         \"height\" : 227252,\n"
                + "         \"version\" : 2,\n"
                + "         \"merkleroot\" : \"c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a\",\n"
                + "         \"tx\" : [\n"
                + "             \"c738fb8e22750b6d3511ed0049a96558b0bc57046f3f77771ec825b22d6a6f4a\"\n"
                + "         ],\n"
                + "         \"time\" : 1398824312,\n"
                + "         \"nonce\" : 1883462912,\n"
                + "         \"bits\" : \"1d00ffff\",\n"
                + "         \"difficulty\" : 1.00000000,\n"
                + "         \"chainwork\" : \"000000000000000000000000000000000000000000000000083ada4a4009841a\",\n"
                + "         \"previousblockhash\" : "
                + "             \"00000000c7f4990e6ebf71ad7e21a47131dfeb22c759505b3998d7a814c011df\",\n"
                + "         \"nextblockhash\" : \"00000000afe1928529ac766f1237657819a11cfcc8ca6d67f119e868ed5b6188\"\n"
                + "     },\n"
                + "    \"error\": null,\n"
                + "    \"id\": \"foo\"\n"
                + "}";
    }

    public static BlockHeaderResponse getBlockHeaderResponse() throws IOException {
        return BitcoinNodeMapperProvider.getMapper().readValue(getBlockHeaderJsonResponse(),
                ImmutableBlockHeaderResponse.class);
    }

    public static String getErrorJsonResponse() {
        return "{\n"
                + "    \"result\": null,\n"
                + "    \"error\": {\"code\": 1, \"message\": \"test error message\"},\n"
                + "    \"id\": \"foo\"\n"
                + "}";
    }

    public static String getErrorJson() throws IOException {
        return BitcoinNodeMapperProvider.getMapper().writeValueAsString(getError());
    }

    public static BlockchainInfoResponse getBlockchainInfoErrorResponse() throws IOException {
        return BitcoinNodeMapperProvider.getMapper().readValue(getErrorJsonResponse(),
                ImmutableBlockchainInfoResponse.class);
    }

    public static BlockHeaderResponse getBlockHeaderErrorResponse() throws IOException {
        return BitcoinNodeMapperProvider.getMapper().readValue(getErrorJsonResponse(),
                ImmutableBlockHeaderResponse.class);
    }

    public static BitcoinNodeResponseError getError() throws IOException {
        return getBlockchainInfoErrorResponse().getError().get();
    }

    private BitcoinNodeExampleResponses() {}

}
