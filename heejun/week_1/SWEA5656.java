package study;

import java.io.*;
import java.util.*;

public class SWEA5656 {
    static int T, N, W, H;
    static int ans;

    static final int[] dr = {-1, 1, 0, 0};
    static final int[] dc = {0, 0, -1, 1};

    static class Node {
        int r, c, power;
        Node(int r, int c, int power) {
            this.r = r; this.c = c; this.power = power;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        T = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();

        for (int tc = 1; tc <= T; tc++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            W = Integer.parseInt(st.nextToken());
            H = Integer.parseInt(st.nextToken());

            int[][] board = new int[H][W];
            for (int r = 0; r < H; r++) {
                st = new StringTokenizer(br.readLine());
                for (int c = 0; c < W; c++) {
                    board[r][c] = Integer.parseInt(st.nextToken());
                }
            }

            ans = Integer.MAX_VALUE;
            dfs(0, board);

            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb);
    }

    static void dfs(int depth, int[][] board) {
        if (ans == 0) return; // 더 줄일 수 없음

        int remain = countBricks(board);
        if (remain == 0) { // 이미 다 깼으면 끝
            ans = 0;
            return;
        }

        if (depth == N) {
            ans = Math.min(ans, remain);
            return;
        }

        for (int col = 0; col < W; col++) {
            int[][] next = copyBoard(board);

            boolean hit = dropAndExplode(next, col);
            if (hit) applyGravity(next);

            dfs(depth + 1, next);
        }
    }

    // col에 구슬 떨어뜨리고 폭발 처리
    static boolean dropAndExplode(int[][] board, int col) {
        int r = 0;
        while (r < H && board[r][col] == 0) r++;
        if (r == H) return false; // 맞출 벽돌 없음

        ArrayDeque<Node> q = new ArrayDeque<>();
        q.offer(new Node(r, col, board[r][col]));
        board[r][col] = 0; // 시작 벽돌 제거

        while (!q.isEmpty()) {
            Node cur = q.poll();
            int power = cur.power;

            // power=1이면 자기만 깨지고 확장 없음
            for (int d = 0; d < 4; d++) {
                int nr = cur.r;
                int nc = cur.c;
                for (int step = 1; step < power; step++) {
                    nr += dr[d];
                    nc += dc[d];
                    if (nr < 0 || nr >= H || nc < 0 || nc >= W) break;
                    if (board[nr][nc] == 0) continue;

                    int p = board[nr][nc];
                    board[nr][nc] = 0; // 깨기
                    if (p > 1) q.offer(new Node(nr, nc, p));
                }
            }
        }
        return true;
    }

    // 중력 적용: 각 열별로 아래로 당김
    static void applyGravity(int[][] board) {
        for (int c = 0; c < W; c++) {
            int write = H - 1;
            for (int r = H - 1; r >= 0; r--) {
                if (board[r][c] != 0) {
                    int val = board[r][c];
                    board[r][c] = 0;
                    board[write][c] = val;
                    write--;
                }
            }
        }
    }

    static int countBricks(int[][] board) {
        int cnt = 0;
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                if (board[r][c] != 0) cnt++;
            }
        }
        return cnt;
    }

    static int[][] copyBoard(int[][] board) {
        int[][] copy = new int[H][W];
        for (int r = 0; r < H; r++) {
            System.arraycopy(board[r], 0, copy[r], 0, W);
        }
        return copy;
    }
}
