import java.util.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable

    private double inputValue = 0.0;
    private double inputWeightSum = 0.0;
    private double outputValue = 0.0;
    private boolean[] flag = new boolean[3]; // 0=inputWeightSum, 1=outputValue
    private double outputGradient = 0.0;
    private double delta = 0.0; //input gradient

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }

        if (type == 2 || type == 4) {
            parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type == 0) {    //If input node
            this.inputValue = inputValue;
        }
    }

    public void calculateInputWeighSum() {
        flag[0] = true;
        inputWeightSum =  parents.stream()
                .map(x -> x.node.getOutput() * x.weight)
                .reduce(0.0, Double::sum);
    }


    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public void calculateOutput() {
        if ((type == 2 || type == 4)) {  //Not an input or bias node
            if (!flag[0]) calculateInputWeighSum();
            outputValue = (type == 2) ? Math.max(0, inputWeightSum) : Math.exp(inputWeightSum);
            flag[1] = true;
        }
    }

    //Gets the output value
    public double getOutput() {
        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            if (!flag[1]) calculateOutput();
            return outputValue;
        }
    }

    // Calculate the delta value of a node.
    // value = additional value needed
    public void calculateDelta(double value) {
        if (type == 2)  {
            delta = value - getOutput();
        }
        if (type == 4) {
            if (!flag[0]) calculateInputWeighSum();
            delta = (inputWeightSum) <= 0 ? 0 : value;
        }
    }


    //Update the weights between parents node and current node
    public void updateWeight(double learningRate, double value) {
        if (type == 2 || type == 4) {
            calculateDelta(value);
            parents.forEach(x -> x.modifyWeight(learningRate * getOutput() * delta));
        }
    }

    public double findParentWeight(Node node) {
        return parents.stream()
                .filter(x -> x.node == node).map(x -> x.weight)
                .findFirst()
                .orElse(-1.0);
    }
}


