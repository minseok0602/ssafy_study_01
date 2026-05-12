
import java.util.*;
public class 배터리효율최적화하기 {
    public static int[][] board;
    public static int N;
    public static int M;
    static int answer = -1 * Integer.MAX_VALUE;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        board = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                board[i][j] = sc.nextInt();
            }
        }
        selectArr(0,0,true,null,new boolean[N][M],0,0,-1);
        System.out.println(answer);
    }
    static int[][] dir = {{-1,0},{0,1},{1,0},{0,-1}};
    public static void selectArr(int cnt, int idx, boolean first, boolean[][]visit_old, boolean[][]visit,int share_cnt, int score, int last_cell){
        // 만약에 공유 셀이 3개이상이면 정답이 아님
        if(share_cnt>=3) return;
        // 5개를 골랐음
        if(cnt==5){
            // 첫번째 배열 검사
            if(first){
            // arr1이 서로 인접해있는지 확인해야함
	            if(check_arr(last_cell,visit)){
	            	// 만약 첫번째 모듈이 서로 인접해있다면, 두번째 모듈을 구성하러 감 (first 비트를 false로 두어서 selectArr 메서드 실행)
	                selectArr(0,0,false,visit,new boolean[N][M],0,score,-1);
	            }
            }
            // 두번째 배열 검사
            else{
                if(share_cnt==2&&check_arr(last_cell,visit)){
                    //여기까지 왔다는 것은, 1,2번째 배열 둘다 서로 인접해있고, 1,2번째 배열은 정확히 2개의 셀만 서로 공유하는 상태 -> 정답 후보
                    answer = Math.max(answer,score);

                }
            }
            return;
        }
        
        // 셀이 5개가 골라지지 않은 상태에선, 2차원 배열에서 조합으로 5개를 골라야함
        for(int i = idx;i<N*M;i++){
        	// 골랐다는 처리를 해줌.
            visit[i/M][i%M] = true;
            int next_share_cnt = share_cnt;
            int next_score = score;
            
            // 만약에 visit_old가 null이 아니다 -> 이 로직은 두번째 배열을 고르른 상황에서 실행되고 있다 -> 공유하는 셀을 확인하기 위해 이미 첫번째 배열에서 선택된 셀인지도 확인함
            if(visit_old!=null){
                if(visit_old[i/M][i%M]){
                    next_share_cnt++;
                    
                }
            }
            // 점수를 갱신
            next_score+=board[i/M][i%M];
            selectArr(cnt+1,i+1,first, visit_old, visit,next_share_cnt, next_score,i);
            visit[i/M][i%M] = false;
        }
    }
    // 고른 셀들이 서로 인접해있는지 확인하는 메서드
    // 셀 집합에서, 제일 마지막 셀을 기억해서 가져옴
    // 이 셀을 이용해서 상하좌우로 인접한 곳으로 이동하면서, 집합의 크기가 5인지 체크함
    public static boolean check_arr(int last_cell, boolean[][]selected){
        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visit = new boolean[N][M];
        queue.add(new int[]{last_cell/M,last_cell%M});
        visit[last_cell/M][last_cell%M] = true;
        int size = 0;
        while(!queue.isEmpty()){
            int[] cur = queue.poll();
            size++;
            // 만약 사이즈가 5이다 -> 인접한 곳으로만 이동했는데 집합의 크기가 5가 되었다 -> 조건 만족
            if(size==5)
                return true;
            for(int i = 0;i<4;i++){
                int nx = cur[0] + dir[i][0];
                int ny = cur[1] + dir[i][1];
                if(is_out(nx,ny)) continue;
                if(visit[nx][ny]) continue;
                // 인접한 곳이, 내가 뽑았던 셀일 때만 큐에 넣어야함.
                if(selected[nx][ny]){
                    visit[nx][ny] = true;
                    queue.add(new int[]{nx,ny});
                }
            }
        }
        return false;
    }
    public static boolean is_out(int x, int y){
        return x<0||y<0||x>=N||y>=M;
    }

}
