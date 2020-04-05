public class Node {

    String hashtag;
    int value;

    int degree = 0;
    Node parent;
    Node child;
    Node left;
    Node right;
    boolean childCut = false;

    public Node(String hashtag, int value) {
        this.hashtag = hashtag;
        this.value = value;

        left = this;
        right = this;
    }

    public static Node create(String hashtag, int value) {
        return new Node(hashtag, value);
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
                ", value=" + value +
                ", degree=" + degree +
                ", childCut=" + childCut +
                '}';
    }
}
