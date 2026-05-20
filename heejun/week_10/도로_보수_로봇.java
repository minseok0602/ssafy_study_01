package studt_s;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class 도로_보수_로봇 {
    static int N,K;
    static int[] points;
    public static void main(String[] args) throws IOException {
        BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st= new StringTokenizer(br.readLine());
        
        N=Integer.parseInt(st.nextToken());
        K=Integer.parseInt(st.nextToken());
        points=new int[N];
        st=new StringTokenizer(br.readLine());

        for(int i=0;i<N;i++){
            points[i]=Integer.parseInt(st.nextToken());
        }
        
        Arrays.sort(points);
        int left=1;
        int right=1_000_000_000;
        while (left<right){
            int mid=(left+right)/2;
            if(check(mid)){
                right=mid;
            }
            else{
                left=mid+1;
            }
            
        }
        System.out.println(left);
    }

    private static boolean check(int mid) {
        int cnt=1;//사용한 패치 수
        int now =points[0];//체크하려는 구멍

        for(int i=1;i<N;i++){
            if(points[i]-now+1<=mid) continue; // 패치로 다음 구멍 커버되는가?

            //커버 안되기에 새 패치사용
            cnt++;
            now=points[i];
        }

        return cnt<=K;

    }


}
