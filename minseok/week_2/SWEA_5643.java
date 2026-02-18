package com.ssafy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class Solution {
    static int n, m;
    static ArrayList<ArrayList<Integer>> short_graph;
    static ArrayList<ArrayList<Integer>> tall_graph;
    // short_info[i] : i가 앞서야 하는 노드들의 집합
    static HashSet<Integer>[] short_info;
    // tall_info[i] : i가 뒤에 와야하는 노드들의 집합
    static HashSet<Integer>[] tall_info;
    static int answer;
    static boolean[][] visit;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();

        for (int tc = 1; tc <= T; tc++) {
            sb.append("#").append(tc).append(" ");
            n = Integer.parseInt(br.readLine().trim());
            m = Integer.parseInt(br.readLine().trim());

            short_graph = new ArrayList<>();
            tall_graph = new ArrayList<>();
            short_info = new HashSet[n + 1];
            tall_info  = new HashSet[n + 1];

            for (int i = 0; i <= n; i++) {
                short_graph.add(new ArrayList<>());
                tall_graph.add(new ArrayList<>());
                short_info[i] = new HashSet<>();
                tall_info[i]  = new HashSet<>();
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                // a b : a가 b보다 작다
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                // a가 b보다 앞서야함
                short_graph.get(a).add(b);
                // b가 a보다 뒤에 있어야함
                tall_graph.get(b).add(a);
            }

            for (int i = 1; i <= n; i++) {
                visit = new boolean[n + 1][2];

                visit[i][0] = true;
                short_dfs(i, i);

                visit[i][1] = true;
                tall_dfs(i, i);
            }

            answer = 0;
            for (int i = 1; i <= n; i++) {
                // 특정 노드 i에 대해서, 본인보다 앞에 있어야하는 노드들과 뒤에 있어야하는 노드들의 개수가, 본인을 제외한 모든 노드의 수면 본인의 위치를 알 수 있음
                if (short_info[i].size() + tall_info[i].size() == n - 1) answer++;
            }

            sb.append(answer).append("\n");
        }
        System.out.print(sb);
    }

    static void short_dfs(int parent, int idx) {
        for (int i : short_graph.get(idx)) {
            if (i != parent) short_info[parent].add(i);
            if (visit[i][0]) continue;
            visit[i][0] = true;
            // ex) short_graph에서, a 집합의 원소에 b가 있다 -> a가 b보다 앞서야 한다.
            // b의 short_graph도 보자 -> b 집합의 원소에 c가 있다 -> b가 c보다 앞서야 한다.
            // 결과적으로, a가 b보다 앞서고, b가 c보다 앞서야하니까 a가 c보다 앞서야 한다.
            short_dfs(parent, i);
        }
    }

    static void tall_dfs(int parent, int idx) {
        for (int i : tall_graph.get(idx)) {
            if (i != parent) tall_info[parent].add(i);
            if (visit[i][1]) continue;
            visit[i][1] = true;
            tall_dfs(parent, i);
        }
    }
}
