#!/usr/bin/env bash
echo "Running lein codox"
lein with-profile codox codox

PROJECT_VERSION=$(script/project-version)
VERSION_PATH="docs/$PROJECT_VERSION"
echo $VERSION_PATH
INDEX_HTML="docs/index.html"

echo "Moving docs into $VERSION_PATH"
if [ -d $VERSION_PATH ]; then
    rm -r $VERSION_PATH
else
    echo "Not found"
fi
mv "docs/new" $VERSION_PATH

echo "Generating new index.html"
if [ -f $INDEX_HTML ]; then
    rm "docs/index.html"
fi
cat script/template-index.html | sed "s/<current-version-number>/$PROJECT_VERSION/" > docs/index.html
