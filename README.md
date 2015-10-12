# bitcoin-flow
Bitcoin transactions data pipeline

#### v0 requirements
- maintain historical record of all transactions that can be easily reprocessed 
- aggregates materialized in an easily-queryable place (Postgres, Kafka, ??)
- transaction listener should run contuously and push/stream data through downstream DAG
- pre-push test coverage and code quality hooks
- pipeline monitoring dashboard

#### initial guess of technologies to use
- BitcoinJ : interacting with Bitcoin world
- Docker : containerization
- Kafka : initial processing of transactions
- Storm : aggregation of transactions
- Postgres : general metadata/logging
- jOOQ : interacting w/ DBs
- Jenkins : build infra
- (Palantir Java code quality tool)
- ipython notebook : monitoring "dashboard"

#### other technologies to perhaps use later
- VoltDB 
- Vertica
- Cassandra (+ AtlasDB?)
- Spark (streaming)
- Spark jobserver
- Puppet
- Nagios
- tcollector
- D3