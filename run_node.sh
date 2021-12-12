PROJECT_NETWORK='project-network'
NODE_IMAGE='project-node-image'
NODE_CONTAINER='node1'

if [ $# -eq 5 ]
then
  docker run -it --rm \
    --name "$1" \
    --network $PROJECT_NETWORK $NODE_IMAGE \
    java NodeImpl "$2" "$3" "$4" "$5"

elif [ $# -eq 3 ]
then
  # clean up existing resources, if any
  echo "----------Cleaning up existing resources----------"
  docker container stop $NODE_CONTAINER 2> /dev/null && docker container rm $NODE_CONTAINER 2> /dev/null
  docker network rm $PROJECT_NETWORK 2> /dev/null

  # only cleanup
  if [ "$1" == "cleanup-only" ]
  then
    exit
  fi

  # create a custom virtual network
  echo "----------creating a virtual network----------"
  docker network create $PROJECT_NETWORK

  # build the images from Dockerfile
  echo "----------Building images----------"
  docker build -t $NODE_IMAGE --target node-build .

  # run the command
  docker run -it --rm \
    --name "$1" \
    --network $PROJECT_NETWORK $NODE_IMAGE \
    java NodeImpl "$2" "$3"

else
  echo "Usage: ./run_node.sh <new-node-name> <new-node-host-name> <new-node-port> <old-node-name> <old-node-port>"
  exit
fi

docker logs "$1" -f