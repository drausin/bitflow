#!/bin/bash

export JAVA_HOME=${JAVA_1_8_HOME:-${JAVA_HOME}}
export GRADLE_OPTS=-Xmx1024m
GRADLE_PARALLEL="--parallel --max-workers 3"
GRADLE_LOG_LEVEL="--info --stacktrace"

# our docker machine
DOCKER_MACHINE_NAME=bitflow-docker