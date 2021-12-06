import java.rmi.Naming;

import java.rmi.RemoteException;
import java.util.Random;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;

public class NodeImpl extends AbstractNode {

  protected NodeImpl(String ipAddress, int portNum, int id) throws RemoteException {
    super(ipAddress, portNum, id);
  }

  @Override
  public Node findSuccessor(int id) throws RemoteException {
    Node predecessor = findPredecessor(id);
    Node successor = null;

    try {
      Node connected = (Node) Naming.lookup("rmi://" + predecessor.getIpAddress() + ":" + predecessor.getPortNum() + "/Node");
      successor = connected.getSuccessor();
      log.logInfoMessage("Successor for given node: " + id + " is node: " + successor.getId());
    } catch (Exception e) {
      log.logErrorMessage("Connection failed in findSuccessor." + e.getMessage());
      // e.printStackTrace();
    }

    return successor;
  }

  @Override
  public Node findPredecessor(int id) throws RemoteException {
    Node newNode = this;
    Node nextConnectedNode = null;

//    int thisId = this.id;
//    int newNodeId = newNode.getId();
//    int newNodeSuccessorId = newNode.getSuccessor().getId();

    //  id ! << ( , ]
    while(!inIntervalIncludeClose(id, newNode.getId(), newNode.getSuccessor().getId())){
      if(newNode == this) {
        // start
        newNode = newNode.closestPrecedingFinger(id);
      } else {
        assert false; // if run at within this scope, nextConnectedNode should not be null.
        newNode = nextConnectedNode.closestPrecedingFinger(id);
      }

      try {
        nextConnectedNode = (Node) Naming.lookup("rmi://" + newNode.getIpAddress() + ":" + newNode.getPortNum() + "/Node");
        newNode = nextConnectedNode;
      } catch (Exception e) {
        log.logErrorMessage("Connection failed in findPredecessor." + e.getMessage());
        // e.printStackTrace();
      }

    }

    return newNode;
  }

  @Override
  public Node closestPrecedingFinger(int id) {
    for(int i = fingerTable.length - 1; i > 0; i--) {
      Node nextNode = fingerTable[i].getNode();
      // nextNode ( , )
      if(inInterval(nextNode.getId(), this.id, id)) {
        return nextNode;
      }
    }
    return this;
  }

  @Override
  public void initFingerTable(Node node) {
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
      Node connected = (Node) Naming.lookup("rmi://" + successor.getIpAddress() + ":" + successor.getPortNum() + "/Node");
      x = connected.getPredecessor();
    } catch (Exception e) {
      log.logErrorMessage("Connection failed in stabilize." + e.getMessage());
      // e.printStackTrace();
    }

    // x >>> ( , )
    if(x != null && inInterval(x.getId(), this.id, successor.getId())) {
      successor = x;
    }

    // Not sure if this should be done by reconnecting to x?
    successor.notify();
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

  @Override
  public void fixFingers() throws RemoteException {
    log.logInfoMessage("Fixing fingers...");

    Random rand = new Random();
    int randomIdx = rand.nextInt(m - 1) + 2;

    this.fingerTable[randomIdx].setNode(this.findSuccessor(this.fingerTable[randomIdx].getStart()));
  }

  @Override
  public void notify(Node node) {
    // TODO BE called in stabilization
    // (node.getId() < this.id && node.getId() > this.predecessor.getId())
    if (this.predecessor == null ||  this.inInterval(this.id, node.getId(), this.predecessor.getId())) {
      this.predecessor = node;
    }
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
      this.setSuccessor(node.findSuccessor(this.getId()));
    } catch (RemoteException e) {
      // todo: throw? catch? and log
      log.logErrorMessage("Failed to join");
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
  public void updateFingerTable(Node s, int i) {
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
}
