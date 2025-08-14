#!/bin/sh
# Delegates Gradle wrapper calls to the android_frontend module.
# This allows CI systems that run from the workspace root to still build the Android app.

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
cd "$SCRIPT_DIR/android_frontend" || exit 1
exec ./gradlew "$@"
