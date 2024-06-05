#!/bin/bash

sudo apt-get update
sudo apt-get install ca-certificates curl gnupg unzip wget
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg
echo "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/Debian "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
sudo apt install docker-compose

cd /home
wget https://github.com/getsentry/self-hosted/archive/refs/tags/23.4.0.zip
unzip 23.4.0.zip
cd self-hosted-23.4.0

sudo chmod +x install.sh
./install.sh

sudo dockerd &
docker-compose up -d
