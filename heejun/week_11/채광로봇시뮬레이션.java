import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class 채광로봇시뮬레이션 {

    static final int MIN_VALUE = -1000000000;

    static int n, t;
    static int[][] board;
    static int[][] tMax;
    static int[][][] dp;

    static void calculateMaxProfit(int startX, int startY, int x, int y, int passedTime, int profit) {
        if (passedTime == t) {
            tMax[startX][startY] = Math.max(tMax[startX][startY], profit);
            return;
        }

        // 아래로 이동
        if (x + 1 < n) {
            calculateMaxProfit(startX, startY, x + 1, y, passedTime + 1, profit + board[x + 1][y]);
        }

        // 오른쪽으로 이동
        if (y + 1 < n) {
            calculateMaxProfit(startX, startY, x, y + 1, passedTime + 1, profit + board[x][y + 1]);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        tMax = new int[n][n];
        dp = new int[n][n][2];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 초기화
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tMax[i][j] = MIN_VALUE;
                dp[i][j][0] = MIN_VALUE;
                dp[i][j][1] = MIN_VALUE;
            }
        }

        // 각 칸에서 시간 역행을 썼을 때 얻을 수 있는 최대 수익 계산
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                calculateMaxProfit(i, j, i, j, 0, board[i][j]);
            }
        }


        dp[0][0][0] = board[0][0];

        // DP 진행
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // 현재 위치에서 시간 역행 사용
                dp[i][j][1] = Math.max(dp[i][j][1], dp[i][j][0] + tMax[i][j]);

                // 아래로 이동
                if (i + 1 < n) {
                    dp[i + 1][j][0] = Math.max(dp[i + 1][j][0], dp[i][j][0] + board[i + 1][j]);

                    dp[i + 1][j][1] = Math.max(dp[i + 1][j][1], dp[i][j][1] + board[i + 1][j]);
                }

                // 오른쪽으로 이동
                if (j + 1 < n) {
                    dp[i][j + 1][0] = Math.max(dp[i][j + 1][0], dp[i][j][0] + board[i][j + 1]);

                    dp[i][j + 1][1] = Math.max(dp[i][j + 1][1], dp[i][j][1] + board[i][j + 1]);
                }
            }
        }

        System.out.println(Math.max(dp[n - 1][n - 1][0], dp[n - 1][n - 1][1]));
    }
}
