package ElevenStreet_2020_FALL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevenStreet_2020_FALL_2 {
    public static void main(String[] args) {
        System.out.println(solution2(new String[]{"abc", "bca", "dbe"}));
    }

    public static int[] solution2(String[] S) {
        List<Map<String, Integer>> mapArray = new ArrayList<Map<String, Integer>>();

        for (int i = 0 ; i < S[0].length() ; i++) {
            mapArray.add(new HashMap<String, Integer>());
        }

        for (int i = 0; i < S.length ; i++) {
            for (int j = 0 ; j < S[i].length() ; j++) {
                String character = S[i].substring(j, j + 1);

                if (mapArray.get(j).containsKey(character)) {
                    return new int[]{mapArray.get(j).get(character), i, j};
                } else {
                    mapArray.get(j).put(character, i);
                }
            }
        }

        return new int[]{};
    }
}
