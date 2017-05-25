#!/bin/bash

# credits
# https://dracoblue.net/dev/uploading-snapshots-and-releases-to-maven-central-with-travis/
# http://www.debonair.io/post/maven-cd/

if [ ! -z "$TRAVIS_TAG" ]; then
	echo "on a tag -> set pom.xml <version> to $TRAVIS_TAG"
	mvn --settings .travis/settings.xml org.codehaus.mojo:versions-maven-plugin:2.1:set -DnewVersion=$TRAVIS_TAG 1>/dev/null 2>/dev/null
else
	echo "not on a tag -> keep snapshot version in pom.xml"
fi

mvn clean deploy --settings .travis/settings.xml -DskipTests=true -P sign -B -U
