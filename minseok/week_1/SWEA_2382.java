package algo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class Solution
{
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    // 상, 우, 하, 좌
    private static int[][] dir = {{0,0},{-1,0},{1,0},{0,-1},{0,1}};
    public static class Asso{
        int x;
        int y;
        int cnt;
        int direction;
        public Asso(int x, int y, int cnt, int direction) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
            this.direction = direction;
        }
    }
    private static List<Asso> cur_assos;
    private static int n;
    private static Map<Integer,List<Asso>> conflict_map;
    public static void main(String args[]) throws Exception
    {
        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
     
        for(int test_case = 1; test_case <= T; test_case++)
        {
            sb.append("#").append(test_case).append(' ');
            StringTokenizer st =new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            cur_assos = new ArrayList<>();
            for(int i = 0;i<k;i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int cnt = Integer.parseInt(st.nextToken());
                int direction = Integer.parseInt(st.nextToken());
                Asso a = new Asso(x,y,cnt,direction);
                cur_assos.add(a);
            }
            for(int i = 0;i<m;i++) {
                conflict_map = new HashMap<>();
                for(Asso a : cur_assos) {
                    move(a);
                }
                List<Asso> temp = new ArrayList<>();
                for(List<Asso> s : conflict_map.values()) {
                    if(s.size()==1) {
                    	// 충돌난 게 없음 -> 그냥 바로 위치 반영
                        temp.add(s.get(0));
                    }
                    else {
                    	// 2개 이상 -> 충돌 생김
                    	// 방향은, 미생물 수가 제일 많은 군집의 방향으로 반영
                    	// 미생물 수는, sum
                        int sum = 0;
                        int dir = 0;
                        int max_cnt = 0;
                        int x = 0;
                        int y = 0 ;
                        for(Asso a : s) {
                            sum+=a.cnt;
                            x = a.x;
                            y = a.y;
                            if(max_cnt<a.cnt) {
                                max_cnt = a.cnt;
                                dir = a.direction;
                            }
                        }
                        temp.add(new Asso(x,y,sum,dir));
                    }
                }
                cur_assos = temp;               
                 
            }
            int result = 0;
            for(Asso a : cur_assos) {
                result+=a.cnt;
            }
            sb.append(result).append('\n');
        }
        System.out.println(sb.toString());
    }
    private static void move(Asso a) {
        int new_x = a.x+dir[a.direction][0];
        int new_y = a.y+dir[a.direction][1];
        a.x = new_x;
        a.y = new_y;
        if(is_out(new_x,new_y)) {
        	// 미생물 수 반감
            a.cnt/=2;
            // 방향 전환
            if(a.direction%2==0)
            	a.direction--;
            else
            	a.direction++;
        }
        int key = n*a.x+a.y;
        if (!conflict_map.containsKey(key)) {
            conflict_map.put(key, new ArrayList<>());
        }
        conflict_map.get(key).add(a);
    }
    private static boolean is_out(int x, int y) {
        if(x==0||y==0||x>=n-1||y>=n-1)
            return true;
        return false;
    }
}