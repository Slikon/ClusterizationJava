import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class Clusterization {
    private Map<String, Map<String, Double>> wordAttributes;
    private Map<String, List<Pair<String, Double>>> model;

    Clusterization(Map<String, Map<String, Double>> wordAttributes) {
        this.wordAttributes = wordAttributes;
    }

    void createModel() {
        model = new HashMap<>(5000, 0.5f);
        Pair<String, Double> pair;
        ArrayList<Pair<String, Double>> list = null;
        double min, temp;
        double[] average = new double[2];
        String name1 = null, name2 = null;

        for (Map.Entry<String, Map<String, Double>> entry : wordAttributes.entrySet()) {

            pair = new Pair<>(entry.getKey(), entry.getValue().get("X"));

            list = new ArrayList<>();
            list.add(pair);

            wordAttributes.get(entry.getKey()).put("length", (double) entry.getKey().length());

            model.put(entry.getKey(), list);
        }

        while (model.size() > 3) {

            min = 1000;
            for (Map.Entry<String, List<Pair<String, Double>>> entry : model.entrySet()) {
                for (Map.Entry<String, List<Pair<String, Double>>> entry1 : model.entrySet()) {

                    if (entry.getKey().equals(entry1.getKey())) {
                        continue;
                    }

                    average[0] = wordAttributes.get(entry.getKey()).get("X") - wordAttributes.get(entry1.getKey()).get("X");
                    average[1] = wordAttributes.get(entry.getKey()).get("length") - wordAttributes.get(entry1.getKey()).get("length");

                    temp = sqrt(pow(average[0], 2) + pow(average[1], 2));

                    if (Double.compare(temp, min) < 0) {
                        min = temp;

                        name1 = entry.getKey();
                        name2 = entry1.getKey();

                        list = new ArrayList<>(entry.getValue());
                        list.addAll(entry1.getValue());
                    }
                }
            }

            wordAttributes.get(name1).replace("X", (wordAttributes.get(name1).get("X") + wordAttributes.get(name2).get("X")) / 2);
            wordAttributes.get(name1).replace("length", (wordAttributes.get(name1).get("length") + wordAttributes.get(name2).get("length")) / 2);
            model.remove(name2);
            model.replace(name1, list);
        }

        int i = 1;
        String[] keySet = new String[3];
        model.keySet().toArray(keySet);
        for (String key : keySet) {

            model.put("cluster-" + i, model.get(key));
            model.remove(key);
            i++;
        }
    }

    Map<String, List<Pair<String, Double>>> getModel() {
        return model;
    }
}