#!/bin/bash
# VesselDoc-server installer
# Author: Frode Pedersen

export DEBIAN_FRONTEND=noninteractive
export LC_ALL=C
LOGFILE='/var/log/install.log'

# Variables
DBUSER='dbuser'
DBPASSWORD='verysecurepassword'
DBNAME='vesseldoc'  # Dont change
SQLFILE="$(find . -name setupdb.sql | head -n 1)"
PROPERTIESFILE="./src/main/resources/application.properties"


# Update distro
echo 'Updating system'
sudo apt-get -y update >> $LOGFILE 2>&1
sudo apt-get -y upgrade >> $LOGFILE 2>&1
sleep 1
# Installing MySQL
echo "Installing MySQL"
sudo apt-get -y install mariadb-server >> $LOGFILE 2>&1

# Configuring MySQL
sudo echo "mysql-server mysql-server/root_password password $DBPASSWORD" | debconf-set-selections >> $LOGFILE 2>&1
sudo echo "mysql-server mysql-server/root_password_again password $DBPASSWORD" | debconf-set-selections >> $LOGFILE 2>&1

# Creating database
sudo mysql -u root -p$DBPASSWORD -e "CREATE DATABASE IF NOT EXISTS $DBNAME;" >> $LOGFILE 2>&1
sudo mysql -u root -p$DBPASSWORD -e "GRANT ALL ON $DBNAME.* TO '$DBUSER'@'localhost' IDENTIFIED BY '$DBPASSWORD';" >> $LOGFILE 2>&1
sudo mysql -u root -p$DBPASSWORD -e "FLUSH PRIVILEGES;" >> $LOGFILE 2>&1
sudo mysql -u root -p$DBPASSWORD $DBNAME < $SQLFILE

sudo service mysql restart >> $LOGFILE 2>&1

# Configuring server application
echo 'Configuring server application'

touch $PROPERTIESFILE
echo "jwt.secret=javainuse
spring.datasource.url=jdbc:mysql://localhost:3306/$DBNAME
spring.datasource.username=$DBUSER
spring.datasource.password=$DBPASSWORD
spring.datasource.platform=mysql
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl" >> $PROPERTIESFILE

# Build server application
echo 'Building server application'
sudo apt-get -y install maven >> $LOGFILE 2>&1
sudo mvn clean package
APPFILE="$(find ./target | grep -E .jar$ | head -n 1 | xargs realpath)"
sudo chmod 500 $APPFILE

echo 'Making application as service'
sudo ln -s $APPFILE /etc/init.d/vesseldoc-server
sudo service vesseldoc-server start

# https://www.baeldung.com/spring-boot-app-as-a-service
sudo touch /etc/systemd/system/vesseldoc-server.service
sudo echo "[Unit]
Description=A VesselDoc Server
After=syslog.target network.target

[Service]
User=root
ExecStart=/usr/bin/java -jar $APPFILE SuccessExitStatus=143

[Install]
WantedBy=multi-user.target" > /etc/systemd/system/vesseldoc-server.service

echo 'The installation is finished'
