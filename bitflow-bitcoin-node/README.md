# Running a bitcoin node

We run the bitcoin nodes in Docker containers.

## Setup

When running on a Mac, ensure your current VM is running via `docker-machine ls` and that your session has the requisite docker machine environment variables (assuming you're using a VM named `default`)

        eval "$(docker-machine env default)"

For obvious reasons, we separate the sensitive configurations (passwords, etc) from the others and store them in `conf/bitcoin.secure.properties`, but we don't want to commit that file, so we encrypt it with a defined password and commit the encrypted version. When encrypting/decrypting, you will need to set the password environment variable via something like

        export BITCOIN_FLOW_PASSWORD=XXXXXXXX

Decrypt the secure properties via

        make decrypt_properties

If you change the secure properties and want to commit them,

        make encrypt_properties

will overwrite the `bitcoin.secure.properties.cast5` file that you can commit.

When you the Dockerfile, you will want to re-build it via

        ./gradlew bitflow-bitcoind-node:docker

You can publish this image to DockerHub via 

        ./gradlew bitflow-bitcoind-node:dockerPush

## Run

We define two different node configurations, pruned and full. A pruned node just retains recent blocks and transactions and is mostly useful for debugging/perhaps some local development (since it doesn't take very much space, usually about ~1GB). A full node keeps all the transactions of the block chain (~50GB as of November 2015).

From the root `bitcoind` directory, run a pruned node via 

        ./bin/run-node pruned

or a full node

        ./bin/run-node full

To stop a node, use the `bitcoin-cli` instead of just killing the process, since killing can result in corrupted blockchain files. First ensure you have the properties loaded (assuming you're in the `bitcoind` root directory)

        source conf/bitcoind.properties

Stop the node via

        docker exec bitcoind-node bitcoin-cli -rpcuser=${RPCUSER} -rpcpassword=${RPCPASSWORD} stop

## Monitor

See which Docker containers are running via 
    
    docker ps

and all them (e.g., the bitcoin-data container) with the additional `-a` flag

    docker ps -a

Examine the node data volume via 

    docker run --volumes-from=bitcoind-data --rm "${BITCOIND_IMAGE}" ls -lh /data/bitcoin/
    docker run --volumes-from=bitcoind-data --rm "${BITCOIND_IMAGE}" ls -lh /data/bitcoin/blocks/

and the tail of the log via

    docker run --volumes-from=bitcoind-data --rm "${BITCOIND_IMAGE}" tail -25 /data/bitcoin/debug.log

or
    
    docker run --volumes-from=bitcoind-data --rm "${BITCOIND_IMAGE}" tail -f /data/bitcoin/debug.log

Sometimes this `tail -f` is hard to kill with Cmnd+C, so you may have to kill it via `docker kill`.


