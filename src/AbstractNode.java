import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import utils.P2PLogger;

public abstract class AbstractNode extends UnicastRemoteObject implements Node, Serializable {
  private static final long serialVersionUID = 1L;

  // m-bit [finger table size]
  protected int m;
  protected int id;
  protected String ipAddress;
  protected int portNum;

  protected FingerTableValue[] fingerTable;
  protected P2PLogger log;


  // This successor field is not needed, it can be accessed by using first element fingerTable
  // protected Node successor;
  // See getSuccessor()
  protected Node predecessor;

  public AbstractNode(String ipAddress, int portNum, int id) throws RemoteException {
    super();
    this.ipAddress = ipAddress;
    this.portNum = portNum;
    this.id = id;
    // TODO
    this.fingerTable = null;
    this.predecessor = null;
    this.log = new P2PLogger("NodeLogger");
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getIpAddress() {
    return ipAddress;
  }

  @Override
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @Override
  public int getPortNum() {
    return portNum;
  }

  @Override
  public void setPortNum(int portNum) {
    this.portNum = portNum;
  }

  public Node getSuccessor() throws RemoteException {
    if(fingerTable != null && fingerTable.length > 0)
      return this.fingerTable[0].getNode();

    return null;
  }

  public void setSuccessor(Node node) throws RemoteException {
    this.fingerTable[0].setNode(node);
  }

  public Node getPredecessor() throws RemoteException {
    return predecessor;
  }

  public void setPredecessor(Node node) throws RemoteException {
    this.predecessor = node;
  }


}
