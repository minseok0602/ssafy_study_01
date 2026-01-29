import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        int result = Integer.MAX_VALUE;
        for(int i =0 ;i<n;i++){
            int sum = 0;
            for(int j = 0;j<n;j++){
                sum+=a[j]*(Math.abs(i-j));
            }
            result = Math.min(result,sum);
        }
        System.out.println(result);
    }
}