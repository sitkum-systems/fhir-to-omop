#!/bin/bash
echo "Hello from UserData!"
sudo dnf update -y
# Install Dependencies
echo "---------- Install Dependencies ----------"
sudo dnf install git -y
# This is so I can keep the "shape" of the !Sub for when you have actual values to sub in.
#echo "${SECRETID}"

# Install JDK
echo "---------- Install JDK ----------"
echo y | sudo dnf install java-11-amazon-corretto
JRE_HOME=/usr/lib/jvm/java-11-amazon-corretto/
sudo chmod +x $JRE_HOME/lib/jspawnhelper

# Install Apache Maven
echo "---------- Install Apache Maven ----------"
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven
mvn --version

# Install Docker
echo "---------- Install Docker ----------"
sudo yum install -y docker

# Install docker-compose
echo "---------- Install docker-compose ----------"
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version

# Clone FHIR-TO-OMOP from our private repo
echo "---------- Clone FHIR-TO-OMOP from our private repo ----------"
cd /home/ec2-user
git clone https://github.com/sitkum-systems/fhir-to-omop
cd /home/ec2-user/fhir-to-omop
git checkout linux
cd /home/ec2-user
echo "--- command: ls ---"
ls
echo "--- command: ls fhir-to-omop ---"
ls fhir-to-omop
echo "--- command: ls fhir-to-omop/BroadseaComponents ---"
ls fhir-to-omop/BroadseaComponents

# Clone Broadsea Docker image from OHDSI
echo "---------- Clone Broadsea Docker image from OHDSI ----------"
cd /home/ec2-user
git clone https://github.com/OHDSI/Broadsea
cd /home/ec2-user/Broadsea
git checkout 069e0492823b43705f849ffc710c6d07dfd19980

# Replace docker-compose.yml for running fhir-to-omop
echo "---------- Replace docker-compose.yml for running fhir-to-omop ----------"
rm docker-compose.yml
# wget https://nachc-cad.github.io/fhir-to-omop/pages/navbar/getting-started/fhir-to-omop/img/docker-compose.yml
cd /home/ec2-user/
ls
cp fhir-to-omop/BroadseaComponents/docker-compose.yml Broadsea

# Install R
echo "---------- Install R ----------"
sudo yum install -y R

# Compile the code
echo "---------- Compile the code ----------"
cd /home/ec2-user/fhir-to-omop
mvn clean install

# Tell CFN that it worked
yum install -y aws-cfn-bootstrap
echo "after yum install aws-cfn-bootstrap"
sudo /opt/aws/bin/cfn-init -v --stack ${AWS::StackName} --resource Instance --region us-east-2
echo "after cfn-init"
sudo /opt/aws/bin/cfn-signal -e $? --stack ${AWS::StackName} --resource Instance --region us-east-2
echo "after cfn-signal"
echo "All Done, tell CloudFormation it worked"