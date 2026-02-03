package com.ssafy.test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
class Solution
{
    public static class BC{
        int coverage;
        int x;
        int y;
        int performance;
        public BC(int coverage, int x, int y, int performance) {
            this.coverage = coverage;
            this.x = x;
            this.y = y;
            this.performance = performance;
        }
    }
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int[] a_moves;
    static int[] b_moves;
    static BC[] bcs;
    static int[][] dir = {{0,0},{0,-1},{1,0},{0,1},{-1,0}};
    public static void main(String args[]) throws Exception
    {
        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();

        for(int test_case = 1; test_case <= T; test_case++)
        {
            sb.append("#").append(test_case).append(' ');
            StringTokenizer st =new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            a_moves = new int[n+1];
            b_moves = new int[n+1];
            bcs = new BC[m];
            StringTokenizer sta =new StringTokenizer(br.readLine());
            StringTokenizer stb =new StringTokenizer(br.readLine());
            for(int i =1;i<=n;i++) {
                a_moves[i] = Integer.parseInt(sta.nextToken());
                b_moves[i] = Integer.parseInt(stb.nextToken());
            }
            for(int i = 0;i<m;i++) {
                st =new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int coverage = Integer.parseInt(st.nextToken());
                int performance = Integer.parseInt(st.nextToken());
                bcs[i] = new BC(coverage,x,y,performance);
            }
            int sum = 0;
            int cur_a_x = 1;
            int cur_a_y = 1;
            int cur_b_x = 10;
            int cur_b_y = 10;
            for(int i = 0;i<=n;i++) {
                cur_a_x+=dir[a_moves[i]][0];
                cur_a_y+=dir[a_moves[i]][1];
                cur_b_x+=dir[b_moves[i]][0];
                cur_b_y+=dir[b_moves[i]][1];
                // 현재 a의 위치에 따라, 충전할 수 있는 충전소들의 집합
                ArrayList<BC> a_bcs = new ArrayList<>();

                // 현재 b의 위치에 따라, 충전할 수 있는 충전소들의 집합
                ArrayList<BC> b_bcs = new ArrayList<>();
                for(int j=0;j<m;j++) {
                    if(can_charge(bcs[j],cur_a_x,cur_a_y)) {
                        a_bcs.add(bcs[j]);
                    }
                    if(can_charge(bcs[j],cur_b_x,cur_b_y)) {
                        b_bcs.add(bcs[j]);
                    }
                }
                a_bcs.sort((o1,o2)->o2.performance - o1.performance);
                b_bcs.sort((o1,o2)->o2.performance - o1.performance);
                if(a_bcs.isEmpty()&&b_bcs.isEmpty()) {
                    continue;
                }
                if(b_bcs.isEmpty()) {
                    sum+=a_bcs.get(0).performance;
                    continue;
                }
                if(a_bcs.isEmpty()) {
                    sum+=b_bcs.get(0).performance;
                    continue;
                }

                // 서로 우선 순위가 겹치면 이제부터 선택을 해야함
                if(a_bcs.get(0)==b_bcs.get(0)) {
                    if(a_bcs.size()==1&&b_bcs.size()==1) {
                        sum+=a_bcs.get(0).performance;
                        continue;
                    }
                    if(b_bcs.size()==1) {
                        sum+=a_bcs.get(1).performance+b_bcs.get(0).performance;
                        continue;
                    }
                    if(a_bcs.size()==1) {
                        sum+=b_bcs.get(1).performance+a_bcs.get(0).performance;
                        continue;
                    }

                    // 둘다 사이즈가 1이 아닐 때
                    // a와 b가 각각 어떤 충전소를 썼을 때 더 이득인지 계산해봐야함
                    sum+=Math.max(b_bcs.get(1).performance+a_bcs.get(0).performance, a_bcs.get(1).performance+b_bcs.get(0).performance);
                    continue;
                }
                // 서로 충돌나지 않으면 그냥 각자 쓰고 싶은 충전소 쓰기
                sum+=(a_bcs.get(0).performance+b_bcs.get(0).performance);

            }
            sb.append(sum).append('\n');
        }
        System.out.println(sb);
    }
    public static boolean can_charge(BC b, int x, int y) {
        int distance = Math.abs(b.x-x) + Math.abs(b.y-y);
        return b.coverage >= distance;
    }
}

