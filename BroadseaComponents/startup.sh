# Start up Broadsea container w/ Postgres Server
cd ~/Broadsea
sudo systemctl start docker
sudo docker-compose pull && sudo docker-compose --profile default up -d