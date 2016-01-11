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

package org.drausin.bitflow.bitcoin.commands;

import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a bitcoin node via the bitcoind executable.
 */
public class BitcoinNodeServerCommand<T extends ServerConfig> extends EnvironmentCommand<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinNodeServerCommand.class);

    private final Class<T> configurationClass;

    public BitcoinNodeServerCommand(Application<T> application) {
        super(application, "server", "Runs the bitcoin node server via bitcoind executable");
        this.configurationClass = application.getConfigurationClass();
    }

    @Override
    protected final Class<T> getConfigurationClass() {
        return configurationClass;
    }

    @Override
    protected final void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        final BitcoindExecutable bitcoind = new BitcoindExecutable(configuration.getBitcoinNode().getConfigFile());
        try {
            bitcoind.run();
            cleanupAsynchronously();
        } catch (Exception e) {
            LOGGER.error("Unable to start server, shutting down", e);
            cleanup();
            throw e;
        }
    }

}
