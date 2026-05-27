import java.io.*;
import java.util.*;

public class AIRobotCleaner {

    static int N, K, L;
    static int[][] board;

    static int[] robotR;
    static int[] robotC;

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    // 청소 방향 우선순위 -> 오른쪽, 아래, 왼쪽, 위
    static int[] cleanOrder = {1, 2, 3, 0};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());

        board = new int[N][N];

        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());
            for (int c = 0; c < N; c++) {
                board[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        
        // 로봇 청소기 위치
        robotR = new int[K];
        robotC = new int[K];

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            robotR[i] = Integer.parseInt(st.nextToken()) - 1;
            robotC[i] = Integer.parseInt(st.nextToken()) - 1;
        }


        for (int t = 0; t < L; t++) {
            moveRobots();
            cleanRobots();
            stackDust();
            spreadDust();

            sb.append(totalDust()).append('\n');
        }

        System.out.print(sb);
    }

    // 1. 청소기들 이동
    static void moveRobots() {
        boolean[][] isRobot = new boolean[N][N];

        for (int i = 0; i < K; i++) {
            isRobot[robotR[i]][robotC[i]] = true;
        }
        
        // 모든 로봇 가장 가까운 먼지로 이동시킴
        for (int i = 0; i < K; i++) {
            int sr = robotR[i];
            int sc = robotC[i];

            // 자기 자신의 위치는 출발점
            isRobot[sr][sc] = false;

            int[] next = bfs(sr, sc, isRobot);

            // 갈 수 있는 먼지 칸이 있으면 이동
            if (next != null) {
                robotR[i] = next[0];
                robotC[i] = next[1];
            }

            // 이동한 위치를 다시 청소기 위치로 표시
            isRobot[robotR[i]][robotC[i]] = true;
        }
    }

    // 가장 가까운 먼지 칸 찾기
    static int[] bfs(int sr, int sc, boolean[][] isRobot) {
        boolean[][] visited = new boolean[N][N];
        Queue<int[]> q = new ArrayDeque<>();

        q.add(new int[] {sr, sc, 0});
        visited[sr][sc] = true;

        int bestR = -1;
        int bestC = -1;
        int bestDist = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] cur = q.poll();

            int r = cur[0];
            int c = cur[1];
            int dist = cur[2];

            // 이미 더 먼 거리라면 볼 필요 없음
            if (dist > bestDist) {
                break;
            }

            // 먼지가 있는 칸이면 후보값 등록
            if (board[r][c] > 0 && !isRobot[r][c]) {
                if (dist < bestDist) {
                    bestDist = dist;
                    bestR = r;
                    bestC = c;
                } else if (dist == bestDist) {
                	// 가까운 격자가 여러 개일 경우 행 번호가 작은격자, 행 번호마저 같으면 열 번호가 작은 격자로 해줘야 됨
                    if (r < bestR || (r == bestR && c < bestC)) {
                        bestR = r;
                        bestC = c;
                    }
                }

                // 이동하면 지금보다 거리가 더 멀어지기만 하니까 continue 시킴
                continue;
            }

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr < 0 || nr >= N || nc < 0 || nc >= N || 
                	visited[nr][nc] || board[nr][nc] == -1 || isRobot[nr][nc]) {
                    continue;
                }
                
                visited[nr][nc] = true;
                q.add(new int[] {nr, nc, dist + 1});
            }
        }

        if (bestR == -1) {
            return null;
        }

        return new int[] {bestR, bestC};
    }

    // 2. 청소 -> 청소 방향 결정하고 청소 진행
    static void cleanRobots() {
        for (int i = 0; i < K; i++) {
            int r = robotR[i];
            int c = robotC[i];

            int bestDir = -1;
            int bestAmount = -1;

            // 오른쪽, 아래, 왼쪽, 위 순서로 확인
            // 같은 값이면 먼저 본 방향을 그대로 선택하면 됨
            for (int dir : cleanOrder) {
                int amount = dustCount(r, c, dir);

                if (amount > bestAmount) {
                    bestAmount = amount;
                    bestDir = dir;
                }
            }

            clean(r, c, bestDir);
        }
    }

    // 해당 방향을 선택했을 때 청소 가능한 먼지량 계산
    static int dustCount(int r, int c, int dir) {
        int sum = 0;

        // 현재 위치
        if (board[r][c] > 0) {
            sum += Math.min(20, board[r][c]);
        }

        // 선택한 방향의 반대 방향은 청소하지 않음
        int back = (dir + 2) % 4;

        for (int d = 0; d < 4; d++) {
            if (d == back) {
                continue;
            }

            int nr = r + dr[d];
            int nc = c + dc[d];

            if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                continue;
            }

            if (board[nr][nc] > 0) {
                sum += Math.min(20, board[nr][nc]);
            }
        }

        return sum;
    }

    // 실제 청소 수행
    static void clean(int r, int c, int dir) {
        // 현재 위치 청소
        if (board[r][c] > 0) {
            board[r][c] -= Math.min(20, board[r][c]);
        }

        for (int d = 0; d < 4; d++) {
        	// 현재 청소기 방향 뒤쪽은 청소 X
            if (d == (dir + 2) % 4) {
                continue;
            }

            int nr = r + dr[d];
            int nc = c + dc[d];

            if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                continue;
            }

            if (board[nr][nc] > 0) {
                board[nr][nc] -= Math.min(20, board[nr][nc]);
            }
        }
    }

    // 3. 먼지 축적
    static void stackDust() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] > 0) board[r][c] += 5;
            }
        }
    }

    // 4. 먼지 확산
    static void spreadDust() {
        int[][] add = new int[N][N];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                // 깨끗한 칸만 확산됨
                if (board[r][c] != 0) {
                    continue;
                }

                int sum = 0;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                        continue;
                    }

                    if (board[nr][nc] > 0) {
                        sum += board[nr][nc];
                    }
                }

                add[r][c] = sum / 10;
            }
        }

        // 동시에 확산되어야 하므로 나중에 한 번에 반영시킴
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                board[r][c] += add[r][c];
            }
        }
    }

    // 먼지 count
    static long totalDust() {
        long sum = 0;

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] > 0) {
                    sum += board[r][c];
                }
            }
        }

        return sum;
    }
}