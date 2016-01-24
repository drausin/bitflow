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

package org.drausin.bitflow.blockchain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.joda.time.DateTime;

public final class SubchainValidator {

    public static void validateSubchain(List<BlockHeader> subchain, long numBlocks) {
        validateSubchain(subchain);
        assertEquals(numBlocks, subchain.size());
    }

    public static void validateSubchain(List<BlockHeader> subchain, DateTime from, DateTime to) {
        validateSubchain(subchain);
        assertFalse(subchain.get(0).getCreatedDateTime().isAfter(from.toInstant()));
        assertFalse(subchain.get(subchain.size() - 1).getCreatedDateTime().isBefore(to.toInstant()));
        if (subchain.size() >= 2) {
            assertTrue(subchain.get(1).getCreatedDateTime().isAfter(from.toInstant()));
            assertTrue(subchain.get(subchain.size() - 2).getCreatedDateTime().isBefore(to.toInstant()));
        }
    }

    public static void validateSubchain(List<BlockHeader> subchain) {
        for (int c = 1; c < subchain.size(); c++) {
            assertEquals(subchain.get(c).getHeaderHash(), subchain.get(c - 1).getNextBlockHash().get());
        }
    }

    private SubchainValidator() {}
}
