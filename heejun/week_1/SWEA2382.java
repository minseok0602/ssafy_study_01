import java.io.*;
import java.util.*;

public class SWEA2382 {

    static class Pair {
        int r, c, num, dir; // dir: 0~3 (상하좌우)
        Pair(int r, int c, int num, int dir) {
            this.r = r; this.c = c; this.num = num; this.dir = dir;
        }
    }

    static int N, M, K;
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static ArrayList<Pair> table;
    static ArrayList<Pair>[][] board;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());

            table = new ArrayList<>();
            board = new ArrayList[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) board[i][j] = new ArrayList<>();
            }

            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                int num = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken()) - 1; // 1~4 -> 0~3
                table.add(new Pair(r, c, num, dir));
            }

            while (M-- > 0) simulation();

            int total = 0;
            for (Pair p : table) total += p.num;
            sb.append('#').append(tc).append(' ').append(total).append('\n');
        }

        System.out.print(sb);
    }

    static void simulation() {
        // 1) 이번 턴 board 비우기 (필수)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) board[i][j].clear();
        }

        // 2) 이동 후 board에 담기 (소멸은 담지 않음)
        for (Pair p : table) {
            int nr = p.r + dr[p.dir];
            int nc = p.c + dc[p.dir];

            p.r = nr;
            p.c = nc;

            // 가장자리 약품 처리
            if (isEdge(nr, nc)) {
                p.num /= 2;
                p.dir = reverse(p.dir);
            }

            if (p.num == 0) continue; // 소멸

            board[nr][nc].add(p);
        }

        // 3) 합치기 후 table 재구성
        ArrayList<Pair> next = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                ArrayList<Pair> cell = board[i][j];
                int size = cell.size();
                if (size == 0) continue;

                if (size == 1) {
                    next.add(cell.get(0));
                    continue;
                }

                int sum = 0;
                Pair maxPair = null;

                for (Pair p : cell) {
                    sum += p.num;
                    if (maxPair == null || p.num > maxPair.num) maxPair = p;
                }

                // maxPair만 남기고 합친 값/방향 적용
                maxPair.num = sum;

                // cell 정리(선택사항: 디버깅용으로 일관성 유지)
                cell.clear();
                cell.add(maxPair);

                next.add(maxPair);
            }
        }

        table = next;
    }

    static boolean isEdge(int r, int c) {
        return r == 0 || r == N - 1 || c == 0 || c == N - 1;
    }

    static int reverse(int dir) {
        // 상(0)<->하(1), 좌(2)<->우(3)
        if (dir == 0) return 1;
        if (dir == 1) return 0;
        if (dir == 2) return 3;
        return 2;
    }
}
