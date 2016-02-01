# Running a bitcoin node

## Monitoring

Examine the `bitcoin-node.bitflow.bfl` container filesystem via 

    docker run --volumes-from=bitcoin-node.bitflow.bfl --rm daedalus2718/bitflow-bitcoin-node:latest ls -lh /data/services/bitflow-bitcoin-node/
    docker run --volumes-from=bitcoin-node.bitflow.bfl --rm daedalus2718/bitflow-bitcoin-node:latest ls -lh /data/services/bitflow-bitcoin-node/blocks

and the tail of the log via

    docker run --volumes-from=bitcoin-node.bitflow.bfl --rm daedalus2718/bitflow-bitcoin-node:latest tail -25 /data/bitcoin/debug.log

or
    
    docker run --volumes-from=bitcoin-node.bitflow.bfl --rm daedalus2718/bitflow-bitcoin-node:latest tail -f /data/services/bitflow-bitcoin-node/debug.log

Sometimes this `tail -f` is hard to kill with Cmnd+C, so you may have to kill it via `docker kill`.


