package com.ssafy.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class Solution
{
    // up,down,left,right
    static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;

    static int n;
    static int[][] map;
    static int[][][] warm_holes;
    static boolean[] filled;
    static int answer;

    public static void main(String args[]) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();

        for(int test_case = 1; test_case <= T; test_case++)
        {
            sb.append("#").append(test_case).append(" ");

            n = Integer.parseInt(br.readLine().trim());

            map = new int[n][n];
            warm_holes = new int[5][2][2];
            filled = new boolean[5];

            for(int i = 0;i<n;i++){
                StringTokenizer st = new StringTokenizer(br.readLine().trim());
                for(int j =0 ;j<n;j++){
                    map[i][j] = Integer.parseInt(st.nextToken());

                    // 웜홀 저장
                    if(map[i][j] >= 6 && map[i][j] <= 10) {
                        if(filled[map[i][j]-6])
                            warm_holes[map[i][j]-6][1] = new int[]{i,j};
                        else{
                            filled[map[i][j]-6] = true;
                            warm_holes[map[i][j]-6][0] = new int[]{i,j};
                        }
                    }
                }
            }

            answer = 0;

            for(int i=0;i<n;i++) {
                for(int j =0 ;j<n;j++) {
                    if(map[i][j]==0) {
                        for(int k=0;k<4;k++) {
                            answer = Math.max(answer, play_game(i,j,k));
                        }
                    }
                }
            }

            sb.append(answer).append("\n");
        }

        System.out.println(sb);
    }

    static int play_game(int start_x, int start_y, int start_dir) {
        int cur_x = start_x;
        int cur_y = start_y;
        int cur_dir = start_dir;
        int cur_point = 0;

        while(true) {
            int next_x = cur_x + dir[cur_dir][0];
            int next_y = cur_y + dir[cur_dir][1];
            if(next_x==start_x && next_y==start_y)
                return cur_point;
            if(next_x<0||next_y<0||next_x>=n||next_y>=n){
                cur_point++;
                cur_dir^=1;
                cur_x -= dir[cur_dir][0];
                cur_y -= dir[cur_dir][1];
                continue;
            }
            if(map[next_x][next_y]==-1) {
                return cur_point;
            }
            int next_shape = map[next_x][next_y];

            if(next_shape>=1 && next_shape<=5){
                cur_point++;
                switch(next_shape){
                    case 1 : switch(cur_dir){
                        case UP : cur_dir = DOWN; break;
                        case DOWN : cur_dir = RIGHT; break;
                        case LEFT : cur_dir = UP; break;
                        case RIGHT : cur_dir = LEFT; break;
                        }
                    break;
                    case 2 : switch(cur_dir){
                        case UP : cur_dir = RIGHT; break;
                        case DOWN : cur_dir = UP; break;
                        case LEFT : cur_dir = DOWN; break;
                        case RIGHT : cur_dir = LEFT; break;
                        }
                        break;
                    case 3 : switch(cur_dir){
                        case UP : cur_dir = LEFT; break;
                        case DOWN : cur_dir = UP; break;
                        case LEFT : cur_dir = RIGHT; break;
                        case RIGHT : cur_dir = DOWN; break;
                        }
                        break;
                    case 4 : cur_dir=(++cur_dir)%4; break;
                    case 5 : cur_dir^=1; break;
                }
            }
            else if(next_shape>=6 && next_shape<=10){
                int[][] temp = warm_holes[next_shape-6];
                if(temp[0][0]==next_x && temp[0][1]==next_y){
                    next_x = temp[1][0];
                    next_y = temp[1][1];
                }
                else{
                    next_x = temp[0][0];
                    next_y = temp[0][1];
                }
            }
            cur_x = next_x;
            cur_y = next_y;
        }
    }
}