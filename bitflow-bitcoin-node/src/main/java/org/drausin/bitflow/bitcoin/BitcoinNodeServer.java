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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.dropwizard.Application;
import io.dropwizard.cli.CheckCommand;
import io.dropwizard.cli.Cli;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.JarLocation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import org.drausin.bitflow.bitcoin.commands.BitcoinNodeServerCommand;
import org.drausin.bitflow.bitcoin.config.BitcoinNodeConfig;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server for external bitcoin node (i.e., bitcoind) process.
 */
public class BitcoinNodeServer extends Application<ServerConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinNodeServerCommand.class);

    public static void main(String[] args) throws Exception {
        new BitcoinNodeServer().run(args);
    }

    @Override
    @SuppressFBWarnings(value = "DM_EXIT")
    public final void run(String... arguments) throws Exception {
        final Bootstrap<ServerConfig> bootstrap = new Bootstrap<>(this);
        bootstrap.addCommand(new BitcoinNodeServerCommand<>(this));
        bootstrap.addCommand(new CheckCommand<>(this));
        initialize(bootstrap);

        final Cli cli = new Cli(new JarLocation(getClass()), bootstrap, System.out, System.err);
        if (!cli.run(arguments)) {
            // only exit if there's an error running the command
            System.exit(1);
        }
    }

    public final void run(ServerConfig config, Environment env) throws Exception {
        createDataDirectory(config);
        writeBitcoindConf(config, env);
    }

    private static void createDataDirectory(ServerConfig config) throws IOException {
        File dataDirectory = new File(config.getBitcoinNode().getDataDirectory());
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw new IOException(String.format("unable to create directory %s", dataDirectory.getAbsolutePath()));
            }
        }

    }

    private static void writeBitcoindConf(ServerConfig config, Environment env) throws IOException {
        BitcoinNodeConfig bitcoinNodeConfig = config.getBitcoinNode();
        PrintWriter writer = new PrintWriter(bitcoinNodeConfig.getConfigFilePath(), "UTF-8");
        Iterator<Map.Entry<String, JsonNode>> configFields = env.getObjectMapper().valueToTree(
                bitcoinNodeConfig).fields();

        // print each field to the *.conf file for bitcoind process
        while (configFields.hasNext()) {
            Map.Entry<String, JsonNode> field = configFields.next();
            String value = bitcoinNodeConfig.getConfFileValue(field.getValue(), config.getMode());
            writer.write(String.format("%s=%s%n", field.getKey(), value));
        }
        writer.close();
    }


}
