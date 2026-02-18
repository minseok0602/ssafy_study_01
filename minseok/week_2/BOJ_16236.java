package com.ssafy;

import java.io.*;
import java.util.*;

class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n;
    static int[][] map;
    static boolean[][] visit;
    static int[][] dir = {{-1,0},{0,-1},{0,1},{1,0}};
    static int answer;
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        map = new int[n][n];
        int shark_x = 0,shark_y=0;
        answer = 0;
        for(int i =0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0;j<n;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
                if(map[i][j]==9){
                    shark_x = i;
                    shark_y = j;
                    map[i][j] = 0;
                }
            }
        }
        int shark_size = 2;
        int cur_cnt = 0;
        while(true){
            int[] fish_info = bfs(shark_x,shark_y,shark_size);
            if(fish_info==null)
                break;
            cur_cnt++;
            if(cur_cnt==shark_size){
                cur_cnt = 0;
                shark_size++;
            }
            map[fish_info[0]][fish_info[1]] = 0;
            shark_x = fish_info[0];
            shark_y = fish_info[1];
            answer += fish_info[2];
            // 물고기를 찾을 때까지 계속 진행
            // 물고기를 찾았다면?
            // 현재 아기상어의 잡어먹은 횟수를 고려해서, 사이즈를 키울지 말지 구현
            // 물고기를 잡아먹고나서 해당 배열의 위치를 0으로 고치고 다음 bfs 진행
        }
        System.out.println(answer);
    }
    //아기상어가 다음 물고기를 찾으러 감
    static int[] bfs(int start_x, int start_y, int start_size){
        visit = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start_x,start_y,0});
        visit[start_x][start_y] = true;
        // 다음 잡아먹을 물고기의 후보
        ArrayList<int[]> candidates = new ArrayList<>();
        // 다음 잡아먹을 물고기와의 거리
        int distance = Integer.MAX_VALUE;
        while(!queue.isEmpty()){
            int [] cur = queue.poll();
            int cur_x = cur[0];
            int cur_y = cur[1];
            // 너비 우선 탐색으로 잡을 물고기를 찾은 상태라면?
            if(cur[2]+1>distance)
                break;
            for(int i = 0 ;i<4;i++){
                int new_x = cur_x + dir[i][0];
                int new_y = cur_y + dir[i][1];
                if(new_x<0||new_y<0||new_x>=n||new_y>=n) continue;
                if(visit[new_x][new_y]) continue;
                if(map[new_x][new_y]>start_size) continue;
                visit[new_x][new_y] = true;
                // 잡아먹을 수 있는 물고기를 발견함
                if(map[new_x][new_y]>0&&map[new_x][new_y]<start_size){
                    distance = cur[2]+1;
                    candidates.add(new int[]{new_x,new_y,cur[2]+1});
                }
                queue.add(new int[]{new_x,new_y,cur[2]+1});
            }
        }
        // 물고리를 x와 y 기준으로 정렬
        candidates.sort((o1,o2)->{
            if(o1[0]==o2[0])
                return o1[1] - o2[1];
            return o1[0] - o2[0];
        });
        return candidates.isEmpty()?null:candidates.get(0);
    }
}
