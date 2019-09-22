package hw1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<>();
		State start = new State(maze.getPlayerSquare(), null, 0, 0);
		StateFValuePair startFValuePair = new StateFValuePair(start, maze.getDistanceToGoal(maze.getPlayerSquare()));
		frontier.add(startFValuePair);
		HashMap<State, StateFValuePair> map = new HashMap<>();
		map.put(start, startFValuePair);

		while (!frontier.isEmpty()) {
			maxSizeOfFrontier = Math.max(maxSizeOfFrontier, frontier.size());
			StateFValuePair cur = frontier.poll();
			State s = cur.getState();
//			if (explored[s.getX()][s.getY()]) {
//				continue;
//			}
			explored[s.getX()][s.getY()] = true;
			noOfNodesExpanded++;
			if (cur.getState().isGoal(maze)) {
				return update(s);
			}
			ArrayList<State> neighbors = s.getSuccessors(explored, maze);
			for (State tmp : neighbors) {
				double f = tmp.getGValue() + maze.getDistanceToGoal(tmp.getSquare());
				StateFValuePair svp = map.get(tmp);
				if (svp == null) {
					svp = new StateFValuePair(tmp, f);
					frontier.offer(svp);
					explored[tmp.getX()][tmp.getY()] = true;
					map.put(tmp, svp);
				} else if (f < svp.getFValue()) {
					frontier.remove(svp);
					svp = new StateFValuePair(tmp, f);
					frontier.offer(svp);
					explored[tmp.getX()][tmp.getY()] = true;
					map.put(tmp, svp);
				}
			}
		}
		return false;
	}

}
