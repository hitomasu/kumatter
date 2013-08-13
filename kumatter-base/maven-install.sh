#!/bin/bash

cd `dirname $0`
. _script-env.sh

echo ""
echo "==============================================================================="
echo "                                                         Install 'Base' project"
echo "                                                         ======================"
mvn -e install
