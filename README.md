# FinalProject-6650-p2pChord

**Real Decentralized Chord p2p**

This Chord project doesn't need any supernode, only need a dummy node. 
Any node can refresh and rejoin! And it supports concurrent PUT/GET for key-value pairs.


***How-to-test-Instruction(an Example)***

**Stage 1: Set up**

1. run dummy node 1111 <localhost 1111>

2. run 1112 based on 1111 (It might be failed) and wait for 5 seconds <localhost 1112 localhost 1111>

3. run 1113 based on 1112 and wait for 5 seconds <localhost 1113 localhost 1112>

4. run 1114 based on 1113 and wait for 5 seconds <localhost 1114 localhost 1113>

**Stage 2: To see if they can do self-stabilization and test a decentralized system**

5. After they are all stabilized, remove/shutdown dummy node 1111

6. run 1115 based on 1114 and wait for 5 seconds <localhost 1115 localhost 1114>

7. run 1116 based on 1115 and wait for 5 seconds <localhost 1116 localhost 1115>

8. run 1117 based on 1112 and wait for 5 seconds <localhost 1117 localhost 1112>

**Stage 3: To see if they can do self-stabilization and test a randomized peer-to-peer system not relying on any specific node**

9. After they are all stabilized, remove/shutdown 1113, to see other servers' reconnecting process/self-stabilization

**Stage 4: Joining/Adding new nodes further**

10. run 1118 based on 1116 and wait for 5 seconds <localhost 1118 localhost 1116>

11. run 1119 based on 1114 and wait for 5 seconds <localhost 1119 localhost 1114>

**Stage 5: Re-joining old nodes that have been in the network but being shut down previously**

12. run/restart 1113 <localhost 1113 localhost 1119>

***How-to-run-the-files***

**Option 1: edit configurations using a Java IDE**
   - for dummy node, input two arguments <hostname, port number> 
      - e.g. localhost 1111
   - for other node, input four arguments <hostname1, port number 1, hostname2, port number 2>
      - e.g. localhost 1112 localhost 1111

**Option 2: via command line/terminal**
   - compile all the files in src 
        the only controller with the main method is NodeImpl, so run this class file)

   - for running **dummy node**: 

        ```
        java NodeImpl <dummay-node-hostname> <dummy-node-port-number>
        ```

        ​	for example, the command to run dummy node at localhost:1111 will be:

        ```
        java NodeImpl localhost 1111
        ```

         for running **other node**:

        ```
        java NodeImpl <new-node-hostname> <new-node-port-number> <existing-node-hostname> <existing-node-port-number>
        ```

        for example, to enable a new node running at localhost:1112 to join the network through an existing node at localhost:1111, we can run:  

        ```
        java NodeImpl localhost 1112 localhost 1111
        ```

**Option 3: via Docker file**

- for running **dummy node**:

  ```
  ./run_node.sh <container-name> <host-name> <port-number>
  ```

  for example: 

  ```
  ./run_node.sh node1 localhost 1111
  ```

  this will run the dummy node at localhost:1111 in a container called node1

- for running **other node**:

  ```
  ./run_node.sh <new-node-container-name> <new-node-host-name> <new-node-port-number> <existing-node-container-name> <existing-node-port-number>
  ```

  for example:

  ```
  ./run_node.sh node2 localhost 1112 node1 1111
  ```

  will run a new node at localhost:1112 in container node2, and make it join the network through an existing node running at localhost:1111 in container node1. 

