import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class UnloadDelivery {
	private static int N;
	private static int[][] map;
	private static ArrayList<Package> packArr;
	static class Package{
		int packNum;
		int height;
		int width;
		int rowPos;
		int colPos;
		boolean onTruck;
		public Package(int packNum, int height, int width, int rowPos, int colPos) {
			this.packNum = packNum;
			this.height = height;
			this.width = width;
			this.rowPos = rowPos;
			this.colPos = colPos;
			onTruck = true; 
		}
	}
	public static void main(String args[]) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		packArr = new ArrayList<>();
		
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			
			int k = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken()) - 1;
			
			// 1. 택배 투입
			dropPack(k, h, w, c);
		}
		
		Collections.sort(packArr, (o1, o2) -> o1.packNum - o2.packNum);
		
		while(!isAllUnloaded()) {
			// 좌측 하차
			for (int i = 0; i < M; i++) {
				if (!packArr.get(i).onTruck) continue;
				
				if (canGoLeft(packArr.get(i))) {
					Package removePack = packArr.get(i); 
					for (int h = removePack.rowPos; h > removePack.rowPos - removePack.height; h--) {
						for (int w = removePack.colPos; w < removePack.colPos + removePack.width; w++) {
							map[h][w] = 0;
						}
					}
					packArr.get(i).onTruck = false;
					applyGravity();
					sb.append(removePack.packNum).append('\n');
					break;
				}
			}
			// 우측 하차
			for (int i = 0; i < M; i++) {
				if (!packArr.get(i).onTruck) continue;
				
				if (canGoRight(packArr.get(i))) {
					Package removePack = packArr.get(i);
					for (int h = removePack.rowPos; h > removePack.rowPos - removePack.height; h--) {
						for (int w = removePack.colPos; w < removePack.colPos + removePack.width; w++) {
							map[h][w] = 0;
						}
					}
					packArr.get(i).onTruck = false;
					applyGravity();
					sb.append(removePack.packNum).append('\n');
					break;
				}
			}
		}
		
		System.out.println(sb);
	}
	
	// 1. 초기 택배 투입 함수
	static void dropPack(int packNum, int height, int width, int colPos) {
		int lieHeight = N - 1;
		// 택배가 떨어지는 가로 범위 내에 다른 택배가 존재하는지 확인
		// 존재한다면 해당 높이로 놓는 위치 갱신, 없다면 가장 바닥으로 떨어짐.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < N; j++) {
				if (map[j][colPos + i] != 0) {
					lieHeight = Math.min(lieHeight, j - 1);
	                break;
				}
			}
		}
		
		packArr.add(new Package(packNum, height, width, lieHeight, colPos));
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				map[lieHeight - i][colPos + j] = packNum;
			}
		}
	}
	
	// 2. 좌측 택배 하차
	static boolean canGoLeft(Package pack) {
		for (int h = 0; h < pack.height; h++) {
			for (int i = 0; i < pack.colPos; i++) {
				if (map[pack.rowPos - h][i] != 0) return false;
			}
		}
		return true;
	}
	
	// 3. 우측 택배 하차
	static boolean canGoRight(Package pack) {
		for (int h = 0; h < pack.height; h++) {
			for (int i = pack.colPos + pack.width; i < N; i++) {
				if (map[pack.rowPos - h][i] != 0) return false;
			}
		}
		return true;
	}
	
	static void applyGravity() {
	    ArrayList<Package> remain = new ArrayList<>();
	    for (Package p : packArr) {
	        if (p.onTruck) remain.add(p);
	    }

	    // 아래에 있는 택배부터 처리
	    Collections.sort(remain, (o1, o2) -> o2.rowPos - o1.rowPos);

	    for (Package p : remain) {
	        // 옮기기 전 자기 자신 자리 지우기
	        for (int h = p.rowPos; h > p.rowPos - p.height; h--) {
	            for (int w = p.colPos; w < p.colPos + p.width; w++) {
	                map[h][w] = 0;
	            }
	        }

	        int downRow = p.rowPos;

	        while (true) {
	            int nextRow = downRow + 1;
	            if (nextRow >= N) break;

	            boolean canDown = true;
	            for (int w = p.colPos; w < p.colPos + p.width; w++) {
	                if (map[nextRow][w] != 0) {
	                    canDown = false;
	                    break;
	                }
	            }

	            if (!canDown) break;
	            downRow++;
	        }

	        for (int h = downRow; h > downRow - p.height; h--) {
	            for (int w = p.colPos; w < p.colPos + p.width; w++) {
	                map[h][w] = p.packNum;
	            }
	        }

	        p.rowPos = downRow;
	    }
	}
	
	// 모든 택배가 하차되었는지 확인하는 유틸함수
	static boolean isAllUnloaded() {
		for (Package p : packArr) {
			if (p.onTruck) return false;
		}
		return true;
	}
}
