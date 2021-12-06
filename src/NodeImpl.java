import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;

public class NodeImpl extends AbstractNode {
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
    try {
      node = (Node) Naming.lookup("rmi://" + ((AbstractNode) node).getIpAddress()
              + ":" + ((AbstractNode) node).getPortNum() + "/Node");
    } catch (Exception e) {
      log.log(Level.WARNING, "Exception occurs in initFingerTable:" + e.getMessage());
    }
    this.fingerTable.get(1L).node = node.findSuccessor(this.fingerTable.get(1L).start);
    this.predecessor = ((AbstractNode)this.successor).predecessor;
    ((AbstractNode)this.successor).predecessor = this; // "this" is n
    for(int i=1; i<=m-1; i++) {
      long fingerINodeId = ((AbstractNode)this.fingerTable.get(Long.valueOf(i)).node).getId();
      long fingerIPlus1DotStart = this.fingerTable.get(Long.valueOf(i+1)).start;
      if( fingerIPlus1DotStart > this.getId() && fingerIPlus1DotStart < fingerINodeId ){
        this.fingerTable.get(Long.valueOf(i+1)).node = this.fingerTable.get(Long.valueOf(i)).node;
      } else {
        this.fingerTable.get(Long.valueOf(i+1)).node
                = node.findSuccessor(fingerIPlus1DotStart);  // this "node" is the input n'
      }
    }
  }

  @Override
  public void stabilize() {

  }

  @Override
  public void fixFingers() {

  }

  @Override
  public void notify(Node node) {

  }

  @Override
  public void join(Node node) {

  }

  @Override
  public void updateOthers() {
    for(int i=1;i<=m;i++) {
      Node p = findPredecessor(this.id - (long) Math.pow(2,i-1));
      try {
        //  use naming.lookup on new Node before use any method of the node
        //  (except getter and setter).
        p = (Node) Naming.lookup("rmi://" + ((AbstractNode) p).getIpAddress()
                + ":" + ((AbstractNode) p).getPortNum() + "/Node");
        p.updateFingerTable(this, i);
      } catch (Exception e) {
        log.log(Level.WARNING, "Exception occurs in updateOthers:" + e.getMessage());
      }

    }
  }

  @Override
  public void updateFingerTable(Node s, long i) {
    if(s == this) {return;} // The finger table of a Node can't contain itself.
    long sId = ((AbstractNode)s).getId();
    long fingerINodeId = ((AbstractNode)this.fingerTable.get(i).node).getId();
    if( sId > this.getId() && sId < fingerINodeId){
      this.fingerTable.get(i).node = s;
      Node p = this.predecessor;
      try {
        //  use naming.lookup on new Node before use any method of the node
        //  (except getter and setter).
        p = (Node) Naming.lookup("rmi://" + ((AbstractNode) p).getIpAddress()
                + ":" + ((AbstractNode) p).getPortNum() + "/Node");
        p.updateFingerTable(s, i);
      } catch (Exception e) {
        log.log(Level.WARNING, "Exception occurs in updateFingerTable:" + e.getMessage());
      }
    }
  }
}
