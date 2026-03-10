package study;

class No_2 {
    static int[] discounts = {10, 20, 30, 40};
    static int[] answer = new int[2]; // [가입자 수, 매출액]
    static int[] selected; // 각 이모티콘에 적용한 할인율

    public int[] solution(int[][] users, int[] emoticons) {
        selected = new int[emoticons.length];
        dfs(0, users, emoticons);
        return answer;
    }

    // 모든 가능한 세일 조합 다해보기
    static void dfs(int cnt, int[][] users, int[] emoticons) {
        if (cnt == emoticons.length) {
            evaluate(users, emoticons);
            return;
        }

        for (int d : discounts) {
            selected[cnt] = d;
            dfs(cnt + 1, users, emoticons);
        }
    }


    static void evaluate(int[][] users, int[] emoticons) {
        int subscriber = 0;
        int sales = 0;

        for (int[] user : users) {
            int minDiscount = user[0];
            int limitPrice = user[1];
            int sum = 0;

            for (int i = 0; i < emoticons.length; i++) {
                if (selected[i] >= minDiscount) {
                    sum += emoticons[i] * (100 - selected[i]) / 100;
                }
            }

            if (sum >= limitPrice) {
                subscriber++;
            } else {
                sales += sum;
            }
        }

        if (subscriber > answer[0]) {
            answer[0] = subscriber;
            answer[1] = sales;
        } else if (subscriber == answer[0] && sales > answer[1]) {
            answer[1] = sales;
        }
    }
}
