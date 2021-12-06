import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class NodeImpl extends AbstractNode {

  protected NodeImpl(String ipAddress, int portNum, int id) throws RemoteException {
    super(ipAddress, portNum, id);
  }

  @Override
  public Node findSuccessor(long id) {
    return null;
  }

  @Override
  public Node findPredecessor(long id) {
    return null;
  }

  @Override
  public Node closestPrecedingFinger(long id) {
    return null;
  }

  @Override
  public void initFingerTable(Node node) {

  }

  @Override
  public void stabilize() {
    log.logInfoMessage("Stabilization running on the node: " + this.id);
    Node successor = null;
    Node tempPeer = null;

    Node successorFound = null;
    Node tempFound = null;

    try {
      successor = this.getSuccessor();

      if(successor.getId() == this.id) {
        // single node
        successorFound = this;

      } else {
        // RMI call to find
        log.logInfoMessage("RMI CALL to find the successor:");
        successorFound = (Node) Naming.lookup("rmi://" + successor.getIpAddress() + ":" + successor.getPortNum() + "/ChordNode");
      }
    } catch (Exception e) {
      // e.printStackTrace();
      log.logErrorMessage("Connected failed.");
    }

    // current successor is dead, trying to find the other candidates
    if(successorFound == null) {

      int i;

      for(i = 1; i < fingerTable.size(); i++) {
        tempPeer = fingerTable.get((long) i).getSuccessor();
        // in a loop iterating over its successors
        assert successor != null;
        if(tempPeer.getId() != successor.getId() && tempPeer.getId() != this.id)
          break;
      }

      // found someone in the current finger table failed!
      if(i != fingerTable.size()) {
        assert tempPeer != null;

        while(true) {
          try {
            log.logInfoMessage("Current Node in predecessor chain" + tempPeer.getId());
            tempFound = (Node) Naming.lookup("rmi://" + tempPeer.getIpAddress() + ":" + tempPeer.getPortNum() + "/ChordNode");

            // if id matched, meaning found
            if (tempFound.getPredecessor().getId() == successor.getId()) {
              tempFound.setPredecessor(this);
//              this.setSuccessor(tempPeer);
              this.setSuccessor(tempFound);

              log.logInfoMessage("New successor has been found: " + tempFound.getId());
              break;
            }

            // if id unmatched, continue the process
            tempPeer = tempFound.getPredecessor();

          } catch (Exception e) {
            // e.printStackTrace();
            log.logErrorMessage("Error occurs in stabilization");
          }

          // Future task consideration: Should we have a bootstrap node class?
          // a boot node is a very first node joining the network
          // it represents at least on existing node
        }

        // not founding someone failed
      } else {
        // TODO: need to discuss: bootstrap class operation
      }

    } else {
      // successorFound != null
      // Current successor is alive
      Node x = null;
      try {
        x = successorFound.getPredecessor();
      } catch (RemoteException e) {
        log.logErrorMessage("Error");
        // e.printStackTrace();
      }

      if((x != null)
              && (inCircularInterval(x.getId(), this.id, this.fingerTable.get(0L).getSuccessor().getId()))) {
        this.fingerTable.get(0L).setSuccessor(x);
      }

      try {
        if(successor.getId() == this.id) {
          successorFound.notify(this);
        }
      } catch (RemoteException e) {
        // e.printStackTrace();
        log.logErrorMessage("Error in calling notify from the successor found");
      }
    }
  }

  // helper
  private boolean inCircularInterval(long x, long a, long b) {
    boolean val = false;
    if (a == b)
      val = true;
    else if (a < b) {// normal range
      if ((x > a) && (x < b))
        val = true;
    } else { // when on one current node is after 0 but predecessor is before 0
      if ((x > a) && (x < (b + Math.pow(2, m)))) {// in ring before 0
        val = true;
      } else if ((x < b) && ((x + Math.pow(2, m)) > a)) {// in ring after 0
        val = true;
      }
    }
    return val;
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
