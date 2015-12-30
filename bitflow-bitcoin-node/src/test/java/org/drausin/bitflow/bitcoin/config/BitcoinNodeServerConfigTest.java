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

package org.drausin.bitflow.bitcoin.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BitcoinNodeServerConfigTest {

    private ServerConfig config;
    private String uri;
    private String rpcUser;
    private String rpcPassword;

    @Before
    public final void setUp() throws Exception {

        uri = "http://localhost:0000";
        rpcUser = "auser";
        rpcPassword = "apassword";

        config = new ServerConfig(uri, rpcUser, rpcPassword);
    }

    @Test
    public final void testGetUri() throws Exception {
        assertEquals(uri, config.getUri());
    }

    @Test
    public final void testGetRpcUser() throws Exception {
        assertEquals(rpcUser, config.getRpcUser());
    }

    @Test
    public final void testGetRpcPassword() throws Exception {
        assertEquals(rpcPassword, config.getRpcPassword());
    }

    @Test
    public final void testEquals() throws Exception {
        assertThat(config, is(new ServerConfig(uri, rpcUser, rpcPassword)));
    }

    @Test
    public final void testHashCode() throws Exception {
        assertEquals((new ServerConfig(uri, rpcUser, rpcPassword)).hashCode(), config.hashCode());
    }
}
