# Install JDK
sudo dnf install java-11-amazon-corretto
JRE_HOME=/usr/lib/jvm/java-11-amazon-corretto/
sudo chmod +x $JRE_HOME/lib/jspawnhelper
# Install Apache Maven
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven
mvn --version
# Install Docker
sudo yum install docker
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
wget https://nachc-cad.github.io/fhir-to-omop/pages/navbar/getting-started/fhir-to-omop/img/docker-compose.yml
cd ..
# Install R
sudo yum install R

# # Download standalone fhir-to-omop app, unzip, and run
# wget https://github.com/NACHC-CAD/fhir-to-omop/releases/download/v1.6.003/fhir-to-omop-v1.6.003.zip
# unzip fhir-to-omop-v1.6.003.zip fhir-to-omop

# Clone fhir-to-omop code here

# Start up Docker container for Postgres, etc
cd Broadsea
docker-compose pull && docker-compose --profile default up -d
# Stop Broadsea with this:
# docker-compose --profile default down
cd ..
sudo java -jar  -Dosgi.requiredJavaVersion=1.8 -Dosgi.instance.area.default=@user.home/eclipse-workspace -XX:+UseG1GC -XX:+UseStringDeduplication -Dosgi.requiredJavaVersion=11 -Dosgi.dataAreaRequiresExplicitInit=true -Xms1g -Xmx16g fhir-to-omop.jar i
