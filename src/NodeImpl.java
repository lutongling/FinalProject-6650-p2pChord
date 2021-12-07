import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class NodeImpl extends AbstractNode {

  protected NodeImpl(String ipAddress, int portNum) throws RemoteException, UnsupportedEncodingException, NoSuchAlgorithmException {
    super(ipAddress, portNum);
  }

  @Override
  public Node findSuccessor(int id) throws RemoteException {
    Node predecessor = findPredecessor(id);
    Node successor = null;

    try {
      predecessor = (Node) Naming.lookup("rmi://" + predecessor.getIpAddress() + ":" + predecessor.getPortNum() + "/Node");
      successor = predecessor.getSuccessor();
      // log.logInfoMessage("Successor for given node: " + id + " is node: " + successor.getId());
    } catch (Exception e) {
      // log.logErrorMessage("Connection failed in findSuccessor." + e.getMessage());
      e.printStackTrace();
    }

    return successor;
  }

  @Override
  public Node findPredecessor(int id) throws RemoteException {
    Node newNode = this;
//    Node nextConnectedNode = null;
//    int thisId = this.id;
//    int newNodeId = newNode.getId();
//    int newNodeSuccessorId = newNode.getSuccessor().getId();

    //  id ! << ( , ]
    int newNodeId = newNode.getId();
    int newNodeSuccessorId = newNode.getSuccessor().getId();

    while(!inIntervalIncludeClose(id, newNodeId, newNodeSuccessorId)){

      if(newNode == this) {
        // start
          newNode = newNode.closestPrecedingFinger(id);
//        newNode = newNode.getPredecessor();
      } else {
//        assert false; // if run at within this scope, nextConnectedNode should not be null.

        try {
          System.out.println("57: " + newNode.getPortNum());

          newNode = (Node) Naming.lookup("rmi://" + newNode.getIpAddress() + ":" + newNode.getPortNum() + "/Node");

          System.out.println("61: " + newNode.getPortNum());

          newNode = newNode.closestPrecedingFinger(id);

          System.out.println("65: " + newNode.getPortNum());

          newNode = (Node) Naming.lookup("rmi://" + newNode.getIpAddress() + ":" + newNode.getPortNum() + "/Node");

          System.out.println("69: " + newNode.getPortNum());

          newNodeId = newNode.getId();
          newNodeSuccessorId = newNode.getSuccessor().getId();

          // newNode = nextConnectedNode;
        } catch (Exception e) {
          // log.logErrorMessage("Connection failed in findPredecessor." + e.getMessage());
          e.printStackTrace();

//          try {
//            log.logInfoMessage("Reconnecting with 1112...");
//            newNode = (Node) Naming.lookup("rmi://localhost" + ":1112" + "/Node");
//          } catch (Exception e1) {
//            log.logErrorMessage("1112 down --- " + e1.getMessage());
//          }

        }

      }

//      try {
//        newNode = (Node) Naming.lookup("rmi://" + newNode.getIpAddress() + ":" + newNode.getPortNum() + "/Node");
//        // newNode = nextConnectedNode;
//      } catch (Exception e) {
//        // log.logErrorMessage("Connection failed in findPredecessor." + e.getMessage());
//        e.printStackTrace();
//      }

    }

    return newNode;
  }

  @Override
  public Node closestPrecedingFinger(int id) throws RemoteException {
//    for(int i = fingerTable.length - 1; i > 0; i--) {
//      Node nextNode = fingerTable[i].getNode();
//      // nextNode ( , )
//      if(inInterval(nextNode.getId(), this.id, id)) {
//        return nextNode;
//      }
//    }

    for(int i = m; i > 0; i--) {
      Node nextNode = fingerTable[i].getNode();
      // nextNode ( , )
      if(inInterval(nextNode.getId(), this.id, id)) {
        return nextNode;
      }
    }

    return this;
  }

  @Override
  public void initFingerTable(Node node) throws RemoteException {
    try {
      node = (Node) Naming.lookup("rmi://" + ((AbstractNode) node).getIpAddress()
              + ":" + ((AbstractNode) node).getPortNum() + "/Node");
    } catch (Exception e) {
      log.logErrorMessage("Exception occurs in initFingerTable:" + e.getMessage());
    }
    try {
      this.fingerTable[1].setNode(node.findSuccessor(this.fingerTable[1].getStart()));  // this.fingerTable[1].node = node.findSuccessor(this.fingerTable[1].getStart());
      this.predecessor = this.getSuccessor().getPredecessor();
      this.getSuccessor().setPredecessor(this); // "this" is n  // this.getSuccessor().predecessor = this;
    } catch (Exception e) {
      log.logErrorMessage("Can't get predecessor in initFingerTable: " + e.getMessage());
      e.printStackTrace();
    }

    for(int i=1; i<=m-1; i++) {
      int fingerINodeId = this.fingerTable[i].getNode().getId();
      int fingerIPlus1DotStart = this.fingerTable[i+1].getStart();
      if( inIntervalIncludeOpen(fingerIPlus1DotStart, this.getId(), fingerINodeId) ){
        this.fingerTable[i+1].setNode(this.fingerTable[i].getNode());  // this.fingerTable[i+1].node = this.fingerTable[i].getNode();
      } else {
        try{
        this.fingerTable[i+1].setNode(node.findSuccessor(fingerIPlus1DotStart));  // this "node" is the input n' 
//        this.fingerTable[i+1].node = node.findSuccessor(fingerIPlus1DotStart);
        } catch (Exception e) {
          log.logErrorMessage("Can't get predecessor in initFingerTable: " + e.getMessage());
        }
      }
    }
  }

  public void stabilize() throws RemoteException {
    log.logInfoMessage("Stabilizing...");
    // not sure if we can do this directly instead of connecting since we are using the Node reference
    // Node x = this.getSuccessor().getPredecessor();
    Node successor = this.getSuccessor();
    Node x = null;

    try {
      successor = (Node) Naming.lookup("rmi://" + successor.getIpAddress() + ":" + successor.getPortNum() + "/Node");
      x = successor.getPredecessor();
      log.logInfoMessage("Predecessor: " + x.getId());
    } catch (Exception e) {

      log.logErrorMessage("Connection failed in stabilize." + e.getMessage());

      try {
        Thread.sleep(5000);
        log.logInfoMessage("Reconnecting with this...");
        successor = this;
        x = successor.getPredecessor();

      } catch (Exception e1) {
        log.logErrorMessage("Impossible" + e1.getMessage());
      }

    }

    // x >>> ( , )
    if(x != null && inInterval(x.getId(), this.id, successor.getId())) {
//      successor = x;
      this.setSuccessor(x);
    }

    // Not sure if this should be done by reconnecting to x?
    try {
      successor = (Node) Naming.lookup("rmi://" + successor.getIpAddress() + ":" + successor.getPortNum() + "/Node");
      System.out.println("135: " + successor.getPortNum());

      successor.notifyNode(this);

      System.out.println("139: " + this.predecessor);

      log.logInfoMessage("Successor: " + successor.getId());
    } catch (Exception e) {
      log.logErrorMessage("Connection failed for successor cin stabilize." + e.getMessage());
      // e.printStackTrace();
    }

    // this.printFingerTable(this.fingerTable);

  }


  // helper to justify if x is within set (open, close)
  private boolean inInterval(int x, int open, int close) {
    return (open == close) || (((x > open) && (x < close))) || ((open > close) && (x > open || x < close));

  }

  private boolean inIntervalIncludeOpen(int x, int open, int close){
    return x == open || inInterval(x, open, close);
  }

  // According to the paper, this is called by some function such as init_finger_table
  private boolean inIntervalIncludeClose(int x, int open, int close){
    return x == close || inInterval(x, open, close);
  }

  // helper to print finger table
//  private void printFingerTable(FingerTableValue[] fingerTable) throws RemoteException {
//    for(int i = 1; i < fingerTable.length; i++) {
//      // Node fingerTableNode = (Node) Naming.lookup("rmi://" + successor.getIpAddress() + ":" + successor.getPortNum() + "/Node");
//      log.logInfoMessage("Start for " + i + " " + fingerTable[i].getStart());
//      log.logInfoMessage("Node ip address " + fingerTable[i].getNode().getIpAddress());
//      log.logInfoMessage("Node port address " + fingerTable[i].getNode().getPortNum());
//      log.logInfoMessage("Node id " + fingerTable[i].getNode().getId());
//    }
//  }

  @Override
  public void fixFingers() throws RemoteException {
    log.logInfoMessage("Fixing fingers...");

    Random rand = new Random();
    // randomIdx >> [2, 32] int
    int randomIdx = rand.nextInt(m - 1) + 2;
    System.out.println("RandomIdx: " + randomIdx);

    this.fingerTable[randomIdx].setNode(this.findSuccessor(this.fingerTable[randomIdx].getStart()));

    System.out.println("189: " + this.getPredecessor());

    log.logInfoMessage("Predecessor: " + this.getPredecessor().getId());

    log.logInfoMessage("Successor: " + this.getSuccessor().getId());

    // this.printFingerTable(this.fingerTable);

  }

  @Override
  public void notifyNode(Node node) throws RemoteException {
    // TODO BE called in stabilization

    // (node.getId() < this.id && node.getId() > this.predecessor.getId())
    //if (this.predecessor == null ||  this.inInterval(this.id, node.getId(), this.predecessor.getId())) {
    if (this.predecessor == null ||  this.inInterval(node.getId(), this.predecessor.getId(), this.id)) {
      this.setPredecessor(node);
      System.out.println("205: " + this.predecessor);
    }

    System.out.println("208: notify success");

  }

//  original join method
//@Override
//public void join(Node node) {
//  if (node != null) {
//    // current node is the only node in the network
//    this.initFingerTable(node);
//    this.updateOthers();
//  } else {
//    for (int i = 1; i <= this.m; i++) {
//      int start = (int) (this.id + Math.pow(2, i - 1) % Math.pow(2, this.m));
//      FingerTableValue value = new FingerTableValue(start, this);
//      this.fingerTable[i] = value;
//      }
//    this.predecessor = this;
//    }
//
//  }

  @Override
  public void join(Node node) {
    this.predecessor = null;
    try {
      node = (Node) Naming.lookup("rmi://" + node.getIpAddress() + ":" + node.getPortNum() + "/Node");
      this.setSuccessor(node.findSuccessor(this.getId()));
    } catch (RemoteException e) {
      // todo: throw? catch? and log
      log.logErrorMessage("Failed to join");
      e.printStackTrace();
    } catch (MalformedURLException | NotBoundException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void updateOthers() throws RemoteException {
    for(int i=1;i<=m;i++) {
      Node p = findPredecessor(this.id - (int)Math.pow(2,i-1));
      try {
        //  use naming.lookup on new Node before use any method of the node
        //  (except getter and setter).
        p = (Node) Naming.lookup("rmi://" +  p.getIpAddress()
                + ":" + ((AbstractNode) p).getPortNum() + "/Node");
        p.updateFingerTable(this, i);
      } catch (Exception e) {
        log.logErrorMessage( "Exception occurs in updateOthers:" + e.getMessage());
      }

    }
  }

  @Override
  public void updateFingerTable(Node s, int i) throws RemoteException {
    if(s == this) {return;} // The finger table of a Node can't contain itself.
    int sId = (s).getId();
    int fingerINodeId = (this.fingerTable[i].getNode()).getId();
    if( inIntervalIncludeOpen(sId, this.getId(), fingerINodeId) ){
      this.fingerTable[i].setNode(s);  // fingerTable[i].node = s;
      Node p = this.predecessor;
      try {
        //  use naming.lookup on new Node before use any method of the node
        //  (except getter and setter).
        p = (Node) Naming.lookup("rmi://" + p.getIpAddress()
                + ":" + p.getPortNum() + "/Node");
        p.updateFingerTable(s, i);
      } catch (Exception e) {
        log.logErrorMessage("Exception occurs in updateFingerTable:" + e.getMessage());
      }
    }
  }

  public void echo(Node joinedNode) throws RemoteException {
    log.logInfoMessage("New joined node IP: " + joinedNode.getIpAddress() +
            " New joined node Port" + joinedNode.getPortNum() +
            " New joined node ID: " + joinedNode.getId());
  }

  public void createFingerTable() throws RemoteException {
//    AbstractNode absNode = (AbstractNode) node;
    int i;
    for (i = 1; i <= m; i++) {
      int start = (this.getId() + (int) Math.pow(2, i - 1)) % (int) Math.pow(2, m);
      FingerTableValue fte = new FingerTableValue(start, this);
      this.fingerTable[i] = fte;
    }

  }

  // entry main point
  public static void main(String[] args) throws Exception {

    // TODO: length
    // 0, 1, 2, 3

    if(args.length == 2) {
      String newNodeIpAddress = args[0];
      int newNodePort = Integer.parseInt(args[1]);
      Node newNode = new NodeImpl(newNodeIpAddress, newNodePort);

      LocateRegistry.createRegistry(newNodePort);
      Naming.rebind("rmi://" + newNodeIpAddress + ":" + newNodePort + "/Node", newNode);

      newNode.createFingerTable();

//      while(true) {
//        newNode.stabilize();
//        newNode.fixFingers();
//
//        Thread.sleep(5000);
//      }

    } else if (args.length == 4){
      String newNodeIpAddress = args[0];
      int newNodePort = Integer.parseInt(args[1]);

      String nodeInChordIpAddress = args[2];
      int nodeInChordPort = Integer.parseInt(args[3]);

      Node newNode = new NodeImpl(newNodeIpAddress, newNodePort);
//    Node nodeInChord = new NodeImpl(nodeInChordIpAddress, nodeInChordPort);
      Node nodeInChord = (Node) Naming.lookup("rmi://" + nodeInChordIpAddress
              + ":" + nodeInChordPort + "/Node");

      newNode.createFingerTable();
      // nodeInChord.createFingerTable();

      LocateRegistry.createRegistry(newNodePort);
      Naming.rebind("rmi://" + newNodeIpAddress + ":" + newNodePort + "/Node", newNode);

      newNode.join(nodeInChord);
      nodeInChord.echo(newNode);

      Runnable run = new Runnable() {
        @Override
        public void run() {
          while(true) {
            try {
              newNode.stabilize();
              newNode.fixFingers();

              System.out.println("Thread done");

              Thread.sleep(10000);
            } catch (Exception e) {
              System.out.println(e.getMessage());
              e.printStackTrace();
            }

          }
        }
      };

      new Thread(run).start();

//      while(true) {
//        newNode.stabilize();
//        newNode.fixFingers();
//
//        Thread.sleep(1000);
//      }

    }



  }
}
