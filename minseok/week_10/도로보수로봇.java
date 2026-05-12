

import java.util.*;
import java.io.*;
public class 도로보수로봇 {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
       int n,k;
       StringTokenizer st = new StringTokenizer(br.readLine());
       n = Integer.parseInt(st.nextToken());
       k = Integer.parseInt(st.nextToken());
       int[] arr = new int[n];
       st = new StringTokenizer(br.readLine());
       for(int i = 0 ;i<n;i++){
        arr[i] = Integer.parseInt(st.nextToken());
       }
       int start = 1;
       int end = arr[n-1];
       int answer = Integer.MAX_VALUE;
       while(start<=end){
        int mid = (start+end)/2;
        if(can_cover(arr,mid,n,k)){
            end = mid -1;
            answer = mid;
        }
        else{
            start = mid + 1;
        }
       }
       System.out.println(answer);





    }
    // 보수 패치의 길이가 length일 때, k개의 보수 패치를 가지고 모든 구멍을 커버할 수 있는지 확인하는 메서드
    public static boolean can_cover(int[] arr, int length, int n, int k){
        //length : 보수 패치의 길이
        // 첫번째보수 패치는 무조건 0번째 위치에  놓을 것임
        // 현재 위치를 0번째 구멍으로 세팅
 
        // 현재 내가 커버가능한 범위는 0번째 구멍의 위치 + length -1 만큼임
        int cur_cover = arr[0] + length - 1;
        
        // cnt : 내가 커버한 구멍의 횟수 (첫번째 구멍은 무조건 커버를 함)
        int cnt = 1;
        // 이미 하나 깔아 놨으니까, 내가 사용할 수 있는 보수 패치의 개수는 k -1개 남음
        int left = k - 1; 
        
        // 남은 보수 패치의 개수가 0이하이고, 아직 모든 구멍을 커버하지 않은 동안 시뮬레이션 진행
        while(left>=0&&cnt<n){
        	// 만약에 지금 커버 범위보다 현재 구멍의 위치가 왼쪽 이하에 있다 -> 현재의 범위로 커버가 가능하다 -> 다음 구멍 확인
            if(cur_cover>=arr[cnt]){
                cnt++;
            }
            // 그럴 수 없다. -> 지금 커버 범위로는 이 구멍을 커버할 수 없음 -> 보수 패치를 하나 더 써야함
            else{
            	// 하나 더 써야하는데 지금 남은 보수 패치가 없다 -> 이 길이로는 k개만에 모든 구멍을 커버할 수 없다 -> false 리턴
                if(left==0) return false;
                // 보수 패치를 새로 가져왔으니까 범위도 갱신
                cur_cover = arr[cnt] + length - 1;
                left--;
            }
        }
        return true;
    }
}
