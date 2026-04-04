#!/usr/bin/env sh

MVNW_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
MVNW_REPO="$MVNW_DIR/.mvn/wrapper"
WRAPPER_JAR="$MVNW_REPO/maven-wrapper.jar"
PROPS_FILE="$MVNW_REPO/maven-wrapper.properties"

if [ ! -f "$PROPS_FILE" ]; then
  echo "Maven wrapper properties not found: $PROPS_FILE" >&2
  exit 1
fi

WRAPPER_URL=$(grep -E '^wrapperUrl=' "$PROPS_FILE" | sed 's/^wrapperUrl=//')
if [ -z "$WRAPPER_URL" ]; then
  echo "wrapperUrl not found in $PROPS_FILE" >&2
  exit 1
fi

if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p "$MVNW_REPO" || exit 1
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR" || exit 1
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR" || exit 1
  else
    echo "Neither curl nor wget found; cannot download Maven Wrapper." >&2
    exit 1
  fi
fi

exec java ${MAVEN_OPTS:-} -Dmaven.multiModuleProjectDirectory="$MVNW_DIR" -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"

