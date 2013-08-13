#!/bin/bash

cd `dirname $0`
. _script-env.sh

echo ""
echo "==============================================================================="
echo "                                          Maven eclipse:eclipse 'Front' project"
echo "                                                         ======================"
mvn -e eclipse:eclipse
