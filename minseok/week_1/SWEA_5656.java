package algo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class Solution
{
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringBuilder sb = new StringBuilder();
    static int[][] dir = {{0,0},{0,1},{1,0},{0,-1},{-1,0}};
    static List<Block>[] map;
    static int h;
    static int w;
    static class Block{
    	int power;
    	boolean is_deleted = false;
    	public Block(int power) {
    		this.power = power;
    	}
    }
    public static void main(String args[]) throws Exception
    {
        int T = Integer.parseInt(br.readLine());
        for(int test_case = 1; test_case <= T; test_case++)
        {
            sb.append("#").append(test_case).append(' ');
            StringTokenizer st = new StringTokenizer(br.readLine());
            w = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());
            for(int i =0;i<w;i++) {
            	map[i] = new ArrayList<>();
            }
            for(int i = 0;i<h;i++) {
            	st = new StringTokenizer(br.readLine());
            	for(int j = 0;j<w;j++)
            		map[i].add(new Block(Integer.parseInt(st.nextToken())));
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }
    public void bomb(int row, int col, Block b) {
    	b.is_deleted = true;
    	int power = b.power;
    	for(int i = 0;i<4;i++) {
    		int cur_row = row;
    		int cur_col = col;
    		for(int j =0;j<power;j++) {
    			cur_row+=dir[i][0];
    			cur_col+=dir[i][1];
    			if(is_out(cur_row,cur_col))
    				continue;
    			if(map[cur_col].size()<=cur_row) {
    				
    			}
    		}
    	}
    }
    public boolean is_out(int row, int col) {
    	return row<0||col<0||row>h||col>w;
    }
    
}