package java_study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class DigitalLogicPatternTest {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String S = br.readLine();
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		int K = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		boolean isNoised = false;
		
		HashMap<Long, Integer> map = new HashMap<>();
		
		long value = 0;
		
		// 최근 K개의 비트만 남기기 위한 마스킹 값
		// 예: K = 3이면 mask = 111(2)
		long mask = (1 << K) - 1;
		
		for (int i = 0; i < S.length(); i++) {
			int bit = S.charAt(i) - '0';
			
			// 원래 값을 왼쪽으로 한 칸 밀고, 현재 bit를 뒤에 붙임
			// 그리고 mask를 이용해서 최근 K개 비트만 남김
			value = ((value << 1) | bit) & mask;
			
			// 아직 길이 K짜리 패턴이 완성되지 않았으면 넘어감
			if (i < K - 1) {
				continue;
			}
			
			int count = 0;
			
			if (map.get(value) != null) {
				count = map.get(value);
			}
			
			count++;
			map.put(value, count);
			
			if (count >= M) {
				isNoised = true;
				break;
			}
		}
		
		if (isNoised) System.out.println(1);
		else System.out.println(0);
	}
}