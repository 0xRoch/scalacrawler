#!/bin/sh

sbt='sbt-launcher.jar'
sbt_get='http://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.12.2/sbt-launch.jar'

if [ ! -f $sbt ]
then
    wget -O $sbt $sbt_get
fi

java -jar $sbt
