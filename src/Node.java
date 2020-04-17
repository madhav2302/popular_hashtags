public class Node {

    /**
     * Hashtag of the node
     */
    String hashtag;

    /**
     * Frequency of the hashtag
     */
    int frequency;

    /**
     * Degree of the tree
     */
    int degree = 0;

    /**
     * Points to parent of the node, null if it is on top level
     */
    Node parent;

    /**
     * Points to child of the node, null if it have no child
     */
    Node child;

    /**
     * Used to maintain doubly-linked list structure, points to left sibling
     */
    Node left;

    /**
     * Used to maintain doubly-linked list structure, points to right sibling
     */
    Node right;

    /**
     * Used to store if any child was cut and used while performing cascading cut.
     */
    boolean childCut = false;

    public Node(String hashtag, int value) {
        this.hashtag = hashtag;
        this.frequency = value;

        left = this;
        right = this;
    }

    public static void reset(Node node) {
        node.left = node;
        node.right = node;
        node.degree = 0;
        node.childCut = false;
        node.child = null;
        node.parent = null;
    }

    @Override
    public String toString() {
        return "{" +
                "hashtag='" + hashtag + '\'' +
                ", value=" + frequency +
                ", degree=" + degree +
                ", childCut=" + childCut +
                '}';
    }
}
