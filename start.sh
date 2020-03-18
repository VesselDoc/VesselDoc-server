#!/bin/bash
### BEGIN INIT INFO
# Provides:          vesseldoc-server
# Required-Start:    $all
# Required-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:
# Short-Description: VesselDoc server
### END INIT INFO

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )";
APPFILE="$(find $DIR/target/ | grep -E .jar$ | head -n 1 | xargs realpath)";

sudo java -jar $APPFILE;
