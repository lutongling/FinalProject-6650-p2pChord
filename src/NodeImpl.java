import java.rmi.Naming;
import java.rmi.RemoteException;

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

  }

  @Override
  public void stabilize() {


//    log.logInfoMessage("Stabilization running on the node: " + this.id);
//    Node successor = null;
//    Node tempPeer = null;
//
//    Node successorFound = null;
//    Node tempFound = null;
//
//    try {
//      successor = this.getSuccessor();
//
//      if(successor.getId() == this.id) {
//        // single node
//        successorFound = this;
//
//      } else {
//        // RMI call to find
//        log.logInfoMessage("RMI CALL to find the successor:");
//        successorFound = (Node) Naming.lookup("rmi://" + successor.getIpAddress() + ":" + successor.getPortNum() + "/ChordNode");
//      }
//    } catch (Exception e) {
//      // e.printStackTrace();
//      log.logErrorMessage("Connected failed.");
//    }
//
//    // current successor is dead, trying to find the other candidates
//    if(successorFound == null) {
//
//      int i;
//
//      for(i = 1; i < fingerTable.size(); i++) {
//        tempPeer = fingerTable.get((long) i).getSuccessor();
//        // in a loop iterating over its successors
//        assert successor != null;
//        if(tempPeer.getId() != successor.getId() && tempPeer.getId() != this.id)
//          break;
//      }
//
//      // found someone in the current finger table failed!
//      if(i != fingerTable.size()) {
//        assert tempPeer != null;
//
//        while(true) {
//          try {
//            log.logInfoMessage("Current Node in predecessor chain" + tempPeer.getId());
//            tempFound = (Node) Naming.lookup("rmi://" + tempPeer.getIpAddress() + ":" + tempPeer.getPortNum() + "/ChordNode");
//
//            // if id matched, meaning found
//            if (tempFound.getPredecessor().getId() == successor.getId()) {
//              tempFound.setPredecessor(this);
////              this.setSuccessor(tempPeer);
//              this.setSuccessor(tempFound);
//
//              log.logInfoMessage("New successor has been found: " + tempFound.getId());
//              break;
//            }
//
//            // if id unmatched, continue the process
//            tempPeer = tempFound.getPredecessor();
//
//          } catch (Exception e) {
//            // e.printStackTrace();
//            log.logErrorMessage("Error occurs in stabilization");
//          }
//
//          // Future task consideration: Should we have a bootstrap node class?
//          // a boot node is a very first node joining the network
//          // it represents at least on existing node
//        }
//
//        // not founding someone failed
//      } else {
//        // TODO: need to discuss: bootstrap class operation
//      }
//
//    } else {
//      // successorFound != null
//      // Current successor is alive
//      Node x = null;
//      try {
//        x = successorFound.getPredecessor();
//      } catch (RemoteException e) {
//        log.logErrorMessage("Error");
//        // e.printStackTrace();
//      }
//
//      if((x != null)
//              && (inInterval(x.getId(), this.id, this.fingerTable[0].getNode().getId()))) {
//        this.fingerTable[0].setNode(x);
//      }
//
//      try {
//        if(successor.getId() == this.id) {
//          successorFound.notify(this);
//        }
//      } catch (RemoteException e) {
//        // e.printStackTrace();
//        log.logErrorMessage("Error in calling notify from the successor found");
//      }
//    }
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
  public void fixFingers() {

  }

  @Override
  public void notify(Node node) {
    // TODO BE called in stabilization


  }

  @Override
  public void join(Node node) {
    if (node != null) {
      // current node is the only node in the network
      this.initFingerTable(node);
      this.updateOthers();
    } else {
      for (int i = 1; i <= this.m; i++) {

      }
    }
  }

  @Override
  public void updateOthers() {

  }

  @Override
  public void updateFingerTable() {

  }
}
