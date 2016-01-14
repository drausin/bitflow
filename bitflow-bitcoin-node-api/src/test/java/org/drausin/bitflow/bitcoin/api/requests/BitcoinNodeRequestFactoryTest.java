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

package org.drausin.bitflow.bitcoin.api.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bitcoinj.core.Sha256Hash;
import org.junit.Test;

public final class BitcoinNodeRequestFactoryTest {

    private static String id = "foo";
    private static Sha256Hash headerHash =
            Sha256Hash.wrap("000000000fe549a89848c76070d4132872cfb6efe5315d01d7ef77e4900f2d39");

    @Test
    public void testCreateBlockchainInfoRequest() throws Exception {
        BitcoinNodeRequest request = BitcoinNodeRequestFactory.createBlockchainInfoRequest();
        assertFalse(request.getMethod().isEmpty());
        assertTrue(request.getParams().isEmpty());
        assertFalse(request.getId().isPresent());
    }

    @Test
    public void testCreateBlockchainInfoRequestWithId() throws Exception {
        BitcoinNodeRequest request = BitcoinNodeRequestFactory.createBlockchainInfoRequest(id);
        assertFalse(request.getMethod().isEmpty());
        assertTrue(request.getParams().isEmpty());
        assertTrue(request.getId().isPresent());
        assertEquals(id, request.getId().get());
    }

    @Test
    public void testCreateBlockHeaderRequest() throws Exception {
        BitcoinNodeRequest request = BitcoinNodeRequestFactory.createBlockHeaderRequest(headerHash);
        assertFalse(request.getMethod().isEmpty());
        assertTrue(request.getParams().contains(headerHash));
        assertFalse(request.getId().isPresent());
    }

    @Test
    public void testCreateBlockHeaderRequestWithId() throws Exception {
        BitcoinNodeRequest request = BitcoinNodeRequestFactory.createBlockHeaderRequest(headerHash, id);
        assertFalse(request.getMethod().isEmpty());
        assertTrue(request.getParams().contains(headerHash));
        assertTrue(request.getId().isPresent());
        assertEquals(id, request.getId().get());
    }
}
