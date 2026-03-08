package week_3;

import java.util.ArrayList;

public class Emoticons {
    public static final int[] discout = new int[] {10, 20, 30, 40};
    public static int emoticonLength, maxPrice, plusMan;
    public static int[] emti;
    public static int[][] user;
    public static ArrayList<Integer> disc;

    public int[] solution(int[][] users, int[] emoticons) {
        disc = new ArrayList<>();
        emti = emoticons;
        emoticonLength = emoticons.length;
        maxPrice = Integer.MIN_VALUE;
        plusMan = Integer.MIN_VALUE;
        user = users;

        dfs(0);

        return new int[] {plusMan, maxPrice};
    }

    static void dfs(int count){
        if (count == emoticonLength) {
            int totalSum = 0;
            int emtiPlus = 0;

            for (int i = 0; i < user.length; i++) {
                int userSum = 0;

                for (int j = 0; j < emoticonLength; j++) {
                    if (disc.get(j) >= user[i][0]) {
                        userSum += (emti[j] * (100 - disc.get(j))) / 100;
                    }
                }

                if (userSum >= user[i][1]) ++emtiPlus;
                else totalSum += userSum;
            }

            if (emtiPlus > plusMan) {
                plusMan = emtiPlus;
                maxPrice = totalSum;
            } else if (emtiPlus == plusMan) {
                maxPrice = Math.max(maxPrice, totalSum);
            }
            return;
        }

        for (int i = 0; i < 4; i++){
            disc.add(discout[i]);
            dfs(count + 1);
            disc.remove(disc.size() - 1);
        }
    }
}