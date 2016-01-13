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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableSoftForkProgress.class)
@JsonDeserialize(as = ImmutableSoftForkProgress.class)
public interface SoftForkProgress {

    /**
     * Get whether the threshhold is reached.
     */
    @Value.Parameter
    @JsonProperty("status")
    boolean getIsThreshholdReached();

    /**
     * Get the number of blocks with the new version found.
     */
    @Value.Parameter
    @JsonProperty("found")
    long getNumBlocksFound();

    /**
     * Get the number of blocks required to trigger.
     */
    @Value.Parameter
    @JsonProperty("required")
    long getNumBlocksRequired();

    /**
     * Get the maximum size of the examined window of recent blocks.
     */
    @Value.Parameter
    @JsonProperty("window")
    long maxWindowSize();
}
