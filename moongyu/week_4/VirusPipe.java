package week_4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class VirusPipe {
    static int n, k, answer;
    static ArrayList<int[]>[] graph;
    public int solution(int n, int infection, int[][] edges, int k) {
        this.n = n;
        this.k = k;
        answer = 1;

        graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] is : edges) {
            int from = is[0];
            int to = is[1];
            int type = is[2];
            graph[from].add(new int[] {to, type});
            graph[to].add(new int[] {from, type});
        }

        boolean[] infected = new boolean[n + 1];
        infected[infection] = true;

        dfs(0, infected);

        return answer;
    }

    static void dfs(int count, boolean[] infected) {
        answer = Math.max(answer, countInfection(infected));

        if (count == k) return;

        for (int type = 1; type <= 3; type++) {
            boolean[] next = spread(type, infected);
            dfs(count + 1, next);
        }
    }

    static boolean[] spread(int pipe, boolean[] infected) {
        boolean[] next = infected.clone();
        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 1; i <= n; i++) if (infected[i]) queue.add(i);

        while(!queue.isEmpty()) {
            int curNode = queue.poll();

            for (int[] nextNode : graph[curNode]) {
                if (nextNode[1] != pipe || next[nextNode[0]]) continue;

                next[nextNode[0]] = true;
                queue.add(nextNode[0]);
            }
        }

        return next;
    }

    static int countInfection(boolean[] inf) {
        int cnt = 0;
        for (boolean b : inf) if (b) ++cnt;
        return cnt;
    }
}
