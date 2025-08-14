#!/bin/bash
cd /home/kavia/workspace/code-generation/simple-notes-app-159655-159674/android_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

