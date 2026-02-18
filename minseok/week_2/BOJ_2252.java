package com.ssafy;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static ArrayList<ArrayList<Integer>> graph;
    static boolean[] visit;
    static int n,m;
    static StringBuilder sb = new StringBuilder();
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        graph = new ArrayList<>();
        for(int i = 0;i<=n;i++)
            graph.add(new ArrayList<>());
        for(int i = 0;i<m;i++){
            st = new StringTokenizer(br.readLine());
            // a가 b의 앞에 서있어야 한다.
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            // 일부러 반대로 저장
            graph.get(b).add(a);
        }
        visit = new boolean[n+1];
        for(int i =1 ;i<=n;i++){
            if(visit[i]) continue;
            visit[i] = true;
            dfs(i);
        }
        System.out.println(sb);

    }
    static void dfs(int idx){
        if(idx>=n+1)
            return;
        for(int i : graph.get(idx)){
            if(visit[i]) continue;
            visit[i] = true;
            dfs(i);
        }
        // 그래프에 반대로 저장을 했기 때문에, 본인의 그래프 안에 있는 것들이 먼저 출력이 되고 나서, 맨 마지막에 본인이 출력된다
        // a가 b보다 앞서야할 때, b의 그래프에 a가 저장이 되어있기 때문에, a가 먼저 출력이 되고, 그 후에 b가 출력이 된다.
        sb.append(idx).append(" ");
    }

}
