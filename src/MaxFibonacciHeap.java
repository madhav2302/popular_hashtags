import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MaxFibonacciHeap {

    /**
     * Comparator used to find node having greater frequency of hashtag.
     * If frequency is same for both the hashtags, then it is ordered as lexicographical order of hashtags.
     */
    public static final Comparator<Node> COMPARATOR = (a, b) -> {
        if (a.frequency == b.frequency) return a.hashtag.compareTo(b.hashtag);
        return Integer.compare(a.frequency, b.frequency);
    };

    /**
     * Root node of heap
     */
    private Node root;

    /**
     * Inserts new node next to root element into existing fibonacci heap.
     * Also updates root, if node is greater than root.
     */
    public void insert(Node node) {
        root = meld(root, node);
    }

    /**
     * Joins two nodes and returns the pointer of max node b/w node1 and node2
     *
     * @param node1 1-2-3
     * @param node2 4-5-6
     * @return 1-4-5-6-2-3
     */
    private Node meld(Node node1, Node node2) {
        if (node1 == null) return node2;
        if (node2 == null) return node1;

        Node right = node1.right;
        Node left = node2.left;

        node1.right = node2;
        node2.left = node1;

        right.left = left;
        left.right = right;

        return COMPARATOR.compare(node1, node2) > 0 ? node1 : node2;
    }

    /**
     * Removes max and do a pairwise combine of remaining tree in the heap
     *
     * @return Max node in the heap
     */
    public Node removeMax() {
        if (root == null) throw new IllegalAccessError("Heap is empty");

        Map<Integer, Node> nodePerDegree = new HashMap<>();

        Node result = root;

        Node pairwiseCombineStartNode;
        if (root.right == root) {
            pairwiseCombineStartNode = root.child;
        } else {
            Node rightOfMax = root.right;
            removeFromSiblings(root);

            pairwiseCombineStartNode = meld(root.child, rightOfMax);
        }

        Node current = pairwiseCombineStartNode;
        root = null;
        if (current == null) return result;

        do {
            Node currentDegree = current;
            current = current.right;

            // Reset node config to consider as a single tree
            currentDegree.parent = null;
            currentDegree.left = currentDegree;
            currentDegree.right = currentDegree;
            currentDegree.childCut = false;

            // Pairwise combine of two nodes having same degree iteratively
            while (nodePerDegree.containsKey(currentDegree.degree)) {
                Node existingDegreeNode = nodePerDegree.remove(currentDegree.degree);

                if (COMPARATOR.compare(existingDegreeNode, currentDegree) > 0) {
                    existingDegreeNode.degree++;
                    existingDegreeNode.child = meld(existingDegreeNode.child != null ? existingDegreeNode.child.left : null, currentDegree);
                    currentDegree.parent = existingDegreeNode;
                    currentDegree = existingDegreeNode;
                } else {
                    currentDegree.degree++;
                    currentDegree.child = meld(currentDegree.child, existingDegreeNode);
                    existingDegreeNode.parent = currentDegree;
                }
            }

            nodePerDegree.put(currentDegree.degree, currentDegree);
        } while (current != pairwiseCombineStartNode);

        // Insert nodes based on degree
        for (Node node : nodePerDegree.values()) insert(node);

        return result;
    }

    /**
     * Removes a provided node from the heap.
     * It melds parent nodes on the top level through cascading cut.
     */
    public void remove(Node node) {
        if (node.right == node) {
            if (node.parent != null) {
                node.parent.child = null;
                node.parent.degree--;
            } else root = null;
        } else {
            if (node.parent != null) {
                node.parent.degree--;
                node.parent.child = node.right;
            }
            removeFromSiblings(node);
        }

        if (node.child != null) {
            Node child = node.child;
            Node current = child;

            do {
                current.parent = null;
                current.childCut = false;
                current = current.right;
            } while (current != child);

            meld(root, child);
        }

        // Cascading cut
        if (node.parent != null) {
            Node parent = node.parent;
            while (parent.childCut) parent = removeAndReturnParent(parent, false);
            parent.childCut = parent.parent != null;
        }
    }

    /**
     * Increases provided nodes by provided frequency.
     * If nodes frequency becomes greater than it's parent, then we implement cascading cut.
     * i.e. We will meld the node it's parent and so on on the top level until child cut for parents are true or
     * we reach at top level.
     */
    public void increaseKey(Node node, int frequency) {
        node.frequency += frequency;

        Node parent = node.parent;
        if (parent != null && node.frequency > node.parent.frequency) {
            parent = removeAndReturnParent(node, true);

            while (parent.childCut) parent = removeAndReturnParent(parent, true);

            parent.childCut = parent.parent != null;
        }

        if (COMPARATOR.compare(root, node) < 0) root = node;
    }

    /**
     * @param node      Removes node from the parent
     * @param meldAtTop adds it to the top level list if true
     */
    private Node removeAndReturnParent(Node node, boolean meldAtTop) {
        Node parent = node.parent;

        node.parent = null;
        node.childCut = false;
        parent.degree--;

        if (node.right == node) {
            parent.child = null;
        } else {
            parent.child = node.right;
            removeFromSiblings(node);
        }

        if (meldAtTop) root = meld(root, node);
        return parent;
    }

    /**
     * @param node removes node from it's siblings
     */
    private void removeFromSiblings(Node node) {
        Node left = node.left;
        Node right = node.right;

        left.right = right;
        right.left = left;

        node.left = node;
        node.right = node;
    }
}
