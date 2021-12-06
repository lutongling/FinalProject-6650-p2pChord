public class FingerTableValue {

  private int start;
  private Node successor;

  /**
   * Dummy constructor.
   */
  public FingerTableValue() {
    this.start = 0;
    this.successor = null;
  }

  /**
   * Construct a finger table value, which is the information the finger table maintains.
   * @param start The start value for each finger table value
   * @param successor The immediate next node on the identifier circle(i.e. the first finger)
   */
  public FingerTableValue(int start, Node successor) {
    this.start = start;
    this.successor = successor;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public Node getSuccessor() {
    return successor;
  }

  public void setSuccessor(Node successor) {
    this.successor = successor;
  }


}
