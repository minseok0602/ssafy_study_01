package week_4;

public class YelloLight {
    public int solution(int[][] signals) {
        int answer = -1;
        int[] cycle = new int[signals.length];
        for (int i = 0; i < signals.length; i++) {
            cycle[i] += signals[i][0] + signals[i][1] + signals[i][2];
        }

        int LCM = lcmArray(cycle);

        for (int t = 0; t < LCM; t++) {
            boolean isAllYellow = true;

            for (int i = 0; i < signals.length; i++) {
                int time = t % cycle[i];
                int green = signals[i][0];
                int yellow = signals[i][1];

                if (! (green <= time && time < green + yellow)) {
                    isAllYellow = false;
                    break;
                }
            }

            if (isAllYellow) {
                answer = t + 1;
                break;
            }
        }

        return answer;
    }

    public static int lcmArray(int[] arr) {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result = lcm(result, arr[i]);
        }
        return result;
    }

    private static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }
    private static int gcd(int a, int b) {
        if (a % b == 0) {
            return b;
        }

        return gcd(b, a % b);
    }
}
