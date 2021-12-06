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
