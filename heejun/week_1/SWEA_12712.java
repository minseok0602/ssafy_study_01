package study;

import java.io.*;
import java.util.StringTokenizer;

public class SWEA_12712 {

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		int T= Integer.parseInt(br.readLine());
		
		for(int test_case = 1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N= Integer.parseInt(st.nextToken()); 
			int M= Integer.parseInt(st.nextToken()); // 분사 세기
			
			int [][] arr = new int[N][N]; //파리 배열
			
			for(int i =0; i<N;i++) {// 배열 값 넣기
				st=new StringTokenizer(br.readLine());
				for(int j=0;j<N;j++) {
					arr[i][j]=Integer.parseInt(st.nextToken());
				}
			}
			
			int [] dr = {-1,0,1,0};
			int [] dc = {0,1,0,-1};
			
			int [] dr2= {-1,1,1,-1};
			int [] dc2= {1,1,-1,-1};
			
			int max=-1;
			int sum=0;
			// + 자
			for(int i=0;i<N;i++) {
				for(int j=0;j<N;j++) {
					sum=arr[i][j];
					for(int k=0;k<4;k++) {
						for(int q=1;q<M;q++) {
							int r =i+dr[k]*q;
							int c=j+dc[k]*q;
							if(r>=0 && r<N && c>=0 && c<N)
								sum+=arr[i+dr[k]*q][j+dc[k]*q];
						}
					}
					
					if(sum>max)
						max=sum;
					
				}
			}
			
			// x 자
			for(int i=0;i<N;i++) {
				for(int j=0;j<N;j++) {
					sum=arr[i][j];
					for(int k=0;k<4;k++) {
						for(int q=1;q<M;q++) {
							int r =i+dr2[k]*q;
							int c=j+dc2[k]*q;
							if(r>=0 && r<N && c>=0 && c<N)
								sum+=arr[i+dr2[k]*q][j+dc2[k]*q];
						}
					}
					
					if(sum>max)
						max=sum;
					
				}
			}
			
			
			System.out.printf("#%d %d\n",test_case,max);
			
		}
		

	}

}
