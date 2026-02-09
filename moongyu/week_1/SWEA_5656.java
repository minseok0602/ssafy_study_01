import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Solution {
    static int N, W, H, ans;
    static final int[] dr = {-1, 1, 0, 0};
    static final int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());                                             // 던질 횟수
            W = Integer.parseInt(st.nextToken());                                             // 가로
            H = Integer.parseInt(st.nextToken());                                             // 세로

            int[][] map = new int[H][W];
            for (int i = 0; i < H; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < W; j++) map[i][j] = Integer.parseInt(st.nextToken());
            }

            ans = Integer.MAX_VALUE;
            // 초기 상태로 DFS 시작
            dfs(0, map, countBricks(map));
            System.out.println("#" + tc + " " + (ans == Integer.MAX_VALUE ? 0 : ans));
        }
    }

    // 구슬 던지기 시뮬레이션 (중복 순열 탐색)
    static void dfs(int depth, int[][] map, int remain) {
        // 벽돌이 없으면 즉시 종료
        if (remain == 0) {
            ans = 0;
            return;
        }

        // N번 던졌으면 최소값 갱신
        if (depth == N) {
            ans = Math.min(ans, remain);
            return;
        }

        // 반복문으로 가로 모든 칸에 던져보기
        for (int col = 0; col < W; col++) {
            int row = topBrickRow(map, col); // 해당 열의 가장 윗벽돌 찾기
            
            // 해당 열에 벽돌이 없으면 skip
            if (row == -1) {
                dfs(depth + 1, map, remain);
                continue;
            }

            // 원본 보존을 위해 맵 복사
            int[][] nextMap = copyMap(map);
            
            // 폭발 (연쇄 반응)
            int removed = explode(nextMap, row, col);
            // 벽돌 내리기
            applyGravity(nextMap);
            
            // 3. 재귀 호출 (남은 개수 차감)
            dfs(depth + 1, nextMap, remain - removed);
            
            if (ans == 0) return;
        }
    }

    // 연쇄 폭발 처리 (BFS)
    static int explode(int[][] map, int sr, int sc) {
        Queue<int[]> queue = new ArrayDeque<>();
        int removed = 0;

        int power = map[sr][sc];
        map[sr][sc] = 0; // 방문 처리 (벽돌 제거)
        queue.offer(new int[]{sr, sc, power});
        removed++;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];
            int p = curr[2]; // 폭발 범위

            if (p <= 1) continue; // 범위 1이면 자기만 터짐

            // 4방향 폭발 전파
            for (int d = 0; d < 4; d++) {
                for (int k = 1; k < p; k++) { // 범위(power-1)만큼 확장
                    int nr = r + dr[d] * k;
                    int nc = c + dc[d] * k;

                    if (nr >= 0 && nr < H && nc >= 0 && nc < W && map[nr][nc] != 0) {
                        removed++;
                        // 큐에 넣을 때 즉시 0으로 만들어 중복 방지 (메모리 관리)
                        queue.offer(new int[]{nr, nc, map[nr][nc]});
                        map[nr][nc] = 0;
                    }
                }
            }
        }
        return removed;
    }

    // 빈 공간 메우기 (아래로 밀기)
    static void applyGravity(int[][] map) {
        for (int c = 0; c < W; c++) {
            int write = H - 1; // 채워질 바닥 포인터
            for (int r = H - 1; r >= 0; r--) {
                if (map[r][c] != 0) {
                    int temp = map[r][c];
                    map[r][c] = 0;
                    map[write--][c] = temp; // 밑에서부터 채우기
                }
            }
        }
    }

    // 특정 열의 최상단 벽돌 위치 검색
    static int topBrickRow(int[][] map, int col) {
        for (int r = 0; r < H; r++) if (map[r][col] != 0) return r;
        return -1;
    }

    // 현재 남은 총 벽돌 개수 계산
    static int countBricks(int[][] map) {
        int cnt = 0;
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) if (map[i][j] != 0) cnt++;
        }
        return cnt;
    }

    // 2차원 배열 깊은 복사
    static int[][] copyMap(int[][] map) {
        int[][] copy = new int[H][W];
        for (int i = 0; i < H; i++) System.arraycopy(map[i], 0, copy[i], 0, W);
        return copy;
    }
}
