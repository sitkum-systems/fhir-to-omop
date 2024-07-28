# Get EC2 instance's public hostname and put into env var
TOKEN=`curl -X PUT "http://169.254.169.254/latest/api/token" -H "X-aws-ec2-metadata-token-ttl-seconds: 21600"` && curl -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/meta-data/
HOSTNAME=`curl -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/meta-data/public-hostname`

sudo envsubst '$HOSTNAME' < fhir-to-omop/BroadseaComponents/.env > .env
sudo mv .env Broadsea

# Download some vocab/FHIR patient files and unzip
sudo mkdir fhir-to-omop/temp/fhir-to-omop/default-fhir-patients
sudo mkdir fhir-to-omop/temp/fhir-to-omop/default-vocabulary

sudo aws s3 cp s3://selway-fhir-resources/synthea-micro.zip fhir-to-omop/temp/fhir-to-omop/default-fhir-patients
sudo aws s3 cp s3://selway-fhir-resources/fhir_vocabulary_download_v5.zip fhir-to-omop/temp/fhir-to-omop/default-vocabulary/

sudo unzip fhir-to-omop/temp/fhir-to-omop/default-fhir-patients/synthea-micro.zip -d fhir-to-omop/temp/fhir-to-omop/default-fhir-patients/
sudo unzip fhir-to-omop/temp/fhir-to-omop/default-vocabulary/fhir_vocabulary_download_v5.zip -d fhir-to-omop/temp/fhir-to-omop/default-vocabulary/

sudo rm fhir-to-omop/temp/fhir-to-omop/default-fhir-patients/synthea-micro.zip
sudo rm fhir-to-omop/temp/fhir-to-omop/default-vocabulary/fhir_vocabulary_download_v5.zip

# Setup startup script and execute
sudo chmod +x ~/startup.sh
sudo echo "@reboot  ~/startup.sh" >> ~/reboot
sudo mv ~/reboot /etc/cron.d/reboot
sudo bash ~/startup.sh
