#!/bin/bash
while [ true ]
do
  hadoop fs -rm /user/cloudera/twitter.txt
  hadoop jar TwitterEinlesen.jar 29
  sleep 3
  hadoop jar MapReduceTwitter.jar
  sleep 3
done
