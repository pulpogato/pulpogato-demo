#!/usr/bin/env bash

ARGS=("$@")
### Use MavenCentral
./gradlew build "${ARGS[@]}"

### Use GitHub Packages
if [ "$GITHUB_ACTOR" != "" ]; then
    ./gradlew build -Prepo=github \
        -Pgpr.user=$GITHUB_ACTOR \
        -Pgpr.key=$GITHUB_TOKEN \
         "${ARGS[@]}"
fi

### Use Jitpack
./gradlew build -Prepo=jitpack \
    "${ARGS[@]}"