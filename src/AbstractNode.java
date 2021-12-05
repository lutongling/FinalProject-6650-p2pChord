import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import utils.P2PLogger;

public abstract class AbstractNode extends UnicastRemoteObject implements Node, Serializable {
  private static final long serialVersionUID = 1L;

  protected long id;
  protected String ipAddress;
  protected int portNum;
  protected Map<Long, FingerTableValue> fingerTable;
  protected P2PLogger log;

  // not sure if successor field is needed. If so please initialize in constructor.
  protected Node successor;

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

  /*
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public int getPortNum() {
    return portNum;
  }

  public void setPortNum(int portNum) {
    this.portNum = portNum;
  }

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

  public Node getSuccessor() {
    return successor;
  }

  public void setSuccessor(Node successor) {
    this.successor = successor;
  }

  public Node getPredecessor() {
    return predecessor;
  }

  public void setPredecessor(Node predecessor) {
    this.predecessor = predecessor;
  }
  */




}
