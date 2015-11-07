# based on Kyle Manna's https://hub.docker.com/r/kylemanna/bitcoind/

FROM ubuntu:14.04
MAINTAINER Drausin Wulsin <daedalus2718@gmail.com>

# directories
ENV HOME /data/bitcoin
ENV BITCOIN_CONF ${HOME}/bitcoin.conf

# (mainnet) ports
ENV CONN_PORT 8333
ENV RPC_PORT 8332

# For some reason, docker.io (0.9.1~dfsg1-2) pkg in Ubuntu 14.04 has permission
# denied issues when executing /bin/bash from trusted builds.  Building locally
# works fine (strange).  Using the upstream docker (0.11.1) pkg from
# http://get.docker.io/ubuntu works fine also and seems simpler.
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 8842ce5e && \
    echo "deb http://ppa.launchpad.net/bitcoin/bitcoin/ubuntu trusty main" > /etc/apt/sources.list.d/bitcoin.list

RUN apt-get update && \
    apt-get install -y bitcoind && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN mkdir -p ${HOME}
RUN useradd -s /bin/bash -d ${HOME} bitcoin
RUN chown bitcoin:bitcoin -R ${HOME}

USER bitcoin

VOLUME [${HOME}]

WORKDIR ${HOME}

EXPOSE ${CONN_PORT} ${RPC_PORT}

CMD bitcoind -conf=${BITCOIN_CONF} -rpcuser=${BITCOIND_RPCUSER} -rpcpassword=${BITCOIND_RPCPASSWORD}
