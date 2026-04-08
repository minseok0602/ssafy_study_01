import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class CaptainCodi {
	static class Ship{
		int id;
		int power;
		int reloadTime;
		
		// 다시 준비되기 까지 걸리는 시간
		int needToWait;
		// 함포 교체 여부를 체크하기 위한 변수
		int version;
		
		public Ship(int id, int power, int reloadTime) {
			this.id = id;
			this.power = power;
			this.reloadTime = reloadTime;
			needToWait = 0;
			version = 0;
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = null;
		int T = Integer.parseInt(br.readLine());
		
		// Ship 객체의 중요한 정보만 빼와서 관리하는 pq ([0] : id, [1] : power, [2] : reloadTime)
		PriorityQueue<int[]> standBy = new PriorityQueue<>((o1, o2) -> {
				if (o1[1] != o2[1]) return o2[1] - o1[1];
				return o1[0] - o2[0];
			});
		
		// 장전 중인 배들 관리하는 pq
		PriorityQueue<Ship> waitQ = new PriorityQueue<>(((o1, o2) -> o1.needToWait - o2.needToWait));
		
		// index 기반으로 바로 조회하기 위한 Map (10^9)
		HashMap<Integer, Ship> ships = new HashMap<>();
				
		for (int i = 0; i < T; i++) {
			st = new StringTokenizer(br.readLine());
			
			int cmd = Integer.parseInt(st.nextToken());
			
			switch (cmd) {
			case 100:						// 공격 준비
				int N = Integer.parseInt(st.nextToken());
				for (int j = 0; j < N; j++) {
					int id = Integer.parseInt(st.nextToken());
					int p = Integer.parseInt(st.nextToken());
					int r = Integer.parseInt(st.nextToken());
					Ship nShip = new Ship(id, p, r);
					ships.put(id, nShip);
					standBy.add(new int[] {id, p, nShip.version});
				}
				break;
				
			case 200:						// 지원 요청 : 새로운 선박 합류
				int idx = Integer.parseInt(st.nextToken());
				int pw = Integer.parseInt(st.nextToken());
				int relo = Integer.parseInt(st.nextToken());
				Ship nShip = new Ship(idx, pw, relo);
				ships.put(idx, nShip);
				standBy.add(new int[] {idx, pw, nShip.version});

				break;
				
			case 300:						// 함포 교체 : id번의 선박 함포 교체
				int id = Integer.parseInt(st.nextToken());
				int p = Integer.parseInt(st.nextToken());
				
				// map 내의 배 업데이트
				Ship ship = ships.get(id);
			    ship.power = p;
			    ship.version++;

			    // 현재 standby 상태라면 PQ에 최신 정보 하나 더 넣음
			    if (ship.needToWait == 0) {
			        standBy.add(new int[] {ship.id, ship.power, ship.version});
			    }
			    
				break;
				
			case 400:						// 공격 명령 : 사격 대기 선박 중 최대 5척 일제 사격
				int out = 0;
				int powerSum = 0;

				// 사격하는 배들의 index 저장
				ArrayList<Integer> shootList = new ArrayList<>();
				while(!standBy.isEmpty() && out < 5) {
					int[] cur = standBy.poll();
					int index = cur[0];
					int power = cur[1];
					int version = cur[2];
					
					// index 기반으로 배 가져옴
					Ship shoot = ships.get(index);

				    // 재장전 중
				    if (shoot.needToWait != 0) continue;
				    
				    // 버전이 다르다는건 300으로 함포가 교체됐다는 것
				    if (shoot.version != version) continue;

				    powerSum += shoot.power;
				    shootList.add(shoot.id);
				    ++out;

				    shoot.needToWait = shoot.reloadTime;
				    waitQ.add(shoot);
				}
				
				sb.append(powerSum).append(' ').append(out);
				for (Integer intge : shootList) sb.append(' ').append(intge);
				sb.append('\n');
				break;
			}
			
			for (Ship ship : waitQ) --ship.needToWait;
			
			while(!waitQ.isEmpty() && waitQ.peek().needToWait == 0) {
				Ship ship = waitQ.poll();
				standBy.add(new int[] {ship.id, ship.power, ship.version});
			}
		}
		
		System.out.print(sb);
	}
}