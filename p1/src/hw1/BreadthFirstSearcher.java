package hw1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		Queue<State> queue = new ArrayDeque<>();
		queue.add(new State(maze.getPlayerSquare(), null, 0, 0));
		while (!queue.isEmpty()) {
			maxSizeOfFrontier = Math.max(maxSizeOfFrontier, queue.size());
			State cur = queue.poll();
//			if (explored[cur.getX()][cur.getY()]) {
//				continue;
//			}
			explored[cur.getX()][cur.getY()] = true;
			noOfNodesExpanded++;
			if (cur.isGoal(maze)) {
				return update(cur);
			}
			ArrayList<State> successors = cur.getSuccessors(explored, maze);
			for (State s : successors) {
				if (!queue.contains(s) && !explored[s.getX()][s.getY()]) {
					explored[s.getX()][s.getY()] = true;
					queue.offer(s);
				}
			}
		}
		return false;
	}
}
