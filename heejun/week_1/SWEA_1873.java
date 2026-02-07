package study;

import java.io.*;
import java.util.StringTokenizer;

public class SWEA_1873 {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int H = Integer.parseInt(st.nextToken());
			int W = Integer.parseInt(st.nextToken());

			char[][] board = new char[H][W]; // 게임판
			
			int start_r=0;
			int start_c=0;

			for (int i = 0; i < H; i++) { // 배열 값 넣기
				String one_line =br.readLine();
				for (int j = 0; j < W; j++) {
					char now =one_line.charAt(j);
					board[i][j]=now;
					if (now=='<'||now=='>'||now=='^'||now=='v') {
						start_r=i;
						start_c=i;
					}
				}
			}
			
			int N = Integer.parseInt(br.readLine());
			String commend =br.readLine();
			
			

		}

	}

}
