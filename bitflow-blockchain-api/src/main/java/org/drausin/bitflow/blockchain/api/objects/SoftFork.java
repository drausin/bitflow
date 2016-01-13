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
@JsonSerialize(as = ImmutableSoftFork.class)
@JsonDeserialize(as = ImmutableSoftFork.class)
public interface SoftFork {

    /**
     * Get the name of the softfork.
     */
    @Value.Parameter
    @JsonProperty("id")
    String getId();

    /**
     * Get the block version.
     */
    @Value.Parameter
    @JsonProperty("version")
    long getVersion();

    /**
     * Get the progress toward enforcing the softfork rules for new-version blocks.
     */
    @Value.Parameter
    @JsonProperty("enforce")
    SoftForkProgress getEnforcementProgress();

    /**
     * Get the progress toward rejecting pre-softfork blocks.
     */
    @Value.Parameter
    @JsonProperty("reject")
    SoftForkProgress getRejectionProgress();
}
