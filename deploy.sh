#!/bin/sh
echo "============ Release dryRun test ============"
mvn release:prepare -DdryRun=true
STATUS=$?
if [ $STATUS -eq 0 ]; then
    echo "============ Release dryRun success ============"
    mvn --batch-mode release:prepare -Dresume=false -DautoVersionSubmodules=true
    STATUS=$?
    if [ $STATUS -eq 0 ]; then
        echo "============ Release prepared success ============"
    else
        echo "============ Release prepared Failed , exec rollback phase ============"
        mvn release:rollback
    fi
else
    echo "============ Release dryRun Failed , exec clean phase ============"
    mvn release:clean
fi
