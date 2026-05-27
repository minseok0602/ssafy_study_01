import java.io.*;
import java.util.*;

public class 채광로봇시뮬레이션 {
    static int n, t;
    static int[][] grid;
    static int[][] front, back;

    static final int MIN_VAL = -1000000000;

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        grid = new int[n][n];
        front = new int[n][n];
        back = new int[n][n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0;j<n;j++){
                grid[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < n; i++) {
            Arrays.fill(front[i], MIN_VAL);
            Arrays.fill(back[i], MIN_VAL);
        }

        // front[i][j] = 시작점 -> (i,j) 최대 이익
        front[0][0] = grid[0][0];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;
                if(j==0)
                    front[i][j] = front[i-1][j] + grid[i][j];
                else if(i==0)
                    front[i][j] = front[i][j-1] + grid[i][j];
                else
                    front[i][j] = Math.max(front[i-1][j],front[i][j-1]) + grid[i][j];
            }
        }

        // back[i][j] = (i,j) -> 도착점 최대 이익
        back[n - 1][n - 1] = grid[n - 1][n - 1];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (i == n - 1 && j == n - 1) continue;

                if(i==n-1){
                    back[i][j] = back[i][j+1] + grid[i][j];
                }
                else if(j==n-1){
                    back[i][j] = back[i+1][j] + grid[i][j];
                }
                else{
                    back[i][j] = Math.max(back[i][j+1],back[i+1][j]) + grid[i][j];
                }
            }
        }

        // 시간 역행을 하지 않고, 0,0 -> n-1,n-1로 가는 가장 최대 이익이 기본 정
        int answer = front[n - 1][n - 1];

        // A = 시간 역행 후 돌아올 위치
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // 지금 위치에서 t초만큼 이동할 수 없으면 반영 x
                if((n - 1 - i) + (n - 1 - j)<t)
                    continue;
                int loop = getLoop(i, j);

                if (loop == MIN_VAL) 
                    continue;

                // 0,0 -> A
                // A -> B
                // 시간 역행으로 A에 도착
                // A -> n-1, n-1
                answer = Math.max(answer, front[i][j] + loop + back[i][j]);
            }
        }

        System.out.println(answer);
    }

    // A 좌표에서 정확히 t초 동안 오른쪽/아래로 이동하면서 얻는 최대 이익
    static int getLoop(int sx, int sy) {
        return dfs(sx, sy, 0, 0);
    }

    static int dfs(int x, int y, int depth, int sum) {
        if (depth == t) {
            return sum;
        }

        int best = MIN_VAL;

        // 아래로 이동
        if (x + 1 < n) {
            best = Math.max(
                    best,
                    dfs(x + 1, y, depth + 1, sum + grid[x + 1][y])
            );
        }

        // 오른쪽으로 이동
        if (y + 1 < n) {
            best = Math.max(
                    best,
                    dfs(x, y + 1, depth + 1, sum + grid[x][y + 1])
            );
        }

        return best;
    }
}
