package com.ssafy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

class Solution {
    static int n;
    static int[][] arr,prefix;
    static int answer;
    static boolean[][] visit;
    static int[][] dir = {{0,-1},{0,1},{-1,0},{1,0}};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();

        for (int test_case = 1; test_case <= T; test_case++) {
            sb.append("#").append(test_case).append(" ");
            n = Integer.parseInt(br.readLine().trim());
            arr = new int[n][n];
            prefix = new int[n][n];
            for(int i = 0;i<n;i++){
                Arrays.fill(prefix[i],Integer.MAX_VALUE);
            }
            visit = new boolean[n][n];
            answer = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                String temp = br.readLine();
                for(int j = 0;j<n;j++){
                    arr[i][j] = temp.charAt(j)-'0';
                }
            }
            bfs();
            sb.append(answer).append("\n");
        }
        System.out.print(sb);
    }
    public static void bfs(){
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0,0,0});
        while(!queue.isEmpty()){
            int[]cur = queue.poll();
            if(cur[0]==n-1&&cur[1]==n-1){
                answer = Math.min(answer,cur[2]);
                continue;
            }
            for(int i = 0;i<4;i++){
                int nx = cur[0] + dir[i][0];
                int ny = cur[1] + dir[i][1];
                if(nx<0||ny<0||nx>=n||ny>=n) continue;
                // prefix[nx][ny] : 원점에서 nx,ny로 가는데 소모하는 최소 비용
                // 만약 현재까지 저장된 최단 경로보다, 지금 찾은 길의 비용이 더 싸다면 비용 갱신
                if(prefix[nx][ny]>cur[2]+arr[nx][ny]){
                    queue.add(new int[]{nx,ny,cur[2]+arr[nx][ny]});
                    prefix[nx][ny] = cur[2] + arr[nx][ny];
                }
            }
        }
    }
}