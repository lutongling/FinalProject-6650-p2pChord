import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface represents the behaviors of a Chord Node, which are implemented in its subclasses,
 * AbstractNode, and NodeImpl.
 *
 * NOTICE: To read the documentation more easily, let's assume:
 * n  :  this(java key word) node, the current object who calls the methods
 * n' :  the other node, which is usually by given information in the methods' parameters.
 */
public interface Node extends Serializable, Remote {

  /**
   * By asking this node(n), return the successor node for a given node(n') by the given id of n'.
   * This method calls findPredecessor(int id) inside its implementation body.
   *
   * @param id of the give node(n')
   * @return the successor node for node(n')
   * @throws RemoteException due to connecting via RMI
   */
  Node findSuccessor(int id) throws RemoteException;

  /**
   * By asking this node(n), return the predecessor node for a given node(n') by the given id of n'.
   * This method calls closestPrecedingFinger(int id) inside its implementation body.
   *
   * @param id of the given node(n')
   * @return the predecessor node for node(n')
   * @throws RemoteException due to connecting via RMI
   */
  Node findPredecessor(int id) throws RemoteException;

  /**
   * Return the closest preceding finger of the node(n'), by given its id.
   *
   * @param id of the given node(n')
   * @return the closest preceding finger for node(n')
   * @throws RemoteException due to connecting via RMI
   */
  Node closestPrecedingFinger(int id) throws RemoteException;

  /**
   * NOTICE:
   * This is a method which does not handling concurrency and failures, which was used previously.
   * But in the current system which is able to handle concurrency and failures by self-stabilization
   * and fixing fingers, we don't use it anymore.
   *
   * Initialize finger table of local node by a given node(n') that already in the network.
   *
   * @param node the given node(n') that already in the network.
   * @throws RemoteException due to connecting via RMI
   */
  void initFingerTable(Node node) throws RemoteException;

  /**
   * The stabilize function is used to periodically verify the nodes immediate successor and
   * tell the successor about itself.
   *
   * This is the self-stabilization for the system. This is how newly-joined nodes are noticed by
   * the network, and how concurrency and failures are being handled.
   *
   * @throws RemoteException due to connecting via RMI
   */
  void stabilize() throws RemoteException;

  /**
   * This function is used to periodically refresh finger table entries.
   *
   * It is also used for handling concurrency and failures.
   *
   * @throws RemoteException due to connecting via RMI
   */
  void fixFingers() throws RemoteException;

  /**
   * To notify this node(n)'s successor of the existence of n itself in the network, giving the
   * successor the chance to change its predecessor to the given node n'.
   *
   * The given node n' represents the potential predecessor of n's successor.
   *
   * This method is called at the end in stabilize.
   *
   * @param node
   * @throws RemoteException
   */
  void notifyNode(Node node) throws RemoteException;

  /**
   * A node(n) tries to join the system by a given known node(n') that already/has been in the system.
   * This function is able to handle concurrent joins.
   *
   * Join does not make the rest of the network aware of itself. This is achieved by stabilize
   * method, which is calling the notify method.
   *
   * @param node the given known node
   * @throws RemoteException due to connecting via RMI
   */
  void join(Node node) throws RemoteException;

  /**
   * NOTICE:
   * This is a method which does not handling concurrency and failures, which was used previously.
   * But in the current system which is able to handle concurrency and failures by self-stabilization
   * and fixing fingers, we don't use it anymore.
   *
   * Update all nodes whose finger tables should refer to this node(n)
   *
   * @throws RemoteException due to connecting via RMI
   */
  void updateOthers() throws RemoteException;

  /**
   * NOTICE:
   * This is a method which does not handling concurrency and failures, which was used previously.
   * But in the current system which is able to handle concurrency and failures by self-stabilization
   * and fixing fingers, we don't use it anymore.
   *
   * It is called by updateOthers() above.
   *
   * If s is i-th finger of this node(n), update n's finger table with s.
   *
   * @param s given node s
   * @param i given index in the finger table of this node(n)
   * @throws RemoteException due to connecting via RMI
   */
  void updateFingerTable(Node s, int i) throws RemoteException;

  /**
   * To echo the given newly joined Node a message.
   *
   * @param joinedNode the given newly joined node
   * @throws RemoteException due to connecting via RMI
   */
  void echo(Node joinedNode) throws RemoteException;

  /**
   * Create finger table for node, called in main(program entry) after creating a node.
   *
   * @throws RemoteException due to connecting via RMI
   */
  void createFingerTable() throws RemoteException;

  /**
   * Get the id of this node.
   *
   * @return the id of this node
   * @throws RemoteException due to connecting via RMI
   */
  int getId() throws RemoteException;

  /**
   * Set the id of this node.
   *
   * @param id the given id to be set of this node
   * @throws RemoteException due to connecting via RMI
   */
  void setId(int id) throws RemoteException;

  /**
   * Get the ip address of this node.
   *
   * @return the ip address of this node
   * @throws RemoteException due to connecting via RMI
   */
  String getIpAddress() throws RemoteException;

  /**
   * Set the ip address of this node.
   *
   * @param ipAddress the given ip address to be set of this node
   * @throws RemoteException due to connecting via RMI
   */
  void setIpAddress(String ipAddress) throws RemoteException;

  /**
   * Get the port number of this node.
   *
   * @return the port number of this node
   * @throws RemoteException due to connecting via RMI
   */
  int getPortNum() throws RemoteException;

  /**
   * Set the port number of this node.
   *
   * @param portNum the given port number to be set of this node
   * @throws RemoteException due to connecting via RMI
   */
  void setPortNum(int portNum) throws RemoteException;

  /**
   * Get the successor of this node.
   *
   * @return the successor of this node
   * @throws RemoteException due to connecting via RMI
   */
  Node getSuccessor() throws RemoteException;

  /**
   * Set the successor of this node.
   *
   * @param node the given successor node to be set of this node
   * @throws RemoteException due to connecting via RMI
   */
  void setSuccessor(Node node) throws RemoteException;

  /**
   * Get the predecessor of this node.
   *
   * @return the predecessor of this node
   * @throws RemoteException due to connecting via RMI
   */
  Node getPredecessor() throws RemoteException;

  /**
   * Set the predecessor of this node.
   *
   * @param node the given predecessor node to be set of this node
   * @throws RemoteException due to connecting via RMI
   */
  void setPredecessor(Node node) throws RemoteException;

  /**
   * Return the value from the key-value storage by the given key.
   *
   * @param key the given key.
   * @return the value from the key-value storage by the given key
   * @throws RemoteException due to connecting via RMI
   */
  String getFromStorage(String key) throws RemoteException;

  /**
   * Put the key-value pair to the storage by given key-value pair.
   *
   * @param key   the given key
   * @param value the given value
   * @throws RemoteException due to connecting via RMI
   */
  void putToStorage(String key, String value) throws RemoteException;

  /**
   * The simulation of a simple "client-side" that reads input from user to perform operations.
   *
   * This is being called in main(the entry point of the program).
   *
   * @throws RemoteException due to connecting via RMI
   */
  void consistentStore() throws RemoteException;
}
