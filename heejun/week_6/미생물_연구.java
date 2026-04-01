import java.io.*;
import java.util.*;

public class Main {
    static int N, Q;
    static int[][] board;   // board[y][x]

    // 상 우 하 좌
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {-1, 0, 1, 0};

    static class Cell {
        int x, y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Group {
        int id;                 // 투입 순서
        ArrayList<Cell> cells;  // 현재 차지하는 칸들
        int size;               // 넓이
        int minX, minY;         // 정규화 기준점

        Group(int id) {
            this.id = id;
            this.cells = new ArrayList<>();
            this.size = 0;
            this.minX = Integer.MAX_VALUE;
            this.minY = Integer.MAX_VALUE;
        }

        void addCell(int x, int y) {
            cells.add(new Cell(x, y));
            size++;
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        StringBuilder sb = new StringBuilder();

        for (int id = 1; id <= Q; id++) {
            st = new StringTokenizer(br.readLine());

            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());

            // 1. 미생물 투입: [x1, x2) x [y1, y2)
            insertGroup(x1, y1, x2, y2, id);

            // 2. 기존 무리 중 분리된 무리 제거
            removeSplitGroups(id);

            // 3. 새 배양 용기로 이동
            board = migrateBoard(id);

            // 4. 실험 결과 계산
            long result = calculateScore(id);
            sb.append(result).append('\n');
        }

        System.out.print(sb);
    }

    // 직사각형 영역 덮어쓰기
    static void insertGroup(int x1, int y1, int x2, int y2, int id) {
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                board[y][x] = id;
            }
        }
    }

    // 분리된 무리 제거
    static void removeSplitGroups(int maxId) {
        boolean[][] visited = new boolean[N][N];
        int[] compCnt = new int[maxId + 1];

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (board[y][x] == 0 || visited[y][x]) continue;

                int id = board[y][x];
                compCnt[id]++;
                bfsMark(x, y, id, visited);
            }
        }

        // 연결 컴포넌트가 2개 이상이면 전체 삭제
        for (int id = 1; id <= maxId; id++) {
            if (compCnt[id] >= 2) {
                removeGroup(id);
            }
        }
    }

    static void bfsMark(int sx, int sy, int id, boolean[][] visited) {
        ArrayDeque<Cell> q = new ArrayDeque<>();
        q.offer(new Cell(sx, sy));
        visited[sy][sx] = true;

        while (!q.isEmpty()) {
            Cell cur = q.poll();

            for (int d = 0; d < 4; d++) {
                int nx = cur.x + dx[d];
                int ny = cur.y + dy[d];

                if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
                if (visited[ny][nx]) continue;
                if (board[ny][nx] != id) continue;

                visited[ny][nx] = true;
                q.offer(new Cell(nx, ny));
            }
        }
    }

    static void removeGroup(int id) {
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (board[y][x] == id) {
                    board[y][x] = 0;
                }
            }
        }
    }

    // 현재 board에 남아있는 무리들 추출
    static ArrayList<Group> extractGroups(int maxId) {
        Group[] groups = new Group[maxId + 1];

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int id = board[y][x];
                if (id == 0) continue;

                if (groups[id] == null) groups[id] = new Group(id);
                groups[id].addCell(x, y);
            }
        }

        ArrayList<Group> list = new ArrayList<>();
        for (int id = 1; id <= maxId; id++) {
            if (groups[id] != null) list.add(groups[id]);
        }
        return list;
    }

    // 새 배양 용기로 이동
    static int[][] migrateBoard(int maxId) {
        ArrayList<Group> groups = extractGroups(maxId);

        // 넓이 내림차순, 같으면 먼저 투입된 번호가 작은 순
        groups.sort((a, b) -> {
            if (a.size != b.size) return Integer.compare(b.size, a.size);
            return Integer.compare(a.id, b.id);
        });

        int[][] newBoard = new int[N][N];

        for (Group g : groups) {
            placeGroup(g, newBoard);
        }

        return newBoard;
    }

    // 한 무리를 새 보드에 배치
    static void placeGroup(Group g, int[][] newBoard) {
        // x가 가장 작은 위치, 같으면 y가 가장 작은 위치
        for (int baseX = 0; baseX < N; baseX++) {
            for (int baseY = 0; baseY < N; baseY++) {
                if (canPlace(g, newBoard, baseX, baseY)) {
                    doPlace(g, newBoard, baseX, baseY);
                    return;
                }
            }
        }
        // 놓을 수 없으면 사라짐
    }

    static boolean canPlace(Group g, int[][] newBoard, int baseX, int baseY) {
        for (Cell cell : g.cells) {
            int nx = baseX + (cell.x - g.minX);
            int ny = baseY + (cell.y - g.minY);

            if (nx < 0 || nx >= N || ny < 0 || ny >= N) return false;
            if (newBoard[ny][nx] != 0) return false;
        }
        return true;
    }

    static void doPlace(Group g, int[][] newBoard, int baseX, int baseY) {
        for (Cell cell : g.cells) {
            int nx = baseX + (cell.x - g.minX);
            int ny = baseY + (cell.y - g.minY);
            newBoard[ny][nx] = g.id;
        }
    }

    // 실험 결과 계산
    static long calculateScore(int maxId) {
        int[] size = new int[maxId + 1];

        // 각 무리 넓이 계산
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                if (board[y][x] != 0) {
                    size[board[y][x]]++;
                }
            }
        }

        // 인접한 무리쌍 중복 없이 체크
        boolean[][] seen = new boolean[maxId + 1][maxId + 1];
        long answer = 0;

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int a = board[y][x];
                if (a == 0) continue;

                for (int d = 0; d < 4; d++) {
                    int nx = x + dx[d];
                    int ny = y + dy[d];

                    if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;

                    int b = board[ny][nx];
                    if (b == 0 || a == b) continue;

                    int g1 = Math.min(a, b);
                    int g2 = Math.max(a, b);

                    if (!seen[g1][g2]) {
                        seen[g1][g2] = true;
                        answer += 1L * size[g1] * size[g2];
                    }
                }
            }
        }

        return answer;
    }
}