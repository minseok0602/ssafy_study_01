import java.io.*;
import java.util.*;

public class ComfortableWorkshop {

    static int N, K;
    static int[][] map;

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    
    // 각 칸의 위치, 높이 저장 클래스
    static class Node {
        int r;
        int c;
        int h;

        Node(int r, int c, int h) {
            this.r = r;
            this.c = c;
            this.h = h;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        // N : 격자 크기, K : 필요한 등산로 최소 길이
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        
        // 모든 칸 정보 저장
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                nodes.add(new Node(i, j, map[i][j]));
            }
        }

        // 낮은 높이부터 처리 (등산로는 항상 낮은 곳에서 높은 곳으로만 이동 가능)
        nodes.sort((a, b) -> a.h - b.h);

        int INF = Integer.MAX_VALUE;

        // dp[r][c][len] : (r, c) 칸에 도착했고, 등산로 길이가 len일 때, 해당 등산로에서 발생한 인접한 높이 차의 최댓값의 최소값
        // 길이 K 까지만 확인하면 됨.
        // 불편함은 높이 차이의 최댓값이므로 K 경로 이상 가봤자 불편함이 그대로이거나 커지기만 한다.
        int[][][] dp = new int[N][N][K + 1];
        
        
        // 길이가 1인 곳 0으로 초기화 (길이가 1 -> 즉 자기 자신이 출발점)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Arrays.fill(dp[i][j], INF);
                dp[i][j][1] = 0;
            }
        }

        for (Node node : nodes) {
            int r = node.r;
            int c = node.c;

            for (int len = 1; len < K; len++) {
                if (dp[r][c][len] == INF) {
                    continue;
                }

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                        continue;
                    }

                    // 더 높은 칸으로만 이동 가능
                    if (map[nr][nc] <= map[r][c]) {
                        continue;
                    }

                    int diff = map[nr][nc] - map[r][c];

                    // 등산로에서 발생한 인접한 높이 차의 최댓값 (지금까지의 최대 높이 차와 이번 이동 높이 차 중 큰 값)
                    int nextCost = Math.max(dp[r][c][len], diff);
                    
                    
                    // 다음 칸에 길이 len + 1로 도착하는 경우를 갱신
                    // 같은 칸, 같은 길이로 도착하는 방법이 여러 개 있을 수 있으므로 nextCost가 더 작은 값을 선택
                    dp[nr][nc][len + 1] = Math.min(dp[nr][nc][len + 1], nextCost);
                }
            }
        }

        int answer = INF;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                answer = Math.min(answer, dp[i][j][K]);
            }
        }

        if (answer == INF) {
            System.out.println(-1);
        } else {
            System.out.println(answer);
        }
    }
}