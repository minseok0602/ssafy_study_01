import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String str ="12345^&*()_";
        for(int i = 0;i<n;i++){
            System.out.print(str);
            System.out.println();
        }
    }
}