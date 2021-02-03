package ElevenStreet_2020_FALL;

import java.util.*;

public class ElevenStreet_2020_FALL_3 {
    public static void main(String[] args) {
        int[] a = new int[200000];
        for (int i = 0 ; i < 100000 ; i++) {
            a[i] = i + 1;
        }
        for (int i = 100000 ; i < 200000 ; i++) {
            a[i] = i + 1 - 100000;
        }
        System.out.println(solution3(a));
    }

    public static int solution3(int[] A) {
        Map<Integer, Integer> map = new HashMap<>(A.length);
        int answer = 0;

        for (int i = 0 ; i < A.length ; i++) {
            if (map.containsKey(A[i])) {
                int temp = map.get(A[i]);
                map.put(A[i], temp + 1);
            } else map.put(A[i], 1);
        }

        for (int i = 0 ; i < A.length ; i++) {
            if (i % 1000 == 0)System.out.println(i);
            int num = A[i];
            int numOfDuplicated = map.get(num);

            if (numOfDuplicated > 1) {
                int dx = 1;
                while (true) {
                    if (num + dx <= A.length && !map.containsKey(num + dx)) {
                        map.put(num, numOfDuplicated - 1);
                        map.put(num + dx, 1);
                        A[i] = num + dx;
                        answer += dx;
                        break;
                    } else if (num - dx > 0 && !map.containsKey(num - dx)) {
                        map.put(num, numOfDuplicated - 1);
                        map.put(num - dx, 1);
                        A[i] = num - dx;
                        answer += dx;
                        break;
                    } else {
                        dx++;
                    }
                }
            }
        }

      return answer;
    }
}
