FROM daedalus2718/bitflow-base
MAINTAINER Drausin Wulsin <daedalus2718@gmail.com>

# handy vars
ENV BINARIES_DIR /opt/bitflow/binaries
ENV SERVICES_DIR /opt/bitflow/services
ENV SERVICE_NAME bitflow-blockchain

# filesystem
RUN mkdir -p ${BINARIES_DIR} && \
    mkdir -p ${SERVICES_DIR}

# install
ADD ${SERVICE_NAME}-*.tgz ${BINARIES_DIR}
RUN mkdir -p ${SERVICES_DIR}/${SERVICE_NAME} && \
    ln -s ${BINARIES_DIR}/${SERVICE_NAME}-*/service ${SERVICES_DIR}/${SERVICE_NAME} && \
    cp -r ${BINARIES_DIR}/${SERVICE_NAME}-*/var ${SERVICES_DIR}/${SERVICE_NAME}/var && \
    chown -R bitflow:bitflow /opt/bitflow

# externals
EXPOSE 8100 \
       8101

# run
USER bitflow
WORKDIR ${SERVICES_DIR}/${SERVICE_NAME}
CMD service/bin/${SERVICE_NAME} server var/conf/${SERVICE_NAME}.yml