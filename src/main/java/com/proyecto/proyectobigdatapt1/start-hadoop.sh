#!/bin/bash

# Iniciar NameNode
$HADOOP_HOME/sbin/hadoop-daemon.sh start namenode

# Iniciar DataNode
$HADOOP_HOME/sbin/hadoop-daemon.sh start datanode

# Mantener el contenedor en ejecución
tail -f $HADOOP_HOME/logs/hadoop-*.log
