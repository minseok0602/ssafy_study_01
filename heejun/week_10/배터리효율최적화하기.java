package studt_s;

import java.io.*;
import java.util.*;

public class 배터리효율최적화하기 {

    static class Point { // 셀하나의 좌표
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int N, M;
    static int[][] map;
    static boolean[][] selected;
    static List<List<Point>> modules = new ArrayList<>(); // 가능한 모듈들의 집합

    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};

    static int answer = Integer.MIN_VALUE;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        selected = new boolean[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dfs(0, 0);

        for (int i = 0; i < modules.size(); i++) {
            for (int j = i + 1; j < modules.size(); j++) {
                checkTwoModules(modules.get(i), modules.get(j));
            }
        }

        System.out.println(answer);
    }

    // 전체 칸 중에서 5칸 고르기
    static void dfs(int start, int count) {

        if (count == 5) {

            if (isConnected()) {
                saveModule();
            }

            return;
        }

        for (int i = start; i < N * M; i++) {
            int r = i / M;
            int c = i % M;
            
            // 모든 조합 찾기
            selected[r][c] = true;
            dfs(i + 1, count + 1);
            selected[r][c] = false;
        }
    }

    // 선택한 5칸이 연결되어 있는지 확인
    static boolean isConnected() {

        boolean[][] visited = new boolean[N][M];
        Queue<Point> q = new ArrayDeque<>();

        outer:
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {

                if (selected[i][j]) {
                    q.offer(new Point(i, j));
                    visited[i][j] = true;
                    break outer;
                }
            }
        }

        int count = 0;

        while (!q.isEmpty()) {

            Point cur = q.poll();

            int x = cur.x;
            int y = cur.y;

            count++;

            for (int d = 0; d < 4; d++) {

                int nx = x + dx[d];
                int ny = y + dy[d];

                if (nx < 0 || ny < 0 || nx >= N || ny >= M) continue;
                if (visited[nx][ny]) continue;
                if (!selected[nx][ny]) continue;

                visited[nx][ny] = true;
                q.offer(new Point(nx, ny));
            }
        }

        return count == 5;
    }

    // 연결된 5칸짜리 모듈 저장
    static void saveModule() {

        List<Point> module = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {

                if (selected[i][j]) {
                    module.add(new Point(i, j));
                }
            }
        }

        modules.add(module);
    }

    // 두 모듈이 정확히 2칸 겹치는지 확인
    static void checkTwoModules(List<Point> a, List<Point> b) {

        Set<String> set = new HashSet<>();
        int sum = 0;

        for (Point p : a) {
            sum += map[p.x][p.y];
            set.add(p.x + "," + p.y);
        }

        for (Point p : b) {
            sum += map[p.x][p.y];
            set.add(p.x + "," + p.y);
        }

        // 5 + 5 = 10칸인데, 겹친 칸이 2개면 실제 서로 다른 칸은 8개
        if (set.size() == 8) {
            answer = Math.max(answer, sum);
        }
    }
}