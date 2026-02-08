package week_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_5656 {
    static int N, W, H;
    static int ans;

    static final int[] dr = {-1, 1, 0, 0};
    static final int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
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
            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb.toString());
    }

    static void dfs(int depth, int[][] map, int remain) {
        if (remain == 0) {
            ans = 0;
            return;
        }
        if (depth == N) {
            if (remain < ans) ans = remain;
            return;
        }
        if (remain >= ans) return;

        for (int col = 0; col < W; col++) {
            int row = topBrickRow(map, col);
            if (row == -1) {
                dfs(depth + 1, map, remain);
                if (ans == 0) return;
                continue;
            }

            int[][] next = copyMap(map);
            int removed = explode(next, row, col);
            applyGravity(next);
            dfs(depth + 1, next, remain - removed);
            if (ans == 0) return;
        }
    }

    static int topBrickRow(int[][] map, int col) {
        for (int r = 0; r < H; r++) {
            if (map[r][col] != 0) return r;
        }
        return -1;
    }

    static int explode(int[][] map, int sr, int sc) {
        int[] qr = new int[H * W];
        int[] qc = new int[H * W];
        int head = 0, tail = 0;

        qr[tail] = sr;
        qc[tail] = sc;
        tail++;

        int removed = 0;

        while (head < tail) {
            int r = qr[head];
            int c = qc[head];
            head++;

            int power = map[r][c];
            if (power == 0) continue;

            map[r][c] = 0;
            removed++;

            for (int d = 0; d < 4; d++) {
                for (int k = 1; k < power; k++) {
                    int nr = r + dr[d] * k;
                    int nc = c + dc[d] * k;
                    if (nr < 0 || nr >= H || nc < 0 || nc >= W) break;
                    if (map[nr][nc] == 0) continue;
                    qr[tail] = nr;
                    qc[tail] = nc;
                    tail++;
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
                    int v = map[r][c];
                    map[r][c] = 0;
                    map[write][c] = v;
                    write--;
                }
            }
        }
    }

    static int countBricks(int[][] map) {
        int cnt = 0;
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                if (map[r][c] != 0) cnt++;
            }
        }
        return cnt;
    }

    static int[][] copyMap(int[][] map) {
        int[][] copy = new int[H][W];
        for (int r = 0; r < H; r++) {
            System.arraycopy(map[r], 0, copy[r], 0, W);
        }
        return copy;
    }
}