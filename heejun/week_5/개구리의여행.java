package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
    static int N, Q;
    static char[][] board;

    static class Plan {
        int r1, c1, r2, c2;

        public Plan(int r1, int c1, int r2, int c2) {
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
        }
    }

    static Plan[] plans;

    static class Node implements Comparable<Node> {
        int r, c, power, cost;
        boolean up;

        public Node(int r, int c, int power, int cost, boolean up) {
            this.r = r;
            this.c = c;
            this.power = power;
            this.cost = cost;
            this.up = up;
        }

        @Override
        public int compareTo(Node o) {
            return this.cost - o.cost;
        }
    }

    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};
    static final int INF = 999_999_999;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        board = new char[N][N];

        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < N; j++) {
                board[i][j] = line.charAt(j);
            }
        }

        Q = Integer.parseInt(br.readLine());
        plans = new Plan[Q];

        for (int i = 0; i < Q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken()) - 1;
            int c1 = Integer.parseInt(st.nextToken()) - 1;
            int r2 = Integer.parseInt(st.nextToken()) - 1;
            int c2 = Integer.parseInt(st.nextToken()) - 1;
            plans[i] = new Plan(r1, c1, r2, c2);
        }

        for (Plan p : plans) {
            System.out.println(find(p));
        }
    }

    private static int find(Plan p) {
        int[][][] dist = new int[N][N][6];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Arrays.fill(dist[i][j], INF);
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();

        dist[p.r1][p.c1][1] = 0;
        pq.offer(new Node(p.r1, p.c1, 1, 0, false));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            int r = cur.r;
            int c = cur.c;
            int power = cur.power;
            int cost = cur.cost;
            boolean up = cur.up;

            if (cost > dist[r][c][power]) continue;

            if (r == p.r2 && c == p.c2) {
                return cost;
            }

            // 1. 현재 점프력으로 4방향 이동 (비용 +1)
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d] * power;
                int nc = c + dc[d] * power;

                if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                if (board[nr][nc] == 'S' || board[nr][nc] == '#') continue;
                if (check(r, c, d, power)) continue;

                int nextCost = cost + 1;
                if (dist[nr][nc][power] > nextCost) {
                    dist[nr][nc][power] = nextCost;
                    pq.offer(new Node(nr, nc, power, nextCost, false));
                }
            }

            // 2. 파워업 (비용 +(power+1)^2)
            if (power + 1 <= 5) {
                int nextPower = power + 1;
                int nextCost = cost + nextPower * nextPower;
                if (dist[r][c][nextPower] > nextCost) {
                    dist[r][c][nextPower] = nextCost;
                    pq.offer(new Node(r, c, nextPower, nextCost, true));
                }
            }

            // 3. 점프력 감소 (직전 행동이 파워업이 아닐 때만, 비용 +1)
            if (!up) {
                for (int nextPower = 1; nextPower < power; nextPower++) {
                    int nextCost = cost + 1;
                    if (dist[r][c][nextPower] > nextCost) {
                        dist[r][c][nextPower] = nextCost;
                        pq.offer(new Node(r, c, nextPower, nextCost, false));
                    }
                }
            }
        }

        return -1;
    }

    // 이동 경로 중 #이 있으면 막힘
    private static boolean check(int r, int c, int d, int power) {
        for (int i = 1; i <= power; i++) {
            int nr = r + dr[d] * i;
            int nc = c + dc[d] * i;
            if (board[nr][nc] == '#') return true;
        }
        return false;
    }
}
