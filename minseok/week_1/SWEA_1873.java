package algo;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class Solution
{
    // up,down,left,right
    static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
    static final int UP = 0;
    static final int DOWN = 1;
    static final int LEFT = 2;
    static final int RIGHT = 3;
    static int cur_x, cur_y;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String args[]) throws Exception
    {
        int T;
        T=Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
     
        for(int test_case = 1; test_case <= T; test_case++)
        {
            sb.append("#").append(test_case+" ");
            int n,m;
            cur_x = 0;
            cur_y = 0;
            StringTokenizer st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            char [][]map = new char[n][m];
            for(int i =0;i<n;i++) {
                String str = br.readLine();
                int j = 0;
                for(char c : str.toCharArray()) {
                    if(c=='<'||c=='>'||c=='^'||c=='v') {
                        cur_x = i;
                        cur_y = j;
                    }
                    map[i][j++] = c;
                }
            }
            br.readLine();
            String command = br.readLine(); 
            for(char c : command.toCharArray()) {
                switch(c) {
                case 'U' : move(map,UP); break;
                case 'D' : move(map,DOWN); break;
                case 'L' : move(map,LEFT); break;
                case 'R' :move(map,RIGHT); break;
                case 'S' : shoot(map);
                }
            }
            for(int i = 0;i<n;i++) {
                for(int j =0;j<m;j++) {
                    sb.append(map[i][j]);
                }
                sb.append('\n');
            }
        }
        System.out.println(sb.toString());
    }
    public static void move(char[][] map,int direciton) {
        char cur_dir = ' ';
        if(direciton==0)
            cur_dir='^';
        else if(direciton==1)
            cur_dir='v';
        else if(direciton==2)
            cur_dir='<';
        else
            cur_dir='>';
         
        map[cur_x][cur_y] = cur_dir;
        int new_x = cur_x+dir[direciton][0];
        int new_y = cur_y+dir[direciton][1];
        if(!cant_go(map,new_x,new_y)) {
            map[cur_x][cur_y] = '.';
            map[new_x][new_y] = cur_dir;
            cur_x = new_x;
            cur_y = new_y;
        }
         
    }
    public static void shoot(char[][] map) {
        switch(map[cur_x][cur_y]) {
        case '^' : shoot(map,0); break;
        case 'v' : shoot(map,1); break;
        case '<' : shoot(map,2); break;
        default : shoot(map,3); break;
        }
    }
    public static void shoot(char[][] map,int direction) {
        int x = cur_x;
        int y = cur_y;
        while(!is_out(map,x,y)&&!is_wall(map,x,y)) {
            x+=dir[direction][0];
            y+=dir[direction][1];
        }
        if(!is_out(map,x,y)) {
            if(map[x][y]=='*')
                map[x][y] = '.';
        }
    }
    public static boolean cant_go(char[][] map,int x, int y) {
        if(is_out(map,x,y))
            return true;
        if(map[x][y]=='-'||map[x][y]=='*'||map[x][y]=='#')
            return true;
        return false;
    }
    public static boolean is_out(char[][] map,int x, int y) {
        if(x<0||y<0||x>=map.length||y>=map[0].length)
            return true;
        return false;
    }
    public static boolean is_wall(char[][] map,int x, int y) {
        if(map[x][y]=='*'||map[x][y]=='#')
            return true;
        return false;
    }
}