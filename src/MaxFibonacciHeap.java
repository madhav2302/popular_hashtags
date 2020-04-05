import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaxFibonacciHeap {

    Set<String> hashtags = new HashSet<>();
    private Node max;

    public void insert(Node node) {
        hashtags.add(node.hashtag);
        if (max == null) max = node;
        else max = meld(max, node);
    }

    private Node meld(Node node1, Node node2) {
        if (node1 == null) return node2;
        if (node2 == null) return node1;

        Node right = node1.right;
        Node left = node2.left;

        node1.right = node2;
        node2.left = node1;

        right.left = left;
        left.right = right;

        return node1.value < node2.value ? node2 : node1;
    }

    public Node removeMax() {
        Map<Integer, Node> map = new HashMap<>();

        Node result = max;

        Node startPairwiseCombineFromHere;
        if (max.right == max) {
            startPairwiseCombineFromHere = max.child;
        } else {
            Node rightOfMax = max.right;
            mergeLeftRight(max);

            startPairwiseCombineFromHere = meld(max.child != null ? max.child.left : null, rightOfMax);
        }

        Node current = startPairwiseCombineFromHere;

        do {
            Node currentDegree = current;
            current = current.right;
            currentDegree.parent = null;
            currentDegree.left = currentDegree;
            currentDegree.right = currentDegree;

            while (map.containsKey(currentDegree.degree)) {
                Node sameDegreeNode = map.remove(currentDegree.degree);

                if (sameDegreeNode.value > currentDegree.value) {
                    sameDegreeNode.degree++;
                    sameDegreeNode.child = meld(sameDegreeNode.child != null ? sameDegreeNode.child.left : null, currentDegree);
                    currentDegree.parent = sameDegreeNode;
                    currentDegree = sameDegreeNode;
                } else {
                    currentDegree.degree++;
                    currentDegree.child = meld(currentDegree.child, sameDegreeNode);
                    sameDegreeNode.parent = currentDegree;
                }
            }

            map.put(currentDegree.degree, currentDegree);
        } while (current != startPairwiseCombineFromHere);

        max = null;

        for (Node node : map.values()) insert(node);

        return result;
    }

    // TODO
    public void remove(Node node) {

    }

    public void increaseKey(Node node, int value) {
        node.value += value;

        Node parent = node.parent;
        if (parent != null && node.value > node.parent.value) {
            parent = removeAndReturnParent(node);

            // TODO
            while (parent.parent != null && parent.childCut) parent = removeAndReturnParent(parent);

            parent.childCut = parent.parent != null;
        }

        if (max.value < node.value) max = node;
    }

    public int getTotalNodes() {
        return getTotalNodes(max);
    }


    private int getTotalNodes(Node node) {
        if (node == null) return 0;
        int totalNodes = 0;

        Node current = node;

        do {
            totalNodes++;
            totalNodes += getTotalNodes(current.child);
            current = current.right;
        } while (current != node);

        return totalNodes;
    }

    private Node removeAndReturnParent(Node node) {
        Node parent = node.parent;

        node.parent = null;
        node.childCut = false;
        parent.degree--;

        if (node.right == node) {
            parent.child = null;
        } else {
            parent.child = node.right;
            mergeLeftRight(node);
        }

        max = meld(max, node);
        return parent;
    }

    private void mergeLeftRight(Node node) {
        Node left = node.left;
        Node right = node.right;

        left.right = right;
        right.left = left;

        node.left = node;
        node.right = node;
    }

    public void print() {
        if (max != null) System.out.println("Max is : " + max);
        int totalNodes = print(max, false);
        System.out.println("Total Nodes are : " + totalNodes);
    }

    private int print(Node node, boolean previousWasMax) {
        if (node == null) return 0;

        int totalNodes = 0;

        Node current = node;

        do {
            totalNodes++;
            if (previousWasMax) System.out.print("( ");
            System.out.print(current);
            totalNodes += print(current.child, node == max);
            current = current.right;
            if (previousWasMax) System.out.print(" )");

            if (node == max) {
                System.out.println();
                System.out.println();
            }

        } while (current != node);

        return totalNodes;
    }
}
