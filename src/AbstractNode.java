import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractNode implements Node {
  private static final long serialVersionUID = 1L;

  protected int m;

  protected long id;
  protected String ipAddress;
  protected int portNum;
  protected Map<Long, FingerTableValue> fingerTable;
  protected Logger log;

  protected Node successor;
  protected Node predecessor;


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

  public Logger getLog() {
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




}
