REMOTE_HOST=$1
IMAGE_NAME="namsterdam"
docker build --no-cache -t $IMAGE_NAME .
docker save namsterdam | gzip > /tmp/img.zip
scp ./build-remote.sh $REMOTE_HOST:~/build-remote.sh
scp ./docker-compose.yml $REMOTE_HOST:~/docker-compose.yml
scp /tmp/img.zip $REMOTE_HOST:/tmp/namsterdam.zip
ssh $REMOTE_HOST 'chmod +x ~/build-remote.sh && ~/build-remote.sh'