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

package org.drausin.bitflow.bitcoin;

import com.fasterxml.jackson.databind.JsonNode;
import io.dropwizard.Application;
import io.dropwizard.cli.CheckCommand;
import io.dropwizard.cli.Cli;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.JarLocation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.drausin.bitflow.bitcoin.commands.BitcoinNodeServerCommand;
import org.drausin.bitflow.bitcoin.config.BitcoindExecutableConfig;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server for external bitcoin node (i.e., bitcoind) process.
 */
@SuppressWarnings("checkstyle:designforextension")
public class BitcoinNodeServer extends Application<ServerConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinNodeServerCommand.class);

    public static void main(String[] args) throws Exception {
        new BitcoinNodeServer().run(args);
    }

    @Override
    public final void run(String... arguments) throws Exception {
        final Bootstrap<ServerConfig> bootstrap = new Bootstrap<>(this);
        bootstrap.addCommand(new BitcoinNodeServerCommand<>(this));
        bootstrap.addCommand(new CheckCommand<>(this));
        initialize(bootstrap);

        final Cli cli = getCli(bootstrap);
        if (!cli.run(arguments)) {
            // only exit if there's an error running the command
            throw new IllegalArgumentException(String.format("error running command with args %s",
                    Arrays.toString(arguments)));
        }
    }

    public final void run(ServerConfig config, Environment env) throws Exception {
        createDataDirectory(config);
        writeBitcoindConf(config, env);
    }

    protected Cli getCli(Bootstrap<ServerConfig> bootstrap) {
        return new Cli(new JarLocation(getClass()), bootstrap, System.out, System.err);
    }

    protected boolean createDataDirectory(ServerConfig config) throws IOException {
        File dataDirectory = getDataDirectory(config);
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw new IOException(String.format("unable to create directory %s", dataDirectory.getAbsolutePath()));
            }
        }
        return true;
    }

    protected File getDataDirectory(ServerConfig config) {
        return new File(config.getBitcoinNode().getDataDirectory());
    }

    private static void writeBitcoindConf(ServerConfig config, Environment env) throws IOException {
        BitcoindExecutableConfig bitcoindExecutableConfig = config.getBitcoinNode();
        PrintWriter writer = new PrintWriter(bitcoindExecutableConfig.getConfigFilePath(), "UTF-8");
        Iterator<Map.Entry<String, JsonNode>> configFields = env.getObjectMapper().valueToTree(
                bitcoindExecutableConfig).fields();

        // print each field to the *.conf file for bitcoind process
        while (configFields.hasNext()) {
            Map.Entry<String, JsonNode> field = configFields.next();
            String value = bitcoindExecutableConfig.getConfFileValue(field.getValue(), config.getMode());
            writer.write(String.format("%s=%s%n", field.getKey(), value));
        }
        writer.close();
    }


}
