#!/usr/bin/env bash

for i in `seq 1 $1`; do

    id=`uuidgen`
    echo "{'a-very-cool-piece-of-data': 'data # $id-$i'}" | http POST http://localhost:65000/rest/$id

    echo -n "."

done

echo "Done!"
