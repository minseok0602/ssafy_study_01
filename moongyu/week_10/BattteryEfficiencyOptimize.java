import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class BattteryEfficiencyOptimize {

    static class Module {
        boolean[][] pos; // 이 모듈이 사용하는 칸 정보
        int score;        // 이 모듈의 점수

        Module(boolean[][] pos, int score) {
            this.pos = pos;
            this.score = score;
        }
    }

    static int N, M;
    static int[][] map;
    static boolean[][] selected;

    static ArrayList<Module> modules = new ArrayList<>();

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        selected = new boolean[N][M];

        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());

            for (int c = 0; c < M; c++) {
                map[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        // 모든 칸을 시작점으로 잡고 연결된 5칸짜리 모듈을 DFS로 만듬
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                selected[r][c] = true;
                dfs(1, map[r][c]);
                selected[r][c] = false;
            }
        }

        int answer = Integer.MIN_VALUE;

        // 만들어진 모듈 후보 중 2개 선택
        for (int i = 0; i < modules.size(); i++) {
            for (int j = i + 1; j < modules.size(); j++) {
                Module a = modules.get(i);
                Module b = modules.get(j);

                int overlap = overlapCount(a.pos, b.pos);

                // 두 모듈은 정확히 2칸만 겹침.
                // 2개만 겹친다고 했을 때, 두 모듈의 스코어를 더
                if (overlap == 2) {
                    int score = a.score + b.score;
                    answer = Math.max(answer, score);
                }
            }
        }

        System.out.println(answer);
    }

    // count : 현재까지 선택한 칸 수
    // sum   : 현재까지 선택한 칸들의 점수 합
    static void dfs(int count, int sum) {

        // 5칸을 선택 시 module 하나 완성
        if (count == 5) {
        	// selected는 계속 바뀌니까 module 저장할 때 복사해서 저장
            boolean[][] copy = new boolean[N][M];
            
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    copy[r][c] = selected[r][c];
                }
            }
            
            modules.add(new Module(copy, sum));
            return;
        }

        // 현재 선택된 칸들 주변으로 확장
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {

                // 선택된 칸만 확장 가능
                if (!selected[r][c]) {
                    continue;
                }

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    // 격자 밖이면 무시
                    if (nr < 0 || nr >= N || nc < 0 || nc >= M || selected[nr][nc]) {
                        continue;
                    }
                    
                    // 백트래킹
                    selected[nr][nc] = true;
                    dfs(count + 1, sum + map[nr][nc]);
                    selected[nr][nc] = false;
                }
            }
        }
    }

    /*
     * 두 모듈이 몇 칸 겹치는지 계산
     */
    static int overlapCount(boolean[][] a, boolean[][] b) {
        int count = 0;

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (a[r][c] && b[r][c]) {
                    count++;
                }
            }
        }

        return count;
    }
}