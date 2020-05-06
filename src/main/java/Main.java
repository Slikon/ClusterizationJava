import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Clusterization method = new Clusterization(loadFileToMapFromCSV());
        method.createModel();

        new ChartGUI(method.getModel());
    }

    private static Map<String, Map<String, Double>> loadFileToMapFromCSV() {
        Map<String, Map<String, Double>> wordAttributes = new HashMap<>(5000, 0.5f);

        try (BufferedReader reader = new BufferedReader(new FileReader("train.csv"))) {
            String readLine;
            String[] words;

            while ((readLine = reader.readLine()) != null) {

                readLine = processString(readLine);

                words = readLine.split(" ");

                int index;
                for (String word: words) {

                    index = 0;
                    while ((index = readLine.indexOf(word, index)) != -1) {
                        putWordAttributesToMap(wordAttributes, word, index);
                        index++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordAttributes.entrySet().removeIf(entry -> entry.getValue().get("count") == 1);
        wordAttributes.forEach((key, value) -> value.replace("X", value.get("X") / value.get("count")));

        return wordAttributes;
    }

    private static String processString(String str) {
        str = str.toLowerCase();
        str = str.replaceAll("[\"\'!,.:@#$%^&*()](\\S*\\d+\\S*)", "");
        str = str.replaceAll("^\\s+|\\s+$", "");
        str = str.replaceAll("\\s{2,}", " ");

        return str;
    }

    private static void putWordAttributesToMap(Map<String, Map<String, Double>> wordsMap, String word, double index) {

        Map<String, Double> wordAttributes = wordsMap.get(word);

        if (wordAttributes != null) {

            wordAttributes.replace("count", wordAttributes.get("count") + 1);
            wordAttributes.replace("X", wordAttributes.get("X") + index);
        } else {

            wordAttributes = new HashMap<>();
            wordAttributes.put("count", 1.0);
            wordAttributes.put("X", index);
            wordsMap.put(word, wordAttributes);
        }
    }
}
