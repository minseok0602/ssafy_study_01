package week_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_2382 {

    static final int[] dr = {0, -1, 1, 0, 0};
    static final int[] dc = {0, 0, 0, -1, 1};

    static int reverseDir(int d) {
        if (d == 1) return 2;
        if (d == 2) return 1;
        if (d == 3) return 4;
        return 3;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int K = Integer.parseInt(st.nextToken());

            int[] r = new int[K];
            int[] c = new int[K];
            int[] cnt = new int[K];
            int[] dir = new int[K];

            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());
                r[i] = Integer.parseInt(st.nextToken());
                c[i] = Integer.parseInt(st.nextToken());
                cnt[i] = Integer.parseInt(st.nextToken());
                dir[i] = Integer.parseInt(st.nextToken());
            }

            int size = N * N;
            int[] sum = new int[size];
            int[] max = new int[size];
            int[] dirPick = new int[size];
            boolean[] touched = new boolean[size];
            int[] touchedKeys = new int[size];

            for (int t = 0; t < M; t++) {
                int touchedCnt = 0;

                for (int i = 0; i < K; i++) {
                    if (cnt[i] == 0) continue;

                    int nr = r[i] + dr[dir[i]];
                    int nc = c[i] + dc[dir[i]];
                    int ncnt = cnt[i];
                    int ndir = dir[i];

                    if (nr == 0 || nc == 0 || nr == N - 1 || nc == N - 1) {
                        ncnt /= 2;
                        ndir = reverseDir(ndir);
                        if (ncnt == 0) {
                            cnt[i] = 0;
                            continue;
                        }
                    }

                    int key = nr * N + nc;

                    if (!touched[key]) {
                        touched[key] = true;
                        touchedKeys[touchedCnt++] = key;
                        sum[key] = 0;
                        max[key] = 0;
                        dirPick[key] = 0;
                    }

                    sum[key] += ncnt;
                    if (ncnt > max[key]) {
                        max[key] = ncnt;
                        dirPick[key] = ndir;
                    }
                }

                int newK = 0;

                for (int idx = 0; idx < touchedCnt; idx++) {
                    int key = touchedKeys[idx];
                    int total = sum[key];
                    if (total > 0) {
                        r[newK] = key / N;
                        c[newK] = key % N;
                        cnt[newK] = total;
                        dir[newK] = dirPick[key];
                        newK++;
                    }
                    touched[key] = false;
                    sum[key] = 0;
                    max[key] = 0;
                    dirPick[key] = 0;
                }

                for (int i = newK; i < K; i++) cnt[i] = 0;
                K = newK;
            }

            int ans = 0;
            for (int i = 0; i < K; i++) ans += cnt[i];

            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb.toString());
    }
}