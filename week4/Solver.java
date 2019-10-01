/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {
    private static final Comparator<SearchNode> MIN_COMPARATOR =
            (b1, b2) -> (b1.board.manhattan() + b1.moves) - (b2.board.manhattan() + b2.moves);
    private final MinPQ<SearchNode> boards;
    private final MinPQ<SearchNode> twinBoards;
    private SearchNode goalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.boards = new MinPQ<>(MIN_COMPARATOR);
        this.twinBoards = new MinPQ<>(MIN_COMPARATOR);
        this.goalNode = null;
        this.boards.insert(new SearchNode(initial, null, 0));
        this.twinBoards.insert(new SearchNode(initial.twin(), null, 0));
        this.solve();
    }

    private void solve() {
        SearchNode node = null,
                twinNode = null;
        while (!boards.isEmpty() && !twinBoards.isEmpty()) {
            node = boards.delMin();
            if (node.board.isGoal()) {
                break;
            }
            visit(node, boards);

            twinNode = twinBoards.delMin();
            if (twinNode.board.isGoal()) {
                break;
            }
            visit(twinNode, twinBoards);
        }

        if (node != null && node.board.isGoal()) {
            this.goalNode = node;
        }
    }

    private void visit(SearchNode node, MinPQ<SearchNode> pq) {
        node.board.neighbors().forEach(nb -> {
            if (node.previous == null || !node.previous.board.equals(nb)) {
                SearchNode snb = new SearchNode(nb, node, node.moves + 1);
                pq.insert(snb);
            }
        });
    }

    // test client (see below)
    public static void main(String[] args) {

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return goalNode != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        return goalNode != null ? goalNode.moves : 0;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> sol = new Stack<>();
        SearchNode node = goalNode;
        while (node != null) {
            sol.push(node.board);
            node = node.previous;
        }
        return sol;
    }

    private static class SearchNode {
        private final Board board;
        private final SearchNode previous;
        private final int moves;

        public SearchNode(Board board, SearchNode previous, int moves) {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
        }
    }
}
