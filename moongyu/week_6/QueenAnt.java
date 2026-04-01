import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class QueenAnt {
	// 개미집 하나의 상태를 나타내는 클래스
	static class AntHouse{
		int idx;				// 개미집 번호
		int pos;				// 개미집 실제 위치
		boolean isDestroy;		// 300 명령어에 의해 부서졌는지 여부
		public AntHouse(int idx, int pos) {
			this.idx = idx;
			this.pos = pos;
			this.isDestroy = false;
		}
	}
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		
		// 개미집 위치를 저장할 List
		ArrayList<AntHouse> antHouses = new ArrayList<>();
		int Q = Integer.parseInt(br.readLine());
		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			
			
			// 초기 마을 건설
			if (cmd == 100) {
				int N = Integer.parseInt(st.nextToken());
				for (int i = 0; i < N; i++) {
					int pos = Integer.parseInt(st.nextToken());
					antHouses.add(new AntHouse(i + 1, pos));
				}
			} 
			
			// 개미집 건설
			else if (cmd == 200) {
				int p = Integer.parseInt(st.nextToken());
				antHouses.add(new AntHouse(antHouses.size() + 1, p));
			} 
			
			// 개미집 철거
			else if (cmd == 300) {
				int destroy = Integer.parseInt(st.nextToken());
				antHouses.get(destroy - 1).isDestroy = true;
			}
			
			// 개미집 정찰
			else if (cmd == 400) {
				int r = Integer.parseInt(st.nextToken());
				
				// 부서지지 않아서 정찰이 필요한 개미집 좌표만 따로 저장
				ArrayList<Integer> needToSearch = new ArrayList<>();
				for (AntHouse antHouse : antHouses) {
					if (!antHouse.isDestroy) needToSearch.add(antHouse.pos);
				}
				
				// 이분 탐색 수행 (최소 시간을 찾기 위해서)
				// 개미가 만약 집 개수만큼 줄 경우 0초만에 정찰 완료 -> minimum boundary로 설정
				int leftTime = 0;
				// 개미가 1마리 밖에 안 주어지는 경우 처음 위치부터 끝까지 가는 시간 -> maximum boundary 설정
				int rightTime = needToSearch.get(needToSearch.size() - 1) - needToSearch.get(0);
				
				// 
				int ans = rightTime;
				
				while(leftTime <= rightTime) {
					int midTime = (leftTime + rightTime) / 2;
					
					if (needGroups(needToSearch, midTime) <= r) {
						ans = midTime;
						rightTime = midTime - 1;
					} else {
						leftTime = midTime + 1;
					}
				}
				sb.append(ans).append('\n');
			}
		}
		System.out.println(sb);
	}
	
	static int needGroups(ArrayList<Integer> search, int time) {
		int gCount = 0;			// 필요한 개미
		int idx = 0;
		int N = search.size();
		
		while(idx < N) {
			gCount++;									// 한 마리의 개미가 커버할 수 있는 범위 계산 시작
			int startPos = search.get(idx);				// 탐색되지 않은 가장 왼쪽의 개미집
			
			// 해당 개미가 커버할 수 있는 만큼 개미집 계산
			while (idx < N && search.get(idx) <= startPos + time) idx++;
		}
		
		return gCount;
	}
}