import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import utils.P2PLogger;

public abstract class AbstractNode extends UnicastRemoteObject implements Node, Serializable {
  private static final long serialVersionUID = 1L;

  protected int m;

  protected long id;
  protected String ipAddress;
  protected int portNum;
  protected Map<Long, FingerTableValue> fingerTable;
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
    this.fingerTable = new HashMap<>();
    this.predecessor = null;
    this.log = new P2PLogger("NodeLogger");
  }


  /*
    These are getters and setters
    temporary not used, if used, define them in interface first
  */

  @Override
  public long getId() {
    return id;
  }

  @Override
  public void setId(long id) {
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

/*
  public Map<Long, FingerTableValue> getFingerTable() {
    return fingerTable;
  }

  public void setFingerTable(Map<Long, FingerTableValue> fingerTable) {
    this.fingerTable = fingerTable;
  }

  public P2PLogger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }

*/

  public Node getSuccessor() {
    if(fingerTable != null && fingerTable.size() > 0)
      return this.fingerTable.get(0L).getSuccessor();

    return null;
  }

  public void setSuccessor(Node node) {
    this.fingerTable.get(0L).setSuccessor(node);
  }

  public Node getPredecessor() {
    return predecessor;
  }

  public void setPredecessor(Node node) {
    this.predecessor = node;
  }



}
