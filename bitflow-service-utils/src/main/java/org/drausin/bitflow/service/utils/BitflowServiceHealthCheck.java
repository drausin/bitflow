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

package org.drausin.bitflow.service.utils;

import com.codahale.metrics.health.HealthCheck;

public class BitflowServiceHealthCheck extends HealthCheck {

    private final BitflowService service;

    public BitflowServiceHealthCheck(BitflowService service) {
        this.service = service;
    }

    @Override
    protected final Result check() throws Exception {
        String pingResult = service.ping();
        if (pingResult.equals("pong")) {
            return Result.healthy();
        }
        return Result.unhealthy(String.format("unknown ping result '%s'", pingResult));
    }
}
