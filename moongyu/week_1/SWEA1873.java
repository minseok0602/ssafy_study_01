import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA1873 {
	private static int H, W, curRow, curCol, curDir;
	private static char[][] map;
	
	
	private static int[] rowDir = {-1, 0, 1,  0};			// 0 : 상, 1 : 우, 2 : 하, 3 : 좌
	private static int[] colDir = {0,  1, 0, -1};
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine());
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			H = Integer.parseInt(st.nextToken());
			W = Integer.parseInt(st.nextToken());
			map = new char[H][W];
			
			for (int i = 0; i < H; i++) {
				String line = br.readLine();
				for (int j = 0; j < W; j++) {
					map[i][j] = line.charAt(j);
					if (map[i][j] == '^') {
						curRow = i;
						curCol = j;
						curDir = 0;
					} else if (map[i][j] == '>') {
						curRow = i;
						curCol = j;
						curDir = 1;
					} else if (map[i][j] == 'v') {
						curRow = i;
						curCol = j;
						curDir = 2;
					} else if (map[i][j] == '<') {
						curRow = i;
						curCol = j;
						curDir = 3;
					}
				}
			}
			
			int N = Integer.parseInt(br.readLine());
			String commands = br.readLine();
			
			for (int i = 0; i < N; i++) {
				switch(commands.charAt(i)) {
				case 'U' : case 'D' : case 'L' : case 'R' :
					move(commands.charAt(i));						
					break;
				case 'S' :
					shoot();
					break;
				}
			}
			
			sb.append("#").append(tc).append(" ");
			for (int i = 0; i < H; i++) {
				for (int j = 0; j < W; j++) {
					sb.append(map[i][j]);
				}
				sb.append('\n');
			}			
		}
		System.out.println(sb);
	}
	
	private static int getDirection(char d) {
		if (d == 'U') return 0;
		if (d == 'R') return 1;
		if (d == 'D') return 2;
		if (d == 'L') return 3;
		return -1;
	}
	
	private static char getTank(int d) {
		if (d == 0) return '^';
		if (d == 1) return '>';
		if (d == 2) return 'v';
		if (d == 3) return '<';
		return ' ';
	}
	private static void move(char direction) {
		curDir = getDirection(direction);								// 탱크 회전
		map[curRow][curCol] = getTank(curDir);
		int nr = curRow + rowDir[curDir];
		int nc = curCol + colDir[curDir];
		
		if (nr >= 0 && nr < H && nc >= 0 && nc < W) {					// 탱크 움직임
			switch(map[nr][nc]) {
			case '.' :
				map[nr][nc] = map[curRow][curCol]; 
				map[curRow][curCol] = '.';
				curRow = nr;
				curCol = nc;
				break;
			case '*' : case '#' : case '-' :
				break;
			}
		}
	}
	
	private static void shoot() {
		int nr = curRow + rowDir[curDir];
		int nc = curCol + colDir[curDir];
		while (nr >= 0 && nr < H && nc >= 0 && nc < W) {
			if (map[nr][nc] == '*') {
				map[nr][nc] = '.';
				break;
			} else if (map[nr][nc] == '#') {
				break;
			} else {
				nr += rowDir[curDir];
				nc += colDir[curDir];
			}
		}
	}
}