#!/bin/sh
sudo yum -y update

sudo yum install gcc-c++

wget https://cmake.org/files/v3.10/cmake-3.10.0.tar.gz
tar xzf cmake-3.10.0.tar.gz
cd cmake-3.10.0
./bootstrap --prefix=/usr
make
sudo make install

sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven

cd ../
git clone --recursive https://github.com/dmlc/xgboost
cd xgboost
sudo make -j4

export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64

cd jvm-packages
sudo mvn package -DskipTests

sudo cp xgboost4j/target/xgboost4j-0.72-jar-with-dependencies.jar /usr/lib/spark/jars
sudo cp xgboost4j-spark/target/xgboost4j-spark-0.72-jar-with-dependencies.jar /usr/lib/spark/jars

sudo cp xgboost4j/target/xgboost4j-0.72-jar-with-dependencies.jar /home/hadoop/aws_spark_xgboost/sbt/lib
sudo cp xgboost4j-spark/target/xgboost4j-spark-0.72-jar-with-dependencies.jar /home/hadoop/aws_spark_xgboost/sbt/lib

curl https://bintray.com/sbt/rpm/rpm > bintray-sbt-rpm.repo
sudo mv bintray-sbt-rpm.repo /etc/yum.repos.d/
sudo yum -y install sbt

export PATH=/usr/lib/jvm/java-1.8.0-openjdk.x86_64/bin:$PATH

