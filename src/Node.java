import java.io.Serializable;
import java.rmi.Remote;

public interface Node extends Serializable, Remote {

  Node findSuccessor(long id);

  Node findPredecessor(long id);

  Node closestPrecedingFinger(long id);

  void initFingerTable(Node node);

  // Tonglingling
  void stabilize();

  void fixFingers();

  // Yifan
  void notify(Node node);

  void join(Node node);

  // Zhixuan
  void updateOthers();

  void updateFingerTable(Node s, long i);




}
