package ElevenStreet_2020_FALL;

public class ElevenStreet_2020_FALL_1 {

    public static void main(String[] args) {
        System.out.println(solution1("aageea"));
    }

    public static int solution1(String S) {
        int numOfOthers = 0;
        int numOfa = 0;
        int numOfaa = 0;
        int aIndex = 0;

        for (int i = 0 ; i < S.length() ; i++) {
            if (S.substring(i, i + 1).equals("a")) {
                aIndex++;
            } else {
                numOfOthers++;
                if (aIndex == 1) {
                    numOfa++;
                    aIndex = 0;
                } else if (aIndex == 2) {
                    numOfaa++;
                    aIndex = 0;
                } else if (aIndex >= 3) {
                    return -1;
                }
            }
        }

        if (S.substring(S.length() - 1, S.length()).equals("a")) {
            if (aIndex == 1) {
                numOfa++;
            } else if (aIndex == 2) {
                numOfaa++;
            } else if (aIndex >= 3) {
                return -1;
            }
        }

        return (numOfOthers + 1 - numOfaa) * 2 - numOfa;
    }
}

