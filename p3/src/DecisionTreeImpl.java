import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(trainData, 0);
	}

	private double calc(double i) {
		if (i == 0) return 0;
		return (double)(-i) * (Math.log(i) / Math.log(2));
	}

	private int getClass(List<List<Integer>> data) {
		int zero = 0;
		int one = 0;
		for (List<Integer> list : data) {
			if (list.get(list.size() - 1) == 0) zero++;
			else one++;

		}
		return zero > one ? 0 : 1;
	}

	private double getEntropy(List<List<Integer>> data) {
		double zero = 0;
		double one = 0;
		for (List<Integer> list : data) {
			if (list.get(list.size() - 1) == 0) zero++;
			else one++;

		}
		double prZero = zero / (zero + one);
		double prOne = one / (zero + one);
		return calc(prOne) + calc(prZero);
	}


	private List<List<List<Integer>>> Partition(List<List<Integer>> data, int attribute, int threshold) {
		List<List<Integer>> left = new ArrayList<>();
		List<List<Integer>> right = new ArrayList<>();
		for (List<Integer> list : data) {
			if (list.get(attribute) <= threshold) {
				left.add(list);
			} else {
				right.add(list);
			}
		}
		List<List<List<Integer>>> partition = new ArrayList<>();
		partition.add(left);
		partition.add(right);
		return partition;
	}

	private DecTreeNode bestAttribute(List<List<Integer>> data) {
		double entropy = getEntropy(data);
		double loss = Double.MAX_VALUE;
		int bestAttribute = -1;
		int threshold = 0;
		for (int i = 0; i < numAttr; i++) { // Attribute
			for (int j = 1; j <= 10; j++) { // Threshold
				List<List<List<Integer>>> partition = Partition(data, i, j);
				List<List<Integer>> left = partition.get(0);
				List<List<Integer>> right = partition.get(1);
				double p = 0;
				if (left.size() != 0) {
					p += getEntropy(left) * left.size() / (left.size() + right.size());
				}
				if (right.size() != 0) {
					p += getEntropy(right) * right.size() / (left.size() + right.size());
				}
				if (p < loss) {
					loss = p;
					bestAttribute = i;
					threshold = j;
				}
			}
		}
		if (loss == entropy) {
			return new DecTreeNode(-2, bestAttribute, threshold);
		}
		if (bestAttribute == -1) {
			System.out.println("Warning : wrong value of attribute!");
		}
		return new DecTreeNode(-1, bestAttribute, threshold);
	}

	private DecTreeNode buildTree(List<List<Integer>> data, int depth) {
		if (data == null) {
			return null;
		}
		if (data.size() <= maxPerLeaf || depth == maxDepth) {
			return new DecTreeNode(getClass(data), -1, -1);
		}
		DecTreeNode cur = bestAttribute(data);
		if (cur.classLabel == -2) {
			return new DecTreeNode(getClass(data), -1, -1);
		}
		List<List<List<Integer>>> partition = Partition(data, cur.attribute, cur.threshold);
		cur.left = buildTree(partition.get(0), depth + 1);
		cur.right = buildTree(partition.get(1), depth + 1);
		return cur;
	}

	public int classify(List<Integer> instance) {
		// Note that the last element of the array is the label.
		DecTreeNode node = root;
		while (!node.isLeaf()) {
			if (instance.get(node.attribute) <= node.threshold) {
				node = node.left;
			} else {
				node = node.right;
			}
		}
		return node.classLabel;
	}
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
