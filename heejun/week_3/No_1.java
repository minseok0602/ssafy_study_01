package study;

class No_1 {
    public long solution(int cap, int n, int[] deliveries, int[] pickups) {
        long answer = 0;

        int d = n - 1; // 가장 먼 배달 집 인덱스
        int p = n - 1; // 가장 먼 수거 집 인덱스

        while (d >= 0 || p >= 0) {

            // 배달할 집이 남아있는 가장 먼 위치 찾기
            while (d >= 0 && deliveries[d] == 0) {
                d--;
            }

            // 수거할 집이 남아있는 가장 먼 위치 찾기
            while (p >= 0 && pickups[p] == 0) {
                p--;
            }

            // 둘 다 끝났으면 종료
            if (d < 0 && p < 0) {
                break;
            }

            // 이번에 가야 하는 가장 먼 집
            int far = Math.max(d, p);

            // 왕복 거리 추가
            answer += (far + 1L) * 2L;

            // 이번 턴 배달 cap개 처리
            int deliverCap = cap;
            while (d >= 0 && deliverCap > 0) {
                if (deliveries[d] <= deliverCap) {
                    deliverCap -= deliveries[d];
                    deliveries[d] = 0;
                    d--;
                } else {
                    deliveries[d] -= deliverCap;
                    deliverCap = 0;
                }
            }

            // 이번 턴 수거 cap개 처리
            int pickupCap = cap;
            while (p >= 0 && pickupCap > 0) {
                if (pickups[p] <= pickupCap) {
                    pickupCap -= pickups[p];
                    pickups[p] = 0;
                    p--;
                } else {
                    pickups[p] -= pickupCap;
                    pickupCap = 0;
                }
            }
        }

        return answer;
    }
}