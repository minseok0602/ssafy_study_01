package com.ssafy;

import java.io.*;
import java.util.*;

public class SWEA_7206 {
    // 특정 숫자의 턴 수를 기록
    static Map<Integer, Integer> memo;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        for (int tc = 1; tc <= T; tc++) {
            int start = Integer.parseInt(br.readLine().trim());
            memo = new HashMap<>();
            int ans = dfs(start);
            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb);
    }
    static int dfs(int x) {
        // 숫자가 10보다 작으면 그냥 그대로 끝
        if (x < 10) return 0;
        // 만약 메모지에 있다면 그 결과를 바로 사용
        if (memo.containsKey(x))
            return memo.get(x);
        String s = Integer.toString(x);
        // 현재 숫자를 가지고 여러 조각들로 나누고, 가능한 제일 긴 턴을 찾음
        int best = separate(s, 0, 1, 0);
        memo.put(x, best);
        return best;
    }

    // s를 분할하는 재귀
    // idx: 자르기 시작할 위치
    // cur_mul: 지금까지 곱한 값
    // pieces: 지금까지 만든 조각 개수
    static int separate(String s, int idx, int cur_mul, int cnt) {

        if (idx == s.length()) {
            if (cnt <= 1) return 0;  // 안 자른 경우는 무효
            // 턴 수 1 증가후, 조각들을 곱한 수를 가지고 다시 게임 시작
            return 1 + dfs(cur_mul);
        }

        int best = 0;

        int num = 0;
        // idx부터 i까지를 한 덩어리로 만듦 (num)
        // idx 기준으로, idx 문자 하나를 덩어리로 만드는 것부터 시작해서, 끝까지를 한 덩어리로 만드는 것까지 모든 경우의 수를 따짐
        for (int i = idx; i < s.length(); i++) {
            num = num * 10 + (s.charAt(i) - '0');
            best = Math.max(best,
                    // 이 조각을 곱에 반영하고 다음 인덱스부터 또 가능한 모든 조각을 계산하면서, 턴 수가 제일 긴 걸 가져옴
                    separate(s, i + 1, cur_mul * num, cnt + 1));
        }

        return best;
    }
}
