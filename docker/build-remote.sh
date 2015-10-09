docker-compose stop
docker rm namsterdam
gunzip < /tmp/namsterdam.zip | docker load
docker-compose up &