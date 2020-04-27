import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
        int cur = 0;
        double accSum = 0;
        clf = new NaiveBayesClassifier();
        for (int i = 0; i < trainData.size() / k; i++) {
            List<Instance> test = new ArrayList<>();
            List<Instance> train = new ArrayList<>(trainData);
            for (int j = 0; j < k; j++) {
                train.remove(cur - j);
                test.add(trainData.get(cur++));
            }
            clf.train(train, v);
            double acc = 0;
            for (int j = 0; j < test.size(); j++) {
                acc += (clf.classify(test.get(j).words).label == test.get(j).label) ? 1 : 0;
            }
            accSum += acc / test.size();
            System.out.println(accSum);
        }
        return accSum / ((double) trainData.size() / k);
    }
}
