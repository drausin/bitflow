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

package org.drausin.bitflow.blockchain.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Deserialize BigIntegers from their hex string values
 *
 * Created by dwulsin on 12/3/15.
 */
public class BigIntegerDeserializer extends JsonDeserializer<BigInteger> {
    @Override
    public final BigInteger deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return new BigInteger(parser.getValueAsString(), 16);
    }

    @Override
    public final Class<BigInteger> handledType() {
        return BigInteger.class;
    }
}
