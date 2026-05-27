import java.util.*;
import java.io.*;
public class AI로봇청소기 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n,k,l;
    static int[][] board;
    // 현재 서있는 방향에 따라서 보이는 방향
    static int[][][]boundary= {{{1,0},{-1,0},{0,1}},{{1,0},{0,-1},{0,1}},{{1,0},{-1,0},{0,-1}},{{0,-1},{-1,0},{0,1}}};
    
    static int[][] dir = {{-1,0},{0,-1},{0,1},{1,0}};
    
    // 현재 청소기가 서있는 곳
    static boolean[][]stand;
    static class Robot{
        int x;
        int y;
        public Robot(int x, int y){
            this.x =x ;
            this.y = y;
        }
    }
    static ArrayList<Robot> robots = new ArrayList<>();
    public static void main(String[] args) throws IOException {
       StringTokenizer st = new StringTokenizer(br.readLine());
       n = Integer.parseInt(st.nextToken());
       k = Integer.parseInt(st.nextToken());
       l = Integer.parseInt(st.nextToken());
       board = new int[n][n];
       stand = new boolean[n][n];
       for(int i = 0;i<n;i++){
        st = new StringTokenizer(br.readLine());
        for(int j = 0;j<n;j++){
            board[i][j] = Integer.parseInt(st.nextToken());
        }
       }
       for(int i = 0;i<k;i++){
        st = new StringTokenizer(br.readLine());
        Robot robot = new Robot(Integer.parseInt(st.nextToken())-1,Integer.parseInt(st.nextToken())-1);
        stand[robot.x][robot.y] = true;
        robots.add(robot);
       }
       for(int i = 0;i<l;i++){
        for(Robot r : robots){
            move(r);
        }
        for(Robot r : robots){
            clean(r);
        }
        stack();
        spread();
        print();
       }

    }
    static void move(Robot robot){
    boolean[][] visit = new boolean[n][n];
    Queue<int[]> queue = new ArrayDeque<>();

    queue.add(new int[]{robot.x, robot.y, 0});
    visit[robot.x][robot.y] = true;

    int minDist = -1;
    ArrayList<int[]> candidates = new ArrayList<>();

    while(!queue.isEmpty()){
        int[] cur = queue.poll();
        int x = cur[0];
        int y = cur[1];
        int dist = cur[2];

        // 이미 가장 가까운 거리보다 먼 곳이면 볼 필요 없음
        if(minDist != -1 && dist > minDist) break;

        // 먼지가 있으면 바로 minDist에 반영
        // 처음에 할당된 minDist를 기준으로 후보에 넣을 거임
        if(board[x][y] > 0){
            minDist = dist;
            candidates.add(new int[]{x, y});
            continue;
        }

        for(int i = 0; i < 4; i++){
            int nx = x + dir[i][0];
            int ny = y + dir[i][1];

            if(is_out(nx, ny)) continue;
            if(visit[nx][ny]) continue;
            if(board[nx][ny] == -1) continue;
            if(stand[nx][ny]) continue;

            visit[nx][ny] = true;
            queue.add(new int[]{nx, ny, dist + 1});
        }
    }
	
	    // 이동할 먼지가 없으면 그대로 있음
	    if(candidates.isEmpty()) return;
	
	    // 거리 같은 후보 중 행 번호 작은 것, 행 같으면 열 번호 작은 것
	    candidates.sort((a, b) -> {
	        if(a[0] != b[0]) return a[0] - b[0];
	        return a[1] - b[1];
	    });
	
	    int[] target = candidates.get(0);
	
	    // 이동 처리
	    stand[robot.x][robot.y] = false;
	    robot.x = target[0];
	    robot.y = target[1];
	    stand[robot.x][robot.y] = true;
    }
    
    static void clean(Robot robot){
        int max_sum = -1;
        int max_dir = 0;

        for(int i = 0; i < 4; i++){
            int sum = 0;

            // 현재 칸
            if(board[robot.x][robot.y] > 0){
                sum += Math.min(20, board[robot.x][robot.y]);
            }

            // 바라보는 방향 기준 왼쪽, 앞쪽, 오른쪽 3칸
            for(int j = 0; j < 3; j++){
                int nx = robot.x + boundary[i][j][0];
                int ny = robot.y + boundary[i][j][1];

                if(is_out(nx, ny)) continue;
                // 최대 20만큼만 먼지를 빨아들일 수 있음
                if(board[nx][ny] > 0){
                    sum += Math.min(20, board[nx][ny]);
                }
            }

            // 동률이면 기존 방향 유지
            // boundary 순서가 오른쪽, 아래쪽, 왼쪽, 위쪽이므로 우선순위 만족
            if(max_sum < sum){
                max_sum = sum;
                max_dir = i;
            }
        }

        // 현재 칸 청소
        if(board[robot.x][robot.y] > 20){
            board[robot.x][robot.y] -= 20;
        } else if(board[robot.x][robot.y] > 0){
            board[robot.x][robot.y] = 0;
        }

        // 선택된 방향의 왼쪽, 앞쪽, 오른쪽 칸 청소
        for(int i = 0; i < 3; i++){
            int nx = robot.x + boundary[max_dir][i][0];
            int ny = robot.y + boundary[max_dir][i][1];

            if(is_out(nx, ny)) continue;

            if(board[nx][ny] > 20){
                board[nx][ny] -= 20;
            } else if(board[nx][ny] > 0){
                board[nx][ny] = 0;
            }
        }
    }
    static void stack(){
        for(int i =0 ;i<n;i++){
            for(int j =0 ;j<n;j++){
                if(board[i][j]>0){
                    board[i][j]+=5;
                }
            }
        }
    }
    static void spread(){
        int[][]old_board = new int[n][n];
        for(int i =0 ;i<n;i++){
            for(int j =0 ;j<n;j++){
                old_board[i][j] = board[i][j];
            }
        }
        for(int i =0 ;i<n;i++){
            for(int j =0 ;j<n;j++){
                if(old_board[i][j]==0){
                    int sum = 0;
                    for(int k = 0;k<4;k++){
                        int nx = dir[k][0] + i;
                        int ny = dir[k][1] + j;
                        if(is_out(nx,ny)) continue;
                        if(old_board[nx][ny]>0){
                            sum+=old_board[nx][ny];
                        }
                    }
                    int point = sum/10;
                    board[i][j] = point;
                }
            }
        }
    }
    static void print(){
        int sum = 0;
        for(int i =0;i<n;i++){
            for(int j = 0;j<n;j++){
                if(board[i][j]>0)
                    sum+=board[i][j];
            }
        }
        System.out.println(sum);
    }
    static boolean is_out(int x, int y){
        return x<0||y<0||y>=n||x>=n;
    }
}
