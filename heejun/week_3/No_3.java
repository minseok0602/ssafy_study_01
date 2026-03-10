package study;

class No_3 {
    public int[] solution(long[] numbers) {
        int[] answer = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            long a = numbers[i];
            String bin = Long.toBinaryString(a);

            // 가장 가까운 포화 이진트리 노드 개수 구하기
            int len = 1;
            while (len < bin.length()) {
                len = len * 2 + 1;
            }

            // 앞에 0 붙이기
            while (bin.length() < len) {
                bin = "0" + bin;
            }

            if (check(bin)) {
                answer[i] = 1;
            } else {
                answer[i] = 0;
            }
        }

        return answer;
    }

    static boolean check(String bin) {
        // 길이가 1이면 리프노드라서 가능
        if (bin.length() == 1) return true;

        int mid = bin.length() / 2;
        char root = bin.charAt(mid);

        String left = bin.substring(0, mid);
        String right = bin.substring(mid + 1);

        // 루트가 0인데 자식 부분에 1이 있으면 불가능
        if (root == '0') {
            if (left.contains("1") || right.contains("1")) {
                return false;
            }
        }

        // 왼쪽, 오른쪽도 재귀적으로 확인
        return check(left) && check(right);
    }
}
