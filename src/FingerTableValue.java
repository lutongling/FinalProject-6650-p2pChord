/**
 * The encapsulation class of the finger table value for each Chord Node.
 * Each Chord node stores an array of FingerTableValue as its field.
 */
public class FingerTableValue {
  // start: id of the finger
  private int start;
  // the finger
  private Node node;

  /**
   * Dummy constructor. Never used for now but write here in case of usage.
   */
  public FingerTableValue() {
    this.start = 0;
    this.node = null;
  }

  /**
   * Construct a finger table value, which is the information the finger table maintains.
   *
   * @param start The start value (i.e. id of the finger in the finger table)
   * @param node  The finger (i.e. the node in the finger table)
   */
  public FingerTableValue(int start, Node node) {
    this.start = start;
    this.node = node;
  }

  /**
   * Get the start of this finger table value.
   *
   * @return the start of this finger table value
   */
  public int getStart() {
    return start;
  }

  /**
   * Set the start of this finger table value.
   *
   * @param start given start (finger id)
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * Get the finger of this finger table value.
   *
   * @return the finger of this finger table value
   */
  public Node getNode() {
    return node;
  }

  /**
   * Set the finger of this finger table value.
   *
   * @param node the given finger to be set in this finger table value
   */
  public void setNode(Node node) {
    this.node = node;
  }

}
