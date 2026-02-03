package algo;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class Solution
{
	static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1},{1,1},{-1,1},{1,-1},{-1,-1}};
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
			StringTokenizer st = new StringTokenizer(br.readLine());
			n = Integer.parseInt(st.nextToken());
			m = Integer.parseInt(st.nextToken());
			int [][]map = new int[n][n];
			int result = 0;
			for(int i =0;i<n;i++) {
				st = new StringTokenizer(br.readLine());
				int j = 0;
				while(st.hasMoreTokens()) {
					map[i][j++] = Integer.parseInt(st.nextToken());
				}
			}
			for(int i =0;i<n;i++) {
				for(int j =0;j<n;j++) {
					result = Math.max(result, spread(map,i,j,m));
				}
			}
			sb.append(result).append('\n');
		}
		System.out.println(sb.toString());
	}
	public static int spread(int[][] map, int x, int y, int power) {
		int sum1 = map[x][y];
		for(int i =0;i<4;i++) {
			int cur_x = x;
			int cur_y = y;
			for(int j = 1;j<power;j++) {
				cur_x +=dir[i][0];
				cur_y +=dir[i][1];
				if(!isOut(cur_x,cur_y,map[0].length)) {
					sum1+= map[cur_x][cur_y];
				}
			}
		}
		int sum2 = map[x][y];
		for(int i =4;i<8;i++) {
			int cur_x = x;
			int cur_y = y;
			for(int j = 1;j<power;j++) {
				cur_x +=dir[i][0];
				cur_y +=dir[i][1];
				if(!isOut(cur_x,cur_y,map[0].length)) {
					sum2+= map[cur_x][cur_y];
				}
			}
		}
		return Math.max(sum1, sum2);
	}
	public static boolean isOut(int x, int y, int n) {
		if(x<0||y<0||x>=n||y>=n)
			return true;
		return false;
	}
}
