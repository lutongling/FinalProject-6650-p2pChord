import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Node extends Serializable, Remote {

  /**
   * Return the successor node for a given node id.
   * @param id of the given node
   * @return the successor node for a given node id
   * @throws RemoteException due to connecting via RMI
   */
  Node findSuccessor(int id) throws RemoteException;

  /**
   * Return the predecessor node for a given node id.
   * @param id of the given node
   * @return the predecessor node for a given node id
   * @throws RemoteException due to connecting via RMI
   */
  Node findPredecessor(int id) throws RemoteException;

  /**
   * Return the closest preceding finger of the given node id.
   * @param id of the given node
   * @return the closest preceding finger of the given node id
   * @throws RemoteException due to connecting via RMI
   */
  Node closestPrecedingFinger(int id) throws RemoteException;

  /**
   * This is called immediately after the join to initialize data structures specifically the finger
   * table entries to assist in routing.
   * @param node
   * @throws RemoteException
   */
  void initFingerTable(Node node) throws RemoteException;


  /**
   * The stabilize function is used to periodically verify the current nodes immediate successor and
   * tell the successor about itself
   * @throws RemoteException
   */
  void stabilize() throws RemoteException;

  /**
   * This function is used to periodically refresh finger table entries
   * @throws RemoteException
   */
  void fixFingers() throws RemoteException;

  // Yifan
  void notify(Node node) throws RemoteException;

  void join(Node node) throws RemoteException;

  // Zhixuan
  void updateOthers() throws RemoteException;

  void updateFingerTable(Node s, int i);

  // getters and setters

  int getId();

  void setId(int id);

  String getIpAddress();

  void setIpAddress(String ipAddress);

  int getPortNum();

  void setPortNum(int portNum);

  Node getSuccessor() throws RemoteException;

  void setSuccessor(Node node) throws RemoteException;

  Node getPredecessor() throws RemoteException;

  void setPredecessor(Node node) throws RemoteException;

}
