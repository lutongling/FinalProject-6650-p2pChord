import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;

public class NodeImpl extends AbstractNode {

  protected NodeImpl(String ipAddress, int portNum, int id) throws RemoteException {
    super(ipAddress, portNum, id);
  }

  @Override
  public Node findSuccessor(int id) {
    return null;
  }

  @Override
  public Node findPredecessor(int id) {
    return null;
  }

  @Override
  public Node closestPrecedingFinger(int id) {
    return null;
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
      if( fingerIPlus1DotStart > this.getId() && fingerIPlus1DotStart < fingerINodeId ){
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
//        tempPeer = fingerTable.get((int) i).getSuccessor();
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
    for(int i=1;i<=m;i++) {
      Node p = findPredecessor(this.id - (int)Math.pow(2,i-1));
      try {
        //  use naming.lookup on new Node before use any method of the node
        //  (except getter and setter).
        p = (Node) Naming.lookup("rmi://" + ((AbstractNode) p).getIpAddress()
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
    if( sId > this.getId() && sId < fingerINodeId){
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
