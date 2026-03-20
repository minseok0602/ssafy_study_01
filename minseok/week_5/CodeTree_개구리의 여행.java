import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
public class CodeTree {
	static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
	static char[][] map;
	//min_dist[x][y][j] : x,y 좌표로 j의 점프력 만큼 뛰어서 올 때의 최소시간
	static int[][][] min_dist;
	static int n;
	static int t;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringBuilder sb = new StringBuilder();
	
	// . : 안전한 돌
	// S : 미끄러운 돌
	// # : 천적이 사는 돌
    public static void main(String[] args) throws IOException {
    	n = Integer.parseInt(br.readLine());
    	map = new char[n][n];
    	for(int i = 0;i<n;i++) {
    		String temp = br.readLine();
    		for(int j =0;j<n;j++) {
    			map[i][j] = temp.charAt(j);
    		}
    	}
    	t = Integer.parseInt(br.readLine());
    	for(int i = 0;i<t;i++) {
    		StringTokenizer st = new StringTokenizer(br.readLine());
    		int sx = Integer.parseInt(st.nextToken())-1;
    		int sy = Integer.parseInt(st.nextToken())-1;
    		int ex = Integer.parseInt(st.nextToken())-1;
    		int ey = Integer.parseInt(st.nextToken())-1;
    		min_dist = new int[n][n][6];
    		for(int j =0;j<n;j++) {
    			for(int k =0 ;k<n;k++) {
    				Arrays.fill(min_dist[j][k], Integer.MAX_VALUE);
    			}
    		}
    		min_dist[sx][sy][1] = 0;
    		bfs(sx,sy,ex,ey);
    		int answer = Integer.MAX_VALUE;
    		for(int j =1;j<=5;j++) {
    			answer = Math.min(answer, min_dist[ex][ey][j]);
    		}
    		sb.append(answer!=Integer.MAX_VALUE?answer:-1).append("\n");
    	}
    	System.out.println(sb);
    }
    static void bfs(int sx, int sy, int ex, int ey) {
    	PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparing(o->o[3]));
    	queue.add(new int[] {sx,sy,1,0,});
    	while(!queue.isEmpty()) {
    		int[] cur = queue.poll();
    		if (cur[3] != min_dist[cur[0]][cur[1]][cur[2]]) continue;
    		for(int i =0 ;i<4;i++) {
    			// 현재 위치에서 할 수 있는 거
    			// 1. 현재 가진 점프력 만큼 점프하기
    			// 2. 가만히 있으면서 점프력만 증가시키기
    			// 3. 가만히 있으면서 점프력 감소시키기
    			int new_jump = cur[2];
				int[] next_pos = jump(cur[0],cur[1],i,new_jump);
			    int nx = next_pos[0];
				int ny = next_pos[1];
				// 새로 설정한 점프력 만큼 점프를 해서 이동을 할 수 있나
				if(can_go(cur[0],cur[1],nx,ny,i)) {
					// 이동을 할 수 있어도, 지금 저장된 값보다 더 작은가?
					if(min_dist[nx][ny][new_jump]>cur[3]+1) {
						min_dist[nx][ny][new_jump] = cur[3]+1;
						queue.add(new int[] {nx,ny,new_jump,min_dist[nx][ny][new_jump]});
					}
				}
    			int weight = 0;
    			for(int j = 1;j+cur[2]<=5;j++) {
    				new_jump = cur[2]+j;
    				weight +=new_jump*new_jump;
    				next_pos = jump(cur[0],cur[1],i,new_jump);
    			    nx = next_pos[0];
    				ny = next_pos[1];
    				// 점프력을 증가시키고, +1의 시간을 들여서 이동함
    				if(can_go(cur[0],cur[1],nx,ny,i)) {
    					if(min_dist[nx][ny][new_jump]>cur[3]+weight+1) {
    						min_dist[nx][ny][new_jump] = cur[3]+weight+1;
    						queue.add(new int[] {nx,ny,new_jump,min_dist[nx][ny][new_jump]});
    					}
    				}
    			}
    			for(int j = 1;cur[2]-j>=1;j++) {
    				new_jump = cur[2]-j;
    				next_pos = jump(cur[0],cur[1],i,new_jump);
    			    nx = next_pos[0];
    				ny = next_pos[1];
    				// 점프력을 감소시키고, +1의 시간을 들여서 이동함
    				if(can_go(cur[0],cur[1],nx,ny,i)) {
    					if(min_dist[nx][ny][new_jump]>cur[3]+1+1) {
    						min_dist[nx][ny][new_jump] = cur[3]+1+1;
    						queue.add(new int[] {nx,ny,new_jump,min_dist[nx][ny][new_jump]});
    					}
    				}
    			}
    		}
    	}
    }
    static int[] jump(int x, int y, int direction, int power) {
    	int[] temp = new int[2];
    	temp[0] = x + dir[direction][0]*power;
    	temp[1] = y + dir[direction][1]*power;
    	return temp;
    }
    
    static boolean can_go(int x1, int y1, int x2, int y2, int direction) {
    	// 만약에 지금 위치 -> 점프 위치로 가다가 천적을 만나거나 맵 밖으로 나가면 false
    	int cur_x = x1;
    	int cur_y = y1;
    	while(true) {
    		if(cur_x<0||cur_y<0||cur_x>=n||cur_y>=n) return false;
    		if(map[cur_x][cur_y]=='#') return false;
    		if(cur_x==x2&&cur_y==y2)
    			break;
    		cur_x+=dir[direction][0];
    		cur_y+=dir[direction][1];
    	}
    	// 위에 다 통과를 했더라도, 착지 지점이 미끄러운 돌이라면 거기로 착지 못함
    	return map[cur_x][cur_y]!='S';
    	
    }
    
}