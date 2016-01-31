#!/bin/bash

GRADLE_PARALLEL="--parallel --max-workers 8"
GRADLE_LOG_LEVEL="--info --stacktrace"

# our docker machine
DOCKER_MACHINE_NAME=bitflow-docker