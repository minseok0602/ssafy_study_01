package week_3;

public class ExpressionBinaryTree {
    public int[] solution(long[] numbers) {
        int[] answer = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            String binary = Long.toBinaryString(numbers[i]);
            int h = 1;
            while (h < binary.length()) {
                h = h * 2 + 1;
            }
            binary = "0".repeat(h - binary.length()) + binary;
            answer[i] = isPossible(binary) ? 1 : 0;
        }
        return answer;
    }

    static boolean isPossible(String b) {
        if (b.length() == 1) return true;

        int mid = b.length() / 2;
        char root = b.charAt(mid);

        String left = b.substring(0, mid);
        String right = b.substring(mid + 1);

        if (root == '0') {
            if (left.contains("1") || right.contains("1")) return false;
        }

        return isPossible(left) && isPossible(right);
    }
}
