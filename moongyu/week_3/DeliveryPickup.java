package week_3;

public class DeliveryPickup {
    public long solution(int cap, int n, int[] deliveries, int[] pickups) {
        long answer = 0;

        int d = n - 1;
        int p = n - 1;

        while (true) {
            while (d >= 0 && deliveries[d] == 0) d--;
            while (p >= 0 && pickups[p] == 0) p--;

            if (d < 0 && p < 0) break;

            int far = Math.max(d, p);
            answer += (long) (far + 1) * 2;

            int deliveryCap = cap;
            while (d >= 0 && deliveryCap > 0) {
                if (deliveries[d] <= deliveryCap) {
                    deliveryCap -= deliveries[d];
                    deliveries[d] = 0;
                    d--;
                } else {
                    deliveries[d] -= deliveryCap;
                    deliveryCap = 0;
                }
            }

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
