#!/bin/bash

export JAVA_HOME=${JAVA_1_8_HOME:-${JAVA_HOME}}

GRADLE_PARALLEL="--parallel --max-workers 8"
GRADLE_LOG_LEVEL="--info --stacktrace"

# our docker machine
DOCKER_MACHINE_NAME=bitflow-docker