import java.util.*;

class EightPuzzleState {
    int[][] cells;
    int blankRow;
    int blankCol;
    
    public EightPuzzleState(int[] numbers) {
	cells = new int[3][3];
	int count = 0;
	for(int row = 0; row < 3; row++)
	    for(int col = 0; col < 3; col++) {
		cells[row][col] = numbers[count];
		if(cells[row][col] == 0) {
		    blankRow = row;
		    blankCol = col;
		}
		count++;
	    }
    }

    public static EightPuzzleState goalState() {
	//int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 0};
	int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8};
	//int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1, 0};
	return new EightPuzzleState(numbers);
    }

    public boolean isGoal() {
	return this.equals(goalState());
    }

    public List<String> legalMoves() {
	List<String> moves = new ArrayList<String>();

	if(blankRow != 0)
	    moves.add("up");
	if(blankRow != 2)
	    moves.add("down");
	if(blankCol != 0)
	    moves.add("left");
	if(blankCol != 2)
	    moves.add("right");

	return moves;
    }

    public EightPuzzleState result(String move) {
	int row = blankRow;
	int col = blankCol;
	int newrow = row, newcol = col;
	if(move.equals("up")) {
	    newrow = row - 1;
	    newcol = col;
	}
	else if(move.equals("down")) {
	    newrow = row + 1;
	    newcol = col;
	}
	else if(move.equals("left")) {
	    newrow = row;
	    newcol = col - 1;
	}
	else if(move.equals("right")) {
	    newrow = row;
	    newcol = col + 1;
	}

	int[] numbers = {0, 0, 0, 0, 0, 0, 0, 0, 0};
	EightPuzzleState newPuzzle = new EightPuzzleState(numbers);
	for(int i = 0; i < 3; i++)
	    newPuzzle.cells[i] = cells[i].clone();

	newPuzzle.cells[row][col] = cells[newrow][newcol];
	newPuzzle.cells[newrow][newcol] = cells[row][col];
	newPuzzle.blankRow = newrow;
	newPuzzle.blankCol = newcol;

	return newPuzzle;
    }

    @Override public boolean equals(Object other) {
	for(int row = 0; row < 3; row++)
	    for(int col = 0; col < 3; col++)
		if(((EightPuzzleState)other).cells[row][col] != cells[row][col])
		    return false;
	return true;
    }
    
    @Override public int hashCode() {
	return this.toString().hashCode();
    }

    @Override public String toString() {
	String str = new String();
	for(int row = 0; row < 3; row++) {
	    for(int col = 0; col < 3; col++)
		str += cells[row][col] + " ";
	    str += "\n";
	}
	return str;
    }

    public static EightPuzzleState shuffle(EightPuzzleState puzzle) {
	for(int i = 0; i < 100; i++) {
	    List<String> legalMoves = puzzle.legalMoves();
	    Random r = new Random();
	    int index = r.nextInt(legalMoves.size());
	    String move = legalMoves.get(index);
	    puzzle = puzzle.result(move);
	}
	return puzzle;
    }
}

class Successor {
    EightPuzzleState state;
    String action;
    int cost;

    public Successor(EightPuzzleState state, String action, int cost) {
	this.state = state;
	this.action = action;
	this.cost = cost;
    }
}

class Node {
    EightPuzzleState state;
    Node parent;
    String action;
    int pathCost;

    public Node(EightPuzzleState state, Node parent, String action, int pathCost) {
	this.state = state;
	this.parent = parent;
	this.action = action;
	this.pathCost = pathCost;
    }

    public List<Node> expand(EightPuzzleSearchProblem problem) {
	List<Node> ns = new ArrayList<Node>();
	Node node = this;
	for(Successor successor : problem.getSuccessors(node.state)) {
	    ns.add(new Node(successor.state, node, successor.action, node.pathCost + successor.cost));
	}	
	return ns;
    }
}

class PriorityNode {
    int priority;
    Node node;

    public PriorityNode(int priority, Node node) {
	this.priority = priority;
	this.node = node;
    }
}

class EightPuzzleSearchProblem {
    EightPuzzleState puzzle;

    public EightPuzzleSearchProblem(EightPuzzleState puzzle) {
	this.puzzle = puzzle;
    }

    public EightPuzzleState getStartState() {
	return puzzle;
    }

    public boolean isGoalState(EightPuzzleState state) {
	return state.isGoal();
    }

    public List<Successor> getSuccessors(EightPuzzleState state) {
	List<Successor> successors = new ArrayList<Successor>();
	for(String action : state.legalMoves())
	    successors.add(new Successor(state.result(action), action, 1));
	return successors;
    }

