# Download some vocab/FHIR patient files and unzip
# sudo mkdir /temp/fhir-to-omop/default-fhir-patients
# sudo mkdir /temp/fhir-to-omop/default-vocabulary

sudo aws s3 cp s3://selway-fhir-resources/synthea-micro.zip /temp/fhir-to-omop/default-fhir-patients/
sudo aws s3 cp s3://selway-fhir-resources/fhir_vocabulary_download_v5.zip /temp/fhir-to-omop/default-vocabulary/

sudo unzip /temp/fhir-to-omop/default-fhir-patients/synthea-micro.zip -d /temp/fhir-to-omop/default-fhir-patients/
sudo unzip /temp/fhir-to-omop/default-vocabulary/fhir_vocabulary_download_v5.zip -d /temp/fhir-to-omop/default-vocabulary/

sudo rm /temp/fhir-to-omop/default-fhir-patients/synthea-micro.zip
sudo rm /temp/fhir-to-omop/default-vocabulary/fhir_vocabulary_download_v5.zip

# Setup startup script and execute
sudo chmod +x ~/startup.sh
sudo echo "@reboot  ~/startup.sh" >> ~/reboot
sudo mv ~/reboot /etc/cron.d/reboot
sudo bash ~/startup.sh
