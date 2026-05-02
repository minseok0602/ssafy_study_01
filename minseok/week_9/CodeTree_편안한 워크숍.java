import java.util.*;
import java.io.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n, k;
    static int[][] map;
    static int[][][] dist;
    static int[][] dir = {{0,1},{1,0},{-1,0},{0,-1}};

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        int answer = Integer.MAX_VALUE;

        map = new int[n][n];
        // dist[i][j][k] : 길이가 k이고, 현재 위치가 {i,j}일 때, 경로상의 인접 등산로와의 차이값의 최댓값이 최소인 값
        dist = new int[n][n][k+1];
        for(int i = 0;i<n;i++){
            for(int j =0;j<n;j++){
                Arrays.fill(dist[i][j],Integer.MAX_VALUE);
            }
        }
        Queue<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                // 길이가 1이고, 현재 위치가 {i,j}일 때는 0으로 초기화
                dist[i][j][1] = 0;
                queue.add(new int[]{i,j,1});
            }
        }
        while(!queue.isEmpty()){
            int[]cur = queue.poll();
            int x = cur[0];
            int y = cur[1];
            int cur_len = cur[2];
            // 만약에 현재 길이가 k이면 그 이후는 볼 필요가 없음
            if(cur_len==k){
                answer = Math.min(answer,dist[x][y][k]);
                continue;
            }
            int next_len = cur[2] + 1;
            for(int i =0;i<4;i++){
                int nx = x + dir[i][0];
                int ny = y + dir[i][1];
                if(is_out(nx,ny)) continue;
                if(map[nx][ny]<=map[x][y]) continue;
                // 현재 위치에서, 인접한 상하좌우의 높이와의 차이와, 현재 길이에 대한 dist값과 비교해서 더 큰값을 구함
                // 더 큰 값을 구하는 이유는, 경로상에서는, 인접한 등산로의 높이 차이의 최댓값을 계속 가지고 있어야함
                int cur_point = Math.max(map[nx][ny]-map[x][y],dist[x][y][cur_len]);

                // 만약 다음 길이가 이 point보다 작으면 갱신해주고 큐에 넣음
                if(dist[nx][ny][next_len]>cur_point){
                    dist[nx][ny][next_len] = cur_point;
                    queue.add(new int[]{nx,ny,next_len});
                }
            }
        }
        answer = (answer==Integer.MAX_VALUE)?-1:answer;
        System.out.println(answer);

    }
    static boolean is_out(int x, int y){
        return x<0||y<0||x>=n||y>=n;
    }
}