import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        MaxFibonacciHeap heap = new MaxFibonacciHeap();
        Map<String, Node> map = new HashMap<>();
        Map<String, Node> pqNode = new HashMap<>();
        PriorityQueue<Node> pqueue = new PriorityQueue<>((a, b) -> Integer.compare(b.value, a.value));

        Scanner scanner = new Scanner(new FileInputStream("src/sampleInput.txt"));
        int count = 0;
        while (scanner.hasNext()) {
            String input = scanner.next();
            if (input.startsWith("#")) {
                input = input.substring(1);
                int value = Integer.parseInt(scanner.next());

                if (pqNode.containsKey(input)) {
                    Node existing = pqNode.remove(input);
                    pqueue.remove(existing);
                    pqNode.put(input, new Node(input, existing.value + value));
                } else {
                    pqNode.put(input, new Node(input, value));
                }
                pqueue.add(pqNode.get(input));

//                System.out.println("#" + input + ", Value : " + value + ", Increase Key : " + map.containsKey(input));

                if (map.containsKey(input)) {
                    heap.increaseKey(map.get(input), value);
                } else {
                    count++;
                    Node node = new Node(input, value);
                    heap.insert(node);
                    map.put(input, node);
                }
            } else if (!input.equalsIgnoreCase("stop")) {
                int top = Integer.parseInt(input);
                System.out.println("Remove top " + Integer.parseInt(input));
                Set<Node> hashtags = new HashSet<>();
                while (top > 0) {
                    Node max = heap.removeMax();
                    hashtags.add(max);
                    System.out.print(max);
                    Node.reset(max);
                    System.out.print(", ");
                    top--;
                }

                System.out.println();

                System.out.println("Print from Queue");
                PriorityQueue<Node> newPqueue = new PriorityQueue<>((a, b) -> Integer.compare(b.value, a.value));
                while (!pqueue.isEmpty()) {
                    Node node = pqueue.poll();
                    System.out.print(node);
                    System.out.print(", ");
                    newPqueue.add(node);
                }
                pqueue = newPqueue;
                System.out.println();

                for (Node hashtag : hashtags) heap.insert(hashtag);
            }

            int heapSize = heap.getTotalNodes();
            if (heapSize != count)
                throw new RuntimeException("Node size changed, Expected : " + count + ", Found : " + heapSize);
        }
    }
}
