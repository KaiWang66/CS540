import java.util.ArrayList;
import java.util.List;

public class AlphaBetaPruning {
    private static int index;
    private static double value;
    private static boolean flag;
    private static int visited;
    private static int evaluate;
    private static int maxdepth;
    private static int initialDepth;
    private static int move;

    public AlphaBetaPruning(int initialDepth) {
        index = -1;
        visited = 0;
        evaluate = 0;
        maxdepth = 0;
        move = 0;
        this.initialDepth = initialDepth;
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        System.out.println("Move: " + move);
        System.out.println("Value: " + value);
        System.out.println("Number of Nodes Visited: " + visited);
        System.out.println("Number of Nodes Evaluated: " + evaluate);
        System.out.println("Max Depth Reached: " + maxdepth);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
        Double alpha = Double.NEGATIVE_INFINITY;
        Double beta = Double.POSITIVE_INFINITY;
        if (state.getCurPlayer() == 0) {
            value = max(state, alpha, beta, depth);
        } else {
            value = min(state, alpha, beta, depth);
        }
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @return int This is the number indicating score of the best next move
     */
//    private double alphabeta(GameState state, int depth, double alpha, double beta) {
//        maxdepth = Math.max(maxdepth, initialDepth - depth);
//        visited++;
//        List<GameState> successors = state.getSuccessors();
//        if ((depth == 0 && !flag) || successors.size() == 0) {
//            evaluate++;
//            return state.evaluate();
//        }
//
//        for (int i = 0; i < successors.size(); i++) {
//            GameState gs = successors.get(i);
//            double score = alphabeta(gs, depth - 1, alpha, beta);
//            if (state.getCurPlayer() == 0) {
//                if (beta < score) {
//                    return score;
//                }
//                if (score > alpha) {
//                    alpha = score;
//                    if (depth ==  initialDepth)
//                        move = gs.getLastMove();
//                }
//            } else {
//                if (alpha > score) {
//                    return score;
//                }
//                if (score < beta) {
//                    beta = score;
//                    if (depth ==  initialDepth)
//                        move = gs.getLastMove();
//                }
//            }
//        }
//        return state.getCurPlayer() == 0 ? alpha : beta;
//    }

    private double max(GameState state, double alpha, double beta, int depth) {
        maxdepth = Math.max(maxdepth, initialDepth - depth);
        visited++;
        List<GameState> successors = state.getSuccessors();
        if (depth == 0 || successors.size() == 0) {
            evaluate++;
            return state.evaluate();
        }
        double v = Double.NEGATIVE_INFINITY;
        for (GameState gs : successors) {
            double min = min(gs, alpha, beta, depth - 1);
            v = Math.max(v, min);
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    private double min(GameState state, double alpha, double beta, int depth) {
        maxdepth = Math.max(maxdepth, initialDepth - depth);
        visited++;
        List<GameState> successors = state.getSuccessors();
        if (depth == 0 || successors.size() == 0) {
            evaluate++;
            return state.evaluate();
        }
        double v = Double.POSITIVE_INFINITY;
        for (GameState gs : successors) {
            double max = max(gs, alpha, beta, depth - 1);
            v = Math.min(v, max);
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

}
