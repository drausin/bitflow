#!/bin/bash

if [ -n "${JAVA_1_8_HOME}" ]; then export JAVA_HOME=$JAVA_1_8_HOME; fi
GRADLE_PARALLEL="--parallel --max-workers 8"
GRADLE_LOG_LEVEL="--info --stacktrace --profile"
