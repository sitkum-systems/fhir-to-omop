# Install JDK
echo y | sudo dnf install java-11-amazon-corretto
JRE_HOME=/usr/lib/jvm/java-11-amazon-corretto/
sudo chmod +x $JRE_HOME/lib/jspawnhelper

# Install Apache Maven
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven
mvn --version

# Install Docker
sudo yum install -y docker

# Install docker-compose
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version

# Clone Broadsea Docker image from OHDSI
git clone https://github.com/OHDSI/Broadsea
cd Broadsea
git checkout 069e0492823b43705f849ffc710c6d07dfd19980

# Replace docker-compose.yml for running fhir-to-omop
rm docker-compose.yml
# wget https://nachc-cad.github.io/fhir-to-omop/pages/navbar/getting-started/fhir-to-omop/img/docker-compose.yml
cd ..
cp fhir-to-omop/BroadseaComponents/docker-compose.yml Broadsea

# Install R
sudo yum install -y R

# Compile the code
cd fhir-to-omop
mvn clean install