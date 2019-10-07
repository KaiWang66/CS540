import java.util.List;

public class AlphaBetaPruning {
    private static int visited;
    private static int evaluated;
    private static double value;
    private static int initialDepth;
    private static int maxDepth;
    private static int move;
    private static int effectiveSuccessors;
    private static int internalNode;
    public AlphaBetaPruning(int depth) {
        initialDepth = depth;
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        System.out.println("Move: " + move);
        System.out.printf("Value: %.1f\n", value);
        System.out.println("Number of Nodes Visited: " + visited);
        System.out.println("Number of Nodes Evaluated: " + evaluated);
        System.out.println("Max Depth Reached: " + maxDepth);
        System.out.printf("Avg Effective Branching Factor: %.1f\n", (double)(effectiveSuccessors)/internalNode);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        value = alphabeta(state, depth, alpha, beta, state.getCurPlayer());
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        maxDepth = Math.max(maxDepth, initialDepth - depth);
        visited++;
        List<GameState> next = state.getSuccessors();
        if (depth == 0 || next.size() == 0) {
            evaluated++;
            return state.evaluate();
        }
        internalNode++;
        double v = maxPlayer ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        for (GameState s : next) {
            effectiveSuccessors++;
            if (maxPlayer) {
                double min = alphabeta(s, depth - 1, alpha, beta, false);
                v = Math.max(v, min);
                if (v >= beta) {
                    return v;
                }
                if (alpha < v) {
                    alpha = v;
                    if (depth == initialDepth) {
                        move = s.getLastMove();
                    }
                }
            } else {
                double max = alphabeta(s, depth - 1, alpha, beta, true);
                v = Math.min(v, max);
                if (v <= alpha) {
                    return v;
                }
                if (v < beta) {
                    beta = v;
                    if (depth == initialDepth) {
                        move = s.getLastMove();
                    }
                }
            }
        }
        return v;
    }
}
