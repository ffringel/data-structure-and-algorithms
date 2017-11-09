import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private static final Comparator<Node> MANHATTAN = new ManhattanOrder();

    private MinPQ<Node> pq = new MinPQ<Node>(MANHATTAN);
    private MinPQ<Node> pq2 = new MinPQ<Node>(MANHATTAN);

    private Node current;
    private Node current2;

    private boolean solvable;

    public Solver(Board initial) {
        current = new Node(initial, null, 0);
        current2 = new Node(initial.twin(), null, 0);

        pq.insert(current);
        pq2.insert(current2);

        while (!current.board.isGoal() && !current2.board.isGoal()) {
            current = pq.delMin();
            current2 = pq2.delMin();

            for (Board i : current.board.neighbors()) {
                if (current.parent == null || !i.equals(current.parent.board))
                    pq.insert(new Node(i, current, current.move + 1));
            }

            for (Board i : current2.board.neighbors()) {
                if (current2.parent == null || !i.equals(current2.parent.board))
                    pq2.insert(new Node(i, current2, current2.move + 1));
            }
        }

        if (current.board.isGoal())
            solvable = true;
        else
            solvable = false;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (solvable)
            return current.move;
        return -1;
    }

    public Iterable<Board> solution() {
        if (!solvable)
            return null;

        Stack<Board> q = new Stack<Board>();
        Node i = current;

        while (i != null) {
            q.push(i.board);
            i = i.parent;
        }

        return q;
    }

    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("Must specify a file path as the first argument");

        In in = new In(args[0]);
        int N = in.readInt();

        if (N <= 0)
            throw new IndexOutOfBoundsException("Board dimension must be greater than 0.");

        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        }
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class Node implements Comparable<Node> {
        private Board board;
        private Node parent;
        private int move, priority;

        public Node(Board b, Node p, int m) {
            board = b;
            parent = p;
            move = m;
            priority = b.manhattan() + m;
        }

        @Override
        public int compareTo(Node other) {
            if (this.priority < other.priority)
                return -1;
            if (this.priority > other.priority)
                return 1;

            return 0;
        }
    }

    private static class ManhattanOrder implements Comparator<Node> {

        @Override
        public int compare(Node v, Node w) {
            return Integer.compare(v.priority, w.priority);
        }
    }
}
