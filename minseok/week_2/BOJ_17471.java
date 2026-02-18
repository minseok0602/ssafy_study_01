package com.ssafy;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int[] arr;
    static ArrayList<ArrayList<Integer>> graph;
    static boolean[] visit;
    static int n;
    static int answer;
    public static void main(String[] args) throws Exception {
        n = Integer.parseInt(br.readLine());
        arr = new int[n+1];
        visit = new boolean[n+1];
        graph = new ArrayList<>();
        answer = Integer.MAX_VALUE;
        StringTokenizer st = new StringTokenizer(br.readLine());
        for(int i = 0;i<n;i++){
            arr[i+1] = Integer.parseInt(st.nextToken());
            graph.add(new ArrayList<>());
        }
        graph.add(new ArrayList<>());
        for(int i = 1;i<=n;i++){
            st = new StringTokenizer(br.readLine());
            int cnt = Integer.parseInt(st.nextToken());
            for(int j =0;j<cnt;j++){
                int node = Integer.parseInt(st.nextToken());
                graph.get(i).add(node);
            }
        }
        // 서로 연결된 그래프의 개수 찾기
        int group_cnt = 0;
        for(int i = 1;i<=n;i++){
            if(!visit[i]){
                visit[i] = true;
                // 서로 연결된 그래프들은 dfs를 통해서 방문처리가 되어있을 것임
                group_cnt++;
                // 서로 연결된 그래프가 3개 이상? 그럼 선거구를 두개로 나눌 수 없음 -> -1 리턴
                if(group_cnt>=3){
                    answer = -1;
                    break;
                }
                dfs(i);
            }
        }
        // 만약 그룹이 2개라면, 정답이 이미 정해져있음
        if(group_cnt==2){
            visit = new boolean[n+1];
            visit[1] = true;
            dfs(1);
            int a_cnt = 0;
            int b_cnt = 0;
            for(int i =  1;i<=n;i++){
                if(visit[i])
                    a_cnt+=arr[i];
                else
                    b_cnt+=arr[i];
            }
            answer = Math.abs(a_cnt-b_cnt);
        }
        // 지금부터가 관건
        // 그래프 내의 집합을 두개로 나누되, 나눴을 때 각 그래프 내의 노드들이 서로 연결되어 있는지 확인해야함
        else if(group_cnt==1){
            // 1은 그룹 1로 고정시키고 시작
            separate(2, 1<<1);
        }
        if (answer == Integer.MAX_VALUE) answer = -1;
        System.out.println(answer);
    }
    static void dfs(int idx){
        for(int i : graph.get(idx)){
            if(visit[i]) continue;
            visit[i] = true;
            dfs(i);
        }
    }
    static void separate(int idx, int bit){
        if(idx>=n+1){
            // 전부 다 1번 노드와 같은 그룹이라면 그룹이 1개밖에 없으니까 제외
            if(bit == (1<<(n+1)) - 2)
                return;
            visit = new boolean[n+1];
            visit[1] = true;
            // 1과 연결된 그래프를 순회하되, 1과 똑같은 비트를 가진 노드에 대해서만 순회함
            // 1과 연결되고, 1과 똑같은 비트를 가진 노드들은 방문처리가 됨
            check_graph(1,bit,bit&1<<1);
            int cnt = 0;

            // 정상적으로 그래프가 두 그룹으로 나누어졌다면, 현재 방문처리가 되지 않은 그래프는, 그들끼리 서로 연결이 되어있어야 함
            for(int i =2;i<=n;i++){
                if(!visit[i]){
                    visit[i] = true;
                    cnt++;
                    // cnt가 2이상이다 -> 그래프가 2개보다 많아졌다 -> 각 그래프의 노드들이 서로 연결되어있지 않음
                    if(cnt==2)
                        return;
                    // i와 동일한 그룹에 대해서만 dfs로 그래프 따라감
                    check_graph(i,bit,bit&1<<i);
                }
            }
            // 여기까지 왔다는 건, 정상적으로 두개가 나뉘어진 거임
            int a_cnt = 0;
            int b_cnt = 0;
            // 그래프의 비트값이 서로 같은 것들끼리 인구수를 합산
            for(int i =  1;i<=n;i++){
                if((bit & (1<<i)) != 0)
                    a_cnt+=arr[i];
                else
                    b_cnt+=arr[i];
            }
            answer = Math.min(answer,Math.abs(a_cnt-b_cnt));
            return;
        }

        // 특정 노드가 A소속인 경우, B소속인 경우를 두어 분기
        separate(idx+1,bit|1<<idx);
        separate(idx+1, bit);
    }
    static void check_graph(int idx, int bit, int flag){
        for(int i : graph.get(idx)){
            // 기존의 dfs에서 , flag값이 추가된 버전
            // 그래프를 따라가되, flag가 같은 값들에 대해서만 순회
            if(!visit[i] && (((bit & (1<<i)) != 0) == (flag != 0))){
                visit[i] = true;
                check_graph(i,bit,flag);
            }

        }
    }
}
