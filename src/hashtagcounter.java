import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class hashtagcounter {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) throw new IllegalArgumentException("Expected input file");
        String inputFile = args[0];

        if (args.length == 2) System.setOut(new PrintStream(new File(args[1])));

        MaxFibonacciHeap heap = new MaxFibonacciHeap();
        Map<String, Node> map = new HashMap<>();

        Scanner scanner = new Scanner(new FileInputStream(inputFile));
        while (scanner.hasNext()) {
            String input = scanner.next();

            // Hashtag found
            if (input.startsWith("#")) {
                String hashtag = input.substring(1);
                int value = Integer.parseInt(scanner.next());

                if (map.containsKey(hashtag)) {
                    heap.increaseKey(map.get(hashtag), value);
                } else {
                    Node node = new Node(hashtag, value);
                    heap.insert(node);
                    map.put(hashtag, node);
                }
            }
            // Stops the program
            else if (input.equalsIgnoreCase("stop")) {
                return;
            }
            // Otherwise, we need to remove top n elements
            else {
                int top = Integer.parseInt(input);
                Set<Node> hashtags = new HashSet<>();

                while (top > 0) {
                    Node max = heap.removeMax();
                    hashtags.add(max);

                    System.out.print(max.hashtag);
                    if (top > 1) System.out.print(",");

                    Node.reset(max);
                    top--;
                }

                System.out.println();

                for (Node hashtag : hashtags) heap.insert(hashtag);
            }
        }
    }
}
