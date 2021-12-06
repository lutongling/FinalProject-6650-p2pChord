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
    if (this.predecessor == null || (node.getId() < this.id && node.getId() > this.predecessor.getId())) {
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
  public void updateOthers() {

  }

  @Override
  public void updateFingerTable() {

  }
}
