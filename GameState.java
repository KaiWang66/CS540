import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move
    private boolean curPlayer;   // The current player, true for 0, false for 1

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size, int nTaken) {

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;

        //Initialize curPlayer
        curPlayer = nTaken % 2 == 0;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
        this.curPlayer = other.curPlayer;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        List<Integer> moves = new ArrayList<>();
        if (lastMove == -1) {
            int maxOdd = size % 2 == 0 ? size / 2 - 1 : size / 2;
            for (int i = 1; i <= maxOdd; i++) {
                if (i % 2 == 1) {
                    moves.add(i);
                }
            }
            return moves;
        }
        int multiple = 1;
        for (int i = 1; i <= lastMove; i++) {
            if (stones[i] && lastMove % i == 0) {
                moves.add(i);
            }
        }

        while (multiple * lastMove <= size) {
            if (stones[multiple * lastMove]) {
                moves.add(multiple * lastMove);
            }
            multiple++;
        }
        return moves;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            state.curPlayer = !state.curPlayer;
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        List<Integer> moves = getMoves();
        if (moves.size() == 0) {
            return curPlayer ? -1 : 1;
        }
        double score;

        if (stones[1]) {
            score = 0;
        } else if (lastMove == 1) {
            score = moves.size() % 2 == 1 ? 0.5 : -0.5;
        } else if (Helper.isPrime(lastMove)) {
            int count = 0;
            for (int i : moves) {
                if (i % lastMove == 0) {
                    count++;
                }
            }
            score = count % 2 == 1 ? 0.7 : -0.7;
        } else {
            int largePrime = Helper.getLargestPrimeFactor(lastMove);
            int count = 0;
            for (int i : moves) {
                if (i % largePrime == 0) {
                    count++;
                }
            }
            score = count % 2 == 1 ? 0.6 : -0.6;
        }
        return curPlayer ? score : -score;
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

    public boolean getCurPlayer() {
        return curPlayer;
    }

}	
