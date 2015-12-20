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

package org.drausin.bitflow.blockchain.client.objects;

public final class BitcoindRpcJsonResponses {

    public static final String BLOCKCHAIN_INFO_RESPONSE = "{\n"
            + "    \"result\": {\n"
            + "         \"chain\" : \"test\",\n"
            + "         \"blocks\" : 315280,\n"
            + "         \"headers\" : 315280,\n"
            + "         \"bestblockhash\" : \"000000000ebb17fb455e897b8f3e343eea1b07d926476d00bc66e2c0342ed50f\",\n"
            + "         \"difficulty\" : 1.00000000,\n"
            + "         \"verificationprogress\" : 1.00000778,\n"
            + "         \"chainwork\" : \"0000000000000000000000000000000000000000000000015e984b4fb9f9b350\"\n"
            + "    },\n"
            + "    \"error\": null,\n"
            + "    \"id\": \"foo\"\n"
            + "}";

    public static final String BLOCK_HEADER_RESPONSE = "{\n"
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

    public static final String ERROR_RESPONSE = "{\n"
            + "    \"result\": null,\n"
            + "    \"error\": {\"code\": 1, \"message\": \"test error message\"},\n"
            + "    \"id\": \"foo\"\n"
            + "}";


}
