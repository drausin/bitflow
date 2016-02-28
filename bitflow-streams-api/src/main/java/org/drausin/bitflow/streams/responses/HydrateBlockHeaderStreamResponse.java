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

package org.drausin.bitflow.streams.responses;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.SAME, strictBuilder = true)
@JsonSerialize(as = ImmutableHydrateBlockHeaderStreamResponse.class)
@JsonDeserialize(as = ImmutableHydrateBlockHeaderStreamResponse.class)
public abstract class HydrateBlockHeaderStreamResponse {

    @Value.Parameter
    public abstract String getStreamName();

    @Value.Parameter
    public abstract long getNumHydrated();

    public static HydrateBlockHeaderStreamResponse of(String streamName, long numHydrated) {
        return ImmutableHydrateBlockHeaderStreamResponse.of(streamName, numHydrated);
    }
}
