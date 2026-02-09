package week_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class SWEA_5656 {
    static int N, W, H, ans;
    static final int[] dr = {-1, 1, 0, 0};
    static final int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            W = Integer.parseInt(st.nextToken());
            H = Integer.parseInt(st.nextToken());

            int[][] map = new int[H][W];
            for (int i = 0; i < H; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < W; j++) map[i][j] = Integer.parseInt(st.nextToken());
            }

            ans = Integer.MAX_VALUE;
            dfs(0, map, countBricks(map));
            System.out.println("#" + tc + " " + (ans == Integer.MAX_VALUE ? 0 : ans));
        }
    }

    static void dfs(int depth, int[][] map, int remain) {
        if (remain == 0) {
            ans = 0;
            return;
        }

        if (depth == N) {
            ans = Math.min(ans, remain);
            return;
        }

        for (int col = 0; col < W; col++) {
            int row = topBrickRow(map, col);
            
            if (row == -1) {
                dfs(depth + 1, map, remain);
                continue;
            }

            int[][] nextMap = copyMap(map);
            int removed = explode(nextMap, row, col);
            applyGravity(nextMap);
            
            dfs(depth + 1, nextMap, remain - removed);
            
            if (ans == 0) return;
        }
    }

    static int explode(int[][] map, int sr, int sc) {
        Queue<int[]> queue = new ArrayDeque<>();
        int removed = 0;

        int power = map[sr][sc];
        map[sr][sc] = 0;
        queue.offer(new int[]{sr, sc, power});
        removed++;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];
            int p = curr[2];

            if (p <= 1) continue;

            for (int d = 0; d < 4; d++) {
                for (int k = 1; k < p; k++) {
                    int nr = r + dr[d] * k;
                    int nc = c + dc[d] * k;

                    if (nr >= 0 && nr < H && nc >= 0 && nc < W && map[nr][nc] != 0) {
                        removed++;
                        queue.offer(new int[]{nr, nc, map[nr][nc]});
                        map[nr][nc] = 0;
                    }
                }
            }
        }
        return removed;
    }

    static void applyGravity(int[][] map) {
        for (int c = 0; c < W; c++) {
            int write = H - 1;
            for (int r = H - 1; r >= 0; r--) {
                if (map[r][c] != 0) {
                    int temp = map[r][c];
                    map[r][c] = 0;
                    map[write--][c] = temp;
                }
            }
        }
    }

    static int topBrickRow(int[][] map, int col) {
        for (int r = 0; r < H; r++) if (map[r][col] != 0) return r;
        return -1;
    }

    static int countBricks(int[][] map) {
        int cnt = 0;
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) if (map[i][j] != 0) cnt++;
        }
        return cnt;
    }

    static int[][] copyMap(int[][] map) {
        int[][] copy = new int[H][W];
        for (int i = 0; i < H; i++) System.arraycopy(map[i], 0, copy[i], 0, W);
        return copy;
    }
}
