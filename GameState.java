import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move
    private int curPlayer;       // The current player, 0 for Max, 1 for min
    private int nTaken;

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

        // Set the current player
        curPlayer = (nTaken & 1) == 0 ? 0 : 1;
        this.nTaken = nTaken;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
        this.curPlayer = other.curPlayer;
        this.nTaken = other.nTaken;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        List<Integer> moves = new ArrayList<>();
        if (this.lastMove == -1) {
            for (int i = 1; 2 * i < size; i++) {
                if ((i % 2) == 1) {
                    moves.add(i);
                }
            }
            return moves;
        }

        for (int i = 2; i * lastMove <= size; i++) {
            if (stones[i * lastMove]) {
                moves.add(i * lastMove);
            }
        }

        if (!Helper.isPrime(lastMove)) {
            for (int i = 1; i < lastMove; i++) {
                if (stones[i] && lastMove % i == 0) {
                    moves.add(i);
                }
            }
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
            state.curPlayer = 1 - state.curPlayer;
            state.nTaken++;
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
        List<GameState> nextMoves = this.getSuccessors();
        if (nextMoves.size() == 0) {
            return curPlayer == 0 ? -1 : 1;
        }
        if (stones[1]) {
            return 0;
        }
        double score;

        if (lastMove == 1) {
            score = (nextMoves.size() % 2) == 1 ? 0.5 : -0.5;
        } else if (Helper.isPrime(lastMove)) {
            int count = 0;
            for (GameState gs : nextMoves) {
                if (gs.lastMove % lastMove == 0) {
                    count++;
                }
            }
            score = (count % 2) == 0 ? -0.7 : 0.7;
        } else {
            int largestPrime = Helper.getLargestPrimeFactor(lastMove);
            int count = 0;
            for (GameState gs : nextMoves) {
                if (gs.lastMove % largestPrime == 0) {
                    count++;
                }
            }
            score = (count % 2) == 0 ? -0.6 : 0.6;
        }
        return curPlayer == 0 ? score : -score;
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

    public int getCurPlayer() {
        return this.curPlayer;
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


}	
