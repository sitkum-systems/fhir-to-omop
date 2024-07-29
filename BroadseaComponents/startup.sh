# Get EC2 instance's public hostname and put into env var
TOKEN=`curl -X PUT "http://169.254.169.254/latest/api/token" -H "X-aws-ec2-metadata-token-ttl-seconds: 21600"` && curl -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/meta-data/
HOSTNAME=`curl -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/meta-data/public-hostname`

sudo envsubst '$HOSTNAME' < fhir-to-omop/BroadseaComponents/.env > .env
sudo cp .env Broadsea

# Start up Broadsea container w/ Postgres Server
cd ~/Broadsea
sudo systemctl start docker
sudo docker-compose pull && sudo docker-compose --profile default up -d