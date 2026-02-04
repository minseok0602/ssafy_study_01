package algo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
 
class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    public static void main(String args[]) throws Exception {
        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        int[][] map;
        for (int test_case = 1; test_case <= T; test_case++) {
            int n = Integer.parseInt(br.readLine());
            map = new int[n][n];
            sb.append("#").append(test_case).append("\n");
            for(int i = 0;i<n;i++){
                StringTokenizer st = new StringTokenizer(br.readLine());
                int idx = 0;
                while(st.hasMoreElements()){
                    map[i][idx++] = Integer.parseInt(st.nextToken());
                }
            }
            for(int i = 0;i<n;i++){
                for(int j = n-1;j>=0;j--){
                    sb.append(map[j][i]);
                }
                sb.append(" ");
                for(int j = n-1;j>=0;j--){
                    sb.append(map[n-i-1][j]);
                }
                sb.append(" ");
                for(int j = 0;j<n;j++){
                    sb.append(map[j][n-i-1]);
                }
                sb.append("\n");
            }
        }
        bw.write(sb.toString());
        bw.flush();
    }
}