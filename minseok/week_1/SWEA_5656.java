package com.ssafy.test;

import java.io.*;
import java.util.*;

class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringBuilder sb = new StringBuilder();

    static int n, w, h;
    static int best; // 남은 벽돌 최소
    static int [][]dir = {{0,1},{1,0},{-1,0},{0,-1}};

    static class Block {
        int x, y, power;
        public Block(int x, int y, int power) {
            this.x = x;
            this.y = y;
            this.power = power;
        }
    }

    public static void main(String[] args) throws Exception {
        int T = Integer.parseInt(br.readLine().trim());

        for (int test_case = 1; test_case <= T; test_case++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            w = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());

            int[][] board = new int[h][w];

            // 초기에 값을 입력받을 때, 제일 위쪽에 있는 블럭들부터 0번 row에 저장이 됨
            for (int i = 0; i < h; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < w; j++) {
                    board[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            best = Integer.MAX_VALUE;
            play_game(0, board);

            sb.append('#').append(test_case).append(' ').append(best).append('\n');
        }

        System.out.print(sb);
    }

    // 구슬을 떨어뜨릴 수 있는 모든 경우의 수를 시뮬레이션
    static void play_game(int depth, int[][] board) {
        int remain = count_bricks(board);
        if (depth == n) {
            best = Math.min(best, remain);
            return;
        }

        for (int i = 0; i < w; i++) {

            int[][] next = copy_board(board);
            shoot(next, i);
            play_game(depth + 1, next);

            if (best == 0) return;
        }
    }

    // 가장 위쪽에 있는 블럭 찾기
    static int get_first_block(int[][] board, int col) {
        for (int i = 0; i < h; i++) {
            if (board[i][col] != 0) return i;
        }
        return -1;
    }

    // 구슬로 블럭 폭파시키기
    static void shoot(int[][] board, int col) {
        int first_block = get_first_block(board, col);
        if (first_block == -1) return;
        // 해당 블록을 터뜨림
        bomb(board, first_block, col);

        // 중력에 의해 블럭들 바닥으로 내리기
        apply_gravity(board);
    }

    // 폭발시키고, 폭발된 블럭에 대해서 또 연쇄적으로 폭발이 진행
    static void bomb(int[][] board, int start_x, int start_y) {
        ArrayDeque<Block> queue = new ArrayDeque<>();
        if (board[start_x][start_y] != 1)
            queue.add(new Block(start_x, start_y, board[start_x][start_y]));
        board[start_x][start_y] = 0;

        while (!queue.isEmpty()) {
            Block cur = queue.poll();
            for (int i = 0; i < 4; i++) {
                int next_x = cur.x;
                int next_y = cur.y;
                // power-1 칸까지 전파
                for (int step = 1; step <= cur.power - 1; step++) {
                    next_x += dir[i][0];
                    next_y += dir[i][1];
                    if (is_out(next_x,next_y)) break;
                    if (board[next_x][next_y] == 0) continue;

                    int next_power = board[next_x][next_y];

                    // 만약 다음 블럭의 파워가 1보다 크면, 그 블럭으로 인해 주변 블럭도 폭발시켜야함
                    // 다음 블럭의 파워가 1이면 어차피 본인 블럭 하나만 터지니까 상관 없음
                    if (next_power > 1)
                        queue.add(new Block(next_x, next_y, next_power));
                    board[next_x][next_y] = 0; // 제거
                }
            }
        }
    }

    // 중력 적용: 각 열마다 아래로 떨어뜨리기
    static void apply_gravity(int[][] board) {
        for (int y = 0; y < w; y++) {
            //중력에 의해 떨어지면 서있게 될 위치
            int bottom = h - 1;
            for (int x = h - 1; x >= 0; x--) {
                // 현재 탐색 중인 idx가 0이 아니면 아래로 떨어져야함
                if (board[x][y] != 0) {
                    int val = board[x][y];
                    board[x][y] = 0;
                    // 떨어져야할 위치에 현재 idx 값을 할당
                    board[bottom][y] = val;
                    // 블럭이 아래로 떨어졌으니, 떨어질 위치 갱신
                    bottom--;
                }
            }
        }
    }

    static int count_bricks(int[][] board) {
        int cnt = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (board[i][j] != 0) cnt++;
            }
        }
        return cnt;
    }

    static int[][] copy_board(int[][] src) {
        int[][] temp = new int[h][w];
        for (int i = 0; i < h; i++) {
            System.arraycopy(src[i], 0, temp[i], 0, w);
        }
        return temp;
    }
    private static boolean is_out(int x, int y){
        return x<0||y<0||x>=h||y>=w;
    }
}