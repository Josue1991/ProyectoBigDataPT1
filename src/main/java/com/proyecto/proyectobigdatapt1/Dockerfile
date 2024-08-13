# Dockerfile

# Usa una imagen base de Ubuntu
FROM ubuntu:20.04

# Evita que los prompts interactivos interrumpan el proceso de construcción
ARG DEBIAN_FRONTEND=noninteractive

# Actualiza e instala dependencias necesarias
RUN apt-get update && \
    apt-get install -y wget openjdk-11-jdk && \
    rm -rf /var/lib/apt/lists/*

# Establecer JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Instala Hadoop
RUN wget https://downloads.apache.org/hadoop/common/hadoop-3.2.1/hadoop-3.2.1.tar.gz && \
    tar xzf hadoop-3.2.1.tar.gz -C /opt && \
    rm hadoop-3.2.1.tar.gz

# Configura Hadoop
ENV HADOOP_HOME=/opt/hadoop-3.2.1
ENV HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop

# Crear un directorio vacío para hadoop.home.dir
RUN mkdir -p /opt/empty-dir

# Establecer hadoop.home.dir a un directorio vacío
ENV HADOOP_HOME_DIR=/opt/empty-dir

# Copiar archivos de configuración
COPY /config/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml
COPY /config/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml

# Crear directorios necesarios
RUN mkdir -p /opt/hadoop-data /opt/hadoop-tmp

# Exponer puertos necesarios
EXPOSE 9870 9000

# Comando para iniciar Hadoop
CMD ["hadoop-daemon.sh", "start", "namenode"]
