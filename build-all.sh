#!/usr/bin/env bash

### Use MavenCentral
./gradlew build

### Use GitHub Packages
if [ "$GITHUB_USER" != "" ]; then
    ./gradlew build -Prepo=github \
        -Pgpr.user=$GITHUB_USER \
        -Pgpr.key=$GITHUB_PAT
fi

### Use Jitpack
./gradlew build -Prepo=jitpack \
    -PpulpogatoVersion=v3.2.0
