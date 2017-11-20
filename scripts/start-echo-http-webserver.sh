#!/usr/bin/env bash

if ! [ -x "$(command -v http-echo-server)" ]; then
  echo "======================================================"
  echo "Error: http-echo-server not found."
  echo "       Make sure that it is installed: "
  echo "         npm install -g http-echo-server "
  echo "======================================================"
  exit
fi

export PORT=65000                                                                                                         Sun 21:06:10
http-echo-server