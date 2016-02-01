[![Circle CI](https://circleci.com/gh/drausin/bitflow/tree/develop.svg?style=svg)](https://circleci.com/gh/drausin/bitflow/tree/develop)
[![Coverage Status](https://coveralls.io/repos/github/drausin/bitflow/badge.svg?branch=develop)](https://coveralls.io/github/drausin/bitflow?branch=develop)

# bitcoin-flow
Bitcoin transactions data pipeline for working with transactions and aggregates of them. The ultimate goals is to have a 
streaming pipeline that handles transactions and updates aggregates on a sub-second timeframe.

### services
- bitflow-bitcoin-node: a wrapper around the actual `bitcoind` executable, which we interact with via the RPC interface
- bitflow-blockchain: interface for blockchain data like specfic blocks, transactions for a particular block, 
block subchains, or general info about the current blockchain state
 
### testing
In addition to our unit tests, we use Docker to manage containers for each service and run integration tests between 
them.

The `integrationTests` gradle task handles bringing up the Docker containers and running the tests for each service.  
