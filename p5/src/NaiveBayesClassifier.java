import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {
    Map<Label, Integer> wordCount;
    Map<Label, Integer> documentCount;
    Map<String, Integer> positiveWordOccur = new HashMap<>();
    Map<String, Integer> negativeWordOccur = new HashMap<>();
    int numOfDocument;
    int totalVocabCount;
    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
        // Hint: First, calculate the documents and words counts per label and store them. 
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
        // Save these information as you will need them to calculate the log probabilities later.
        //
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
        totalVocabCount = v;
        wordCount = getWordsCountPerLabel(trainData);
        documentCount = getDocumentsCountPerLabel(trainData);
        numOfDocument = documentCount.get(Label.POSITIVE) + documentCount.get(Label.NEGATIVE);
        for (Instance i : trainData) {
            for (String word: i.words) {
                if (i.label == Label.POSITIVE) {
                    positiveWordOccur.merge(word, 1, Integer::sum);
                } else {
                    negativeWordOccur.merge(word, 1, Integer::sum);
                }
            }
        }
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        Map<Label, Integer> map = new HashMap<>();
        map.put(Label.POSITIVE, trainData.stream().filter(x -> x.label == Label.POSITIVE)
                .mapToInt(x -> x.words.size()).sum());
        map.put(Label.NEGATIVE, trainData.stream().filter(x -> x.label == Label.NEGATIVE)
                .mapToInt(x -> x.words.size()).sum());
        return map;
    }


    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        Map<Label, Integer> map = new HashMap<>();
        map.put(Label.POSITIVE, (int)trainData.stream().filter(x -> x.label == Label.POSITIVE).count());
        map.put(Label.NEGATIVE, (int)trainData.stream().filter(x -> x.label == Label.NEGATIVE).count());
        return map;
    }


    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
    private double p_l(Label label) {
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
        return (double)documentCount.get(label) / numOfDocument;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        // Calculate the probability with Laplace smoothing for word in class(label)
        Map<String, Integer> occur = (label == Label.POSITIVE) ? positiveWordOccur : negativeWordOccur;
        double numOfToken_w =  (occur.get(word) == null) ? 0 : occur.get(word);
        double sumOfTokenInLable = occur.values().stream().mapToInt(Integer::intValue).sum();
        return (numOfToken_w + 1) / (totalVocabCount + sumOfTokenInLable);
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
        ClassifyResult cr = new ClassifyResult();
        double positive = Math.log(p_l(Label.POSITIVE));
        double negative = Math.log(p_l(Label.NEGATIVE));
        for (String word : words) {
            positive += Math.log(p_w_given_l(word, Label.POSITIVE));
            negative += Math.log(p_w_given_l(word, Label.NEGATIVE));
        }
        cr.label = (positive >= negative) ? Label.POSITIVE : Label.NEGATIVE;
        cr.logProbPerLabel = new HashMap<>();
        cr.logProbPerLabel.put(Label.POSITIVE, positive);
        cr.logProbPerLabel.put(Label.NEGATIVE, negative);
        return cr;
    }


}