    public int getCostOfActions(List<String> actions) {
	return actions.size();
    }

    public static List<String> solution(Node node) {
	List<String> solList = new ArrayList<String>();
	if(node.parent == null) return new ArrayList<String>();
	while(node.parent != null) {
	    solList.add(node.action);
	    node = node.parent;
	}
	Collections.reverse(solList);
	return solList;
    }
    
    public List<String> breadthFirstSearch() {
	Queue<Node> fringe = new LinkedList<Node>();
	EightPuzzleSearchProblem problem = this;
	Set<EightPuzzleState> closed = new HashSet<EightPuzzleState>();
	int expandedNodeCount = 1;

	fringe.add(new Node(problem.getStartState(), null, null, 0));
	while(!fringe.isEmpty()) {
	    Node node = fringe.remove();
	    if(problem.isGoalState(node.state)) {
		System.out.println("Expanded node count: " + expandedNodeCount);
		return solution(node);
	    }
	    if(!closed.contains(node.state)) {
		closed.add(node.state);
		for(Node newNode : node.expand(problem)) {
		    fringe.add(newNode);
		    expandedNodeCount++;
		}
	    }
	}
	return new ArrayList<String>();
    }

    class Point {
	int x, y;

	public Point(int x, int y) {
	    this.x = x;
	    this.y = y;
	}

	public int manhatanDistance(int otherX, int otherY) {
	    return Math.abs(x - otherX) + Math.abs(y - otherY);
	}
    }

    private int heuristic(EightPuzzleState state) {
	int h = 0;
	EightPuzzleState goalState = EightPuzzleState.goalState();
	Map<Integer, Point> map = new HashMap<Integer, Point>();

	for(int row = 0; row < 3; row++)
	    for(int col = 0; col < 3; col++)
		if(goalState.cells[row][col] != 0)
		    map.put(goalState.cells[row][col], new Point(row, col));
	for(int row = 0; row < 3; row++)
	    for(int col = 0; col < 3; col++) {
		if(state.cells[row][col] != 0)
		    h += map.get(state.cells[row][col]).manhatanDistance(row, col);
	    }
	return h;
    }

    public List<String> aStarSearch() {
	PriorityQueue<PriorityNode> fringe = new PriorityQueue<PriorityNode>(1, new Comparator<PriorityNode>() {
            public int compare(PriorityNode a, PriorityNode b){
                if (a.priority > b.priority) return 1;
                else if (a.priority < b.priority) return -1;
		return 0;
            }
	});
	int expandedNodeCount = 1;
	EightPuzzleSearchProblem problem = this;
	Set<EightPuzzleState> closed = new HashSet<EightPuzzleState>();

	fringe.add(new PriorityNode(1, new Node(problem.getStartState(), null, null, 0)));
	while(!fringe.isEmpty()) {
	    Node node = fringe.remove().node;
	    if(problem.isGoalState(node.state)) {
		System.out.println("Expanded node count: " + expandedNodeCount);
		return solution(node);
	    }
	    if(!closed.contains(node.state)) {
		closed.add(node.state);
		for(Node newNode : node.expand(problem)) {
		    fringe.add(new PriorityNode(node.pathCost + heuristic(newNode.state), newNode));
		    expandedNodeCount++;
		}
	    }
	}
	return new ArrayList<String>();
    }
}

public class EightPuzzle {
    public static void main(String[] args) throws InterruptedException {
	int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8};
	EightPuzzleState puzzle = new EightPuzzleState(numbers);
	puzzle = EightPuzzleState.shuffle(puzzle);
	System.out.println("Puzzle");
	System.out.println(puzzle);
	System.out.println("Goal State");
	System.out.println(EightPuzzleState.goalState());
	final EightDrawer drawer = new EightDrawer(puzzle);
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		drawer.createAndShowGUI();
            }
        });
	Thread.sleep(100);
	EightPuzzleSearchProblem problem = new EightPuzzleSearchProblem(puzzle);
	List<String> solution;

	// Bread Frirst Search
	System.out.println("Bread First Search Solution:");
	solution = problem.breadthFirstSearch();
	System.out.println("Moves: " + solution.size());
	System.out.println(solution);

	System.out.println();

	// A* Search
	System.out.println("A* Search Solution:");
	solution = problem.aStarSearch();
	System.out.println("Moves: " + solution.size());
	System.out.println(solution);

	for(String action : solution) {
	    Thread.sleep(500);
	    if(action.equals("left"))
		drawer.moveLeft();
	    if(action.equals("up"))
		drawer.moveUp();
	    if(action.equals("right"))
		drawer.moveRight();
	    if(action.equals("down"))
		drawer.moveDown();
	}
    }
}
