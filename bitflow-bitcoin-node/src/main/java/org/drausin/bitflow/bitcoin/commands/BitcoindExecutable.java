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

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for bitcoin node (i.e., bitcoind) executable.
 */
public class BitcoindExecutable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoindExecutable.class);

    private static final List<String> bitcoindLocations = Arrays.asList("/usr/bin/bitcoind",
            System.getenv("BITCOIND_LOCATION"));

    private final File bitcoindConfigurationFile;

    public BitcoindExecutable(File bitcoindConfigurationFile) {
        this.bitcoindConfigurationFile = bitcoindConfigurationFile;
    }

    public final void run() throws IOException, InterruptedException {
        executeBitcoindCommand();
    }

    private void executeBitcoindCommand(String... commands) throws IOException,
            InterruptedException {
        List<String> args = Lists.newArrayList(getBitcoindPath(),
                String.format("-conf=%s", bitcoindConfigurationFile.getAbsolutePath()));
        for (String command : commands) {
            args.add(command);
        }
        LOGGER.info(String.format("running process: %s", args.toString()));
        Process bitcoind = new ProcessBuilder().command(args)
                .redirectErrorStream(true)
                .start();
        bitcoind.waitFor();
    }

    private String getBitcoindPath() {
        return bitcoindLocations.stream()
                .filter(StringUtils::isNotBlank)
                .filter(path -> new File(path).exists())
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find bitcoind in: " + bitcoindLocations));
    }

}
