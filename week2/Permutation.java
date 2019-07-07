import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (int i=0;i<k;i++) {
            String element = StdIn.readString();
            queue.enqueue(element);
        }
        for (String element: queue) {
            StdOut.println(element);
        }
    }
}
