import java.util.Scanner;
public class Main {
    static int R;
    static int C;
    static char[][] grid;
    static int result = 0;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        R = sc.nextInt();
        C = sc.nextInt();
        grid = new char[R][C];
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                grid[i][j] = sc.next().charAt(0);
            }
        }
        dfs(0,0,0,grid[0][0]);
        System.out.println(result);
    }
    public static void dfs(int x, int y, int cnt, char color){
        if(cnt>3){
            return;
        }
        if(cnt==3){
            if(x==R-1&&y==C-1)
                result++;
            return;
        }
        for(int i = x+1;i<R;i++){
            for(int j =y+1;j<C;j++){
                if(isOut(i,j))
                    continue;
                if(grid[i][j]!=color){
                    dfs(i,j,cnt+1,grid[i][j]);
                }
            }
        }
    }
    public static boolean isOut(int x, int y){
        if(x<0||y<0||x>=R||y>=C)
            return true;
        return false;
    }
}