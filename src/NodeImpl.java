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

  }

  @Override
  public void updateFingerTable() {

  }
}
