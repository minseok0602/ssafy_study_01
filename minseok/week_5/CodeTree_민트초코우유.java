import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
public class CodeTree {
	static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
	static int[][] bits;
	static int[][] map;
	static boolean[][] visit;
	static int[] answer;
	static int n,t;
	static ArrayList<King> cur_kings;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static class King implements Comparable<King>{
		int x;
		int y;
		int count;
		public King(int x, int y,int count) {
			this.x = x;
			this.y = y;
			this.count = count;
		}
		@Override
		public int compareTo(King k) {
			int x1 =Integer.bitCount(bits[this.x][this.y]);
			int x2 = Integer.bitCount(bits[k.x][k.y]);
			if(x1!=x2) {
				return Integer.compare(x1,x2);
			}
			if(map[k.x][k.y]!=map[this.x][this.y]) {
				return Integer.compare(map[k.x][k.y], map[this.x][this.y]);			
			}
			if(k.x!=this.x)
				return Integer.compare(this.x, k.x);
			return Integer.compare(this.y, k.y);
		}
		
	}
    public static void main(String[] args) throws IOException {
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	n = Integer.parseInt(st.nextToken());
    	t = Integer.parseInt(st.nextToken());
    	bits = new int[n][n];
    	map = new int[n][n];
    	visit = new boolean[n][n];
    	for(int i = 0;i<n;i++) {
    		String temp = br.readLine();
    		for(int j =0 ;j<n;j++) {
    			switch(temp.charAt(j)) {
	    			case 'T' : {
	    				bits[i][j]=1<<2;
	    				break;
	    			}
	    			case 'C' : {
	    				bits[i][j]=1<<1;
	    				break;
	    			}
	    			case 'M' : {
	    				bits[i][j] = 1;
	    				break;
	    			}
    			}
    		}
    	}
    	for(int i = 0;i<n;i++) {
    		st = new StringTokenizer(br.readLine());
    		for(int j =0;j<n;j++) {
    			map[i][j] = Integer.parseInt(st.nextToken());
    		}
    	}
    	for(int i = 0;i<t;i++) {
    		answer = new int[1<<3];
    		morning();
    		lunch();
    		night();
    		count_result();
    		System.out.println(answer[7]+" "+answer[6]+" "+answer[5]+" "+answer[3]+" "+answer[1]+" "+answer[2]+" "+answer[4]);
    	}
    	
    }
    static void morning() {
    	for(int i =0;i<n;i++) {
    		for(int j =0 ;j<n;j++) {
    			map[i][j]++;
    		}
    	}
    }
    
    static void lunch() {
    	cur_kings = new ArrayList<>();
    	// 증가되어야하는 가중치를 저장
    	int[][] weight_map = new int[n][n];
    	for(int i = 0;i<n;i++) {
    		for(int j = 0;j<n;j++) {
    			// 아직 그룹을 정하지 않은 사람 -> 인접한 사람들과 그룹을 형성
    			if(weight_map[i][j] != -1) {
    				int[] temp = connect_group(weight_map,i,j,bits[i][j]);
    				//대표자를 찾아서 그 대표자를 리스트에 넣음
    				cur_kings.add(new King(temp[0],temp[1],temp[2]));
    			}
    		}
    	}
    	// 대표자의 신앙심은, 조직의 인원수 -1 만큼 증가됨
    	for(King k : cur_kings) {
    		weight_map[k.x][k.y] = k.count;
    	}
    	for(int i = 0;i<n;i++) {
    		for(int j = 0;j<n;j++) {
    			map[i][j] +=weight_map[i][j];
    		}
    	}
    }
    
    
    static void night() {
    	// 대표자가 전파 시작
    	Collections.sort(cur_kings);
    	// 본인이 전파 당했는지를 나타냄
    	visit = new boolean[n][n];
    	for(King k : cur_kings) {
    		// 대표자가 다른 전파자에 의해 전파를 당했으면 당일에는 전파 안함
    		if(visit[k.x][k.y]) continue;
    		spread(k);
    	}
    }
    static int[] connect_group(int[][] minus_map,int x, int y, int bit) {
    	Queue<int[]> queue = new ArrayDeque<>();
    	queue.add(new int[] {x,y});
    	//king[0] : king의 x
    	//king[1] : king의 y
    	//king[2] : 현재 집단의 인원수-1
    	int[] king = new int[4];
    	king[0] = x;
    	king[1] = y;
    	king[2] = 0;
    	int max = map[x][y];
    	minus_map[x][y] = -1;
    	while(!queue.isEmpty()){
    		int[] cur = queue.poll();
    		for(int i = 0;i<4;i++) {
    			int nx = cur[0] + dir[i][0];
    			int ny = cur[1] + dir[i][1];
    			if(nx<0||ny<0||nx>=n||ny>=n) continue;
    			if(minus_map[nx][ny] == -1) continue;
    			if(bits[nx][ny]==bit) {
    				minus_map[nx][ny] = -1;
    				if (map[nx][ny] > max ||
    						   (map[nx][ny] == max && (nx < king[0] || (nx == king[0] && ny < king[1])))) {
    						    king[0] = nx;
    						    king[1] = ny;
    						    max = map[nx][ny];
    						}
    				king[2]++;
    				queue.add(new int[] {nx,ny});
    			}
    		}
    		
    	}
    	return king;
    }
    static void spread(King k) {
    	int cx = k.x;
    	int cy = k.y;
    	int wish = map[k.x][k.y] - 1; 
    	int new_dir = map[k.x][k.y]%4;
    	map[k.x][k.y] = 1;
    	cx += dir[new_dir][0];
    	cy += dir[new_dir][1];
    	while(wish>0&&cx>=0&&cy>=0&&cx<n&&cy<n) {
    		if(bits[k.x][k.y]==bits[cx][cy]) {
    			cx += dir[new_dir][0];
            	cy += dir[new_dir][1];
            	continue;
    		}
    		visit[cx][cy] = true;
    		if(wish>map[cx][cy]) {
    			// 강한 전파
    			wish-=(map[cx][cy]+1);
    			map[cx][cy]++;
    			bits[cx][cy] = bits[k.x][k.y];
    		}
    		else {
    			// 약한 전파
    			int new_bit = bits[k.x][k.y] | bits[cx][cy];
    			bits[cx][cy] = new_bit;
    			map[cx][cy]+=wish;
    			wish = 0;
    			
    		}
    		cx += dir[new_dir][0];
        	cy += dir[new_dir][1];
    	}
    	
    }
    static void count_result() {
    	for(int i =0 ;i<n;i++) {
    		for(int j =0;j<n;j++) {
    			answer[bits[i][j]]+=map[i][j];
    		}
    	}
    }
    
}