package com.ssafy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Solution {
    static int n,m,k;
    static int[][] arr;
    static int answer;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();

        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            k = Integer.parseInt(st.nextToken());
            arr = new int[n][m];
            answer = Integer.MAX_VALUE;
            for(int i = 0;i<n;i++){
                st = new StringTokenizer(br.readLine());
                for(int j = 0;j<m;j++){
                    arr[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            dfs(0,0);
            sb.append("#").append(tc).append(" ").append(answer+"\n");
        }
        System.out.print(sb);
    }
    static void dfs(int cnt,int idx){
        if(cnt>=answer) return;
        if(check()){
            answer = cnt;
            return;
        }
        if(idx>=n){
            return;
        }
        for(int i = idx;i<n;i++){
            // 원본 임시 저장
            int[] old_arr = new int[m];
            for(int j = 0;j<m;j++){
                old_arr[j] = arr[i][j];
            }
            // 특정 행 0으로 교체
            for(int j = 0;j<m;j++){
                arr[i][j] = 0;
            }
            // 0으로 교체한 뒤 다음 dfs 실행 -> 특정 행을 약품 처리한 효과
            dfs(cnt+1,i+1);
            for(int j = 0;j<m;j++){
                arr[i][j] = 1;
            }
            // 1로 교체한 뒤 다음 dfs 실행
            dfs(cnt+1,i+1);
            // 원복
            arr[i] = old_arr;
        }
    }
    // 모든 열에 대해서, 연속으로 k번 이상 같은 값이 나오는지 확인
    static boolean check(){
        for(int j = 0;j<m;j++){
            int cnt = 1;
            int pivot = arr[0][j];
            for(int i = 1;i<n;i++){
                if(pivot!=arr[i][j]){
                    cnt = 1;
                    pivot = arr[i][j];
                }
                else
                    cnt++;
                if(cnt==k)
                    break;
            }
            if(cnt<k)
                return false;
        }
        return true;
    }
}
