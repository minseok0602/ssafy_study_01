package week_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA_5644 {
    static final int[] dr = {0, -1, 0, 1, 0};
    static final int[] dc = {0, 0, 1, 0, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int M = Integer.parseInt(st.nextToken());
            int A = Integer.parseInt(st.nextToken());

            int[] moveA = new int[M];
            int[] moveB = new int[M];

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) moveA[i] = Integer.parseInt(st.nextToken());

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) moveB[i] = Integer.parseInt(st.nextToken());

            int[] bcR = new int[A];
            int[] bcC = new int[A];
            int[] bcCov = new int[A];
            int[] bcPow = new int[A];

            for (int i = 0; i < A; i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                bcC[i] = x;
                bcR[i] = y;
                bcCov[i] = Integer.parseInt(st.nextToken());
                bcPow[i] = Integer.parseInt(st.nextToken());
            }

            int ar = 1, ac = 1;
            int brp = 10, bcp = 10;

            int ans = 0;

            for (int t = 0; t <= M; t++) {
                int best = 0;

                for (int i = -1; i < A; i++) {
                    int aVal = 0;
                    if (i != -1) {
                        int distA = Math.abs(ar - bcR[i]) + Math.abs(ac - bcC[i]);
                        if (distA <= bcCov[i]) aVal = bcPow[i];
                        else continue;
                    }

                    for (int j = -1; j < A; j++) {
                        int bVal = 0;
                        if (j != -1) {
                            int distB = Math.abs(brp - bcR[j]) + Math.abs(bcp - bcC[j]);
                            if (distB <= bcCov[j]) bVal = bcPow[j];
                            else continue;
                        }

                        int total;
                        if (i != -1 && i == j) total = bcPow[i];
                        else total = aVal + bVal;

                        if (total > best) best = total;
                    }
                }

                ans += best;

                if (t == M) break;

                int mdA = moveA[t];
                int mdB = moveB[t];

                ar += dr[mdA];
                ac += dc[mdA];
                brp += dr[mdB];
                bcp += dc[mdB];
            }

            sb.append("#").append(tc).append(" ").append(ans).append("\n");
        }

        System.out.print(sb.toString());
    }
}
