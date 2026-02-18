package com.ssafy;

import java.io.*;
import java.util.StringTokenizer;

class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int[][] map;
    static long[][][] dp;

    public static void main(String[] args) throws Exception {
        int n = Integer.parseInt(br.readLine().trim());
        map = new int[n][n];
        // 현재 모양에 따라서 3가지로 나눔 (가로, 대각선, 세로)
        // 0 : 대각선
        // 1 : 가로
        // 2 : 세로
        dp = new long[n][n][3];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 초기 상태: 0,1에 가로 모양으로 파이프가 놓여있음
        dp[0][1][1] = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 2; j < n; j++) {
                // 해당 위치가 벽이면 그냥 넘어감
                if (map[i][j] == 1) continue;

                // 현재 모양이 가로인 경우의 수
                // 왼쪽에서 가로로 오는 경우
                // 왼쪽에서 대각선으로 오는 경우
                dp[i][j][1] += dp[i][j - 1][1] + dp[i][j - 1][0];

                // 현재 모양이 세로인 경우의 수
                // 위쪽에서 세로로 오는 경우의 수
                // 위쪽에서 대각선으로 오는 경우의 수
                if (i > 0) {
                    dp[i][j][2] += dp[i - 1][j][2] + dp[i - 1][j][0];
                }

                // 현재 모양이 대각선인 경우의 수
                // 본인의 바로 위쪽과, 본인의 바로 왼쪽에 벽이 없어야 대각선으로 돌릴 수 있음
                // 왼쪽 위에서 대각선으로 오는 경우의 수
                // 왼쪽 위에서 가로로 오는 경우의 수
                // 왼쪽 위에서 세로로 오는 경우의 수
                if (i > 0 && map[i - 1][j] == 0 && map[i][j - 1] == 0) {
                    dp[i][j][0] += dp[i - 1][j - 1][0] + dp[i - 1][j - 1][1] + dp[i - 1][j - 1][2];
                }
            }
        }
        // 마지막 좌표에 도착할 수 있는 경우의 수를 모양 별로 모두 더하면 정답
        long answer = dp[n - 1][n - 1][0] + dp[n - 1][n - 1][1] + dp[n - 1][n - 1][2];
        System.out.println(answer);
    }
}
