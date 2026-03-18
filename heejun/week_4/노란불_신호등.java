import java.util.ArrayList;

class Solution {
	static int size;
	static int[] sumT;
	static ArrayList<Integer>[]sig;
	static int minT;
    public int solution(int[][] signals) {
        int answer = 0;
        size =signals.length;
        sumT=new int[size];
        sig =new ArrayList[size];
        minT=999_999_999;
        for(int i=0;i<size;i++) {
        	sig[i]=new ArrayList<>();
        }
        
        for(int i=0;i<size;i++) { // 각 신호등의 시간 총합
        	int sum=0;
        	for(int j=0;j<3;j++) {
        		sum+=signals[i][j];
        	}
        	minT=Math.min(minT, sum);
        	sumT[i]=sum;
        }
        
        int connet=1;
        for(int x :sumT) {
        	connet*=x;
        }
        int start=0;
        int ans=-1;
        int timmer=minT;
        while(true) {
        	make(signals);
            for(int i=start;i<timmer;i++) {
            	int flag=0;
            	for(int j=0;j<size;j++) {
            		if(sig[j].get(i)==0)break;
            		if(j==size-1) flag=1;
            	}
            	if(flag==1) {
                    ans=i;
                    break;
                }
            }
            if(ans!=-1)break;
            if(timmer>connet)break;
            start=timmer;
            timmer+=minT;
            
        }
        
        if(ans==-1)return ans;
        
        
        return ans+1;
    }

	private void make(int[][] signals) {
		// TODO Auto-generated method stub
		for(int i=0;i<size;i++) { // 신호등을 고르고
			for(int j=0;j<3;j++) {// 초->노->빨 신호
				int q =signals[i][j];
				if(j==1) {
					for(int k=0;k<q;k++) {
						sig[i].add(1);
					}
					continue;
				}
				for(int k=0;k<q;k++) {
					sig[i].add(0);
				}
			}
		}
		
	}
}