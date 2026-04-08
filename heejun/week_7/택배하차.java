import java.io.*;
import java.util.*;

public class Main {
    static class Box {
        int k;      // 택배 번호
        int h, w;   // 높이, 너비
        int r, c;   // 좌상단 좌표(0-index)
        boolean removed; // 제거 여부

        Box(int k, int h, int w, int r, int c) {
            this.k = k;
            this.h = h;
            this.w = w;
            this.r = r;
            this.c = c;
            this.removed = false;
        }
    }

    static int N, M;
    static int[][] A; // 격자
    static List<Box> boxes = new ArrayList<>();

    // 방향: 아래, 왼쪽, 오른쪽
    static int[] dx = {1, 0, 0};
    static int[] dy = {0, -1, 1};

    static boolean inRange(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < N;
    }

    // 한 칸 이동 가능한지 검사
    static boolean canPut(int h, int w, int r, int c, int d) {
        int r1 = r, r2 = r + h - 1, c1 = c, c2 = c + w - 1;

        if (d == 0) r1 = r + h - 1;   // 아래 이동이면 맨 아래 줄만 검사
        else if (d == 1) c2 = c;      // 왼쪽 이동이면 맨 왼쪽 줄만 검사
        else c1 = c + w - 1;          // 오른쪽 이동이면 맨 오른쪽 줄만 검사

        for (int i = r1; i <= r2; i++) {
            for (int j = c1; j <= c2; j++) {
                if (!inRange(i, j) || A[i][j] != 0) return false;
            }
        }
        return true;
    }

    // 해당 방향으로 끝까지 이동
    static int[] moveBox(int h, int w, int r, int c, int d) {
        int rr = r, cc = c;

        while (true) {
            int nr = rr + dx[d];
            int nc = cc + dy[d];

            if (canPut(h, w, nr, nc, d)) {
                rr = nr;
                cc = nc;
            } else {
                break;
            }
        }

        return new int[]{rr, cc};
    }

    // 격자에서 박스 제거
    static void removeBox(Box b) {
        b.removed = true;
        for (int i = b.r; i < b.r + b.h; i++) {
            for (int j = b.c; j < b.c + b.w; j++) {
                A[i][j] = 0;
            }
        }
    }

    // 격자에 박스 배치
    static void putBox(Box b) {
        b.removed = false;
        for (int i = b.r; i < b.r + b.h; i++) {
            for (int j = b.c; j < b.c + b.w; j++) {
                A[i][j] = b.k;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        A = new int[N][N];

        // 초기 적재
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int k = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());

            int r0 = 0;
            int c0 = c1 - 1; // 1-index -> 0-index

            int[] res = moveBox(h, w, r0, c0, 0); // 아래로 끝까지 낙하
            Box b = new Box(k, h, w, res[0], res[1]);
            boxes.add(b);
            putBox(b);
        }

        // 하차 우선순위: 번호 작은 순
        boxes.sort(Comparator.comparingInt(b -> b.k));

        for (int turn = 0; turn < M; turn++) {
            boolean isLeft = (turn % 2 == 0); // 짝수 턴: 왼쪽, 홀수 턴: 오른쪽

            // 1. 하차할 박스 찾기
            for (Box b : boxes) {
                if (b.removed) continue;

                removeBox(b); // 자기 자신 제거 후 이동 검사
                int[] rc = moveBox(b.h, b.w, b.r, b.c, isLeft ? 1 : 2);
                int c = rc[1];

                if ((isLeft && c == 0) || (!isLeft && c + b.w == N)) {
                    System.out.println(b.k); // 하차
                    break;
                } else {
                    putBox(b); // 하차 못 하면 원복
                }
            }

            // 2. 남은 박스 낙하 정리
            boxes.sort((a, b) -> Integer.compare((b.r + b.h), (a.r + a.h))); // 아래쪽 박스부터

            for (Box b : boxes) {
                if (b.removed) continue;

                removeBox(b);
                int[] fall = moveBox(b.h, b.w, b.r, b.c, 0);
                b.r = fall[0];
                b.c = fall[1];
                putBox(b);
            }

            // 다음 턴 위해 다시 번호순 정렬
            boxes.sort(Comparator.comparingInt(b -> b.k));
        }
    }
}