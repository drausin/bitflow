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

package org.drausin.bitflow.streams;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

public abstract class BitflowStream<T> {

    /**
     * Get the stream name.
     */
    @Value.Parameter
    @JsonProperty("name")
    public abstract String getName();

    @Value.Parameter
    @JsonProperty("latest")
    public abstract T getLatest();
}
