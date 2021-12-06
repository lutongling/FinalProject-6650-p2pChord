public class FingerTableValue {

  private int start;
  private Node node;

  /**
   * Dummy constructor.
   */
  public FingerTableValue() {
    this.start = 0;
    this.node = null;
  }

  /**
   * Construct a finger table value, which is the information the finger table maintains.
   * @param start The start value for each finger table value
   * @param successor The immediate next node on the identifier circle(i.e. the first finger)
   */
  public FingerTableValue(int start, Node successor) {
    this.start = start;
    this.node = successor;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }


}
