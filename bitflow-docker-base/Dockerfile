FROM ubuntu:14.04
MAINTAINER Drausin Wulsin <daedalus2718@gmail.com>

# For some reason, docker.io (0.9.1~dfsg1-2) pkg in Ubuntu 14.04 has permission
# denied issues when executing /bin/bash from trusted builds.  Building locally
# works fine (strange).  Using the upstream docker (0.11.1) pkg from
# http://get.docker.io/ubuntu works fine also and seems simpler.
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 8842ce5e && \
    echo "deb http://ppa.launchpad.net/bitcoin/bitcoin/ubuntu trusty main" > /etc/apt/sources.list.d/bitcoin.list

RUN apt-get install -y software-properties-common && \
    add-apt-repository -y ppa:webupd8team/java && \
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections

RUN apt-get update && \
    apt-get install -y curl && \
    apt-get install -y bitcoind && \
    apt-get install -y oracle-java8-installer && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /var/cache/*

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# filesystem
RUN mkdir -p /home/bitflow
RUN mkdir -p /opt/bitflow
RUN mkdir -p /data

# user
RUN useradd -s /bin/bash bitflow
RUN chown bitflow:bitflow -R /home/bitflow
RUN chown bitflow:bitflow -R /opt/bitflow
RUN chown bitflow:bitflow -R /data

# no-op since this is just a base image
CMD ["true"]
