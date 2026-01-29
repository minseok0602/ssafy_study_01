import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();
        int result = 0;
        for(int i = 0;i<n;i++){
            int a = arr[i];
            for(int j = i+1;j<n;j++){
                int b = arr[j];
                if(b<a)
                    continue;
                for(int k = j+1;k<n;k++){
                    int c = arr[k];
                    if(c<b)
                        continue;
                    result++;
                }
            }
        }
        System.out.println(result);
    }
}