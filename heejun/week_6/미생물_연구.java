
import java.io.*;
import java.util.*;

public class Main {
    static int N, Q;
    static int[][] board;

    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    static class Cell {
        int r, c;

        Cell(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static class Group {
        int id;                 // 투입 순서
        ArrayList<Cell> cells;  // 현재 차지하는 칸들
        int size;               // 넓이
        int minR, minC;         // 정규화용 기준점

        Group(int id) {
            this.id = id;
            this.cells = new ArrayList<>();
            this.size = 0;
            this.minR = Integer.MAX_VALUE;
            this.minC = Integer.MAX_VALUE;
        }

        void addCell(int r, int c) {
            cells.add(new Cell(r, c));
            size++;
            minR = Math.min(minR, r);
            minC = Math.min(minC, c);
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
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());

            // 1. 미생물 투입 ([r1, r2) x [c1, c2))
            insertGroup(r1, c1, r2, c2, id);

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
    static void insertGroup(int r1, int c1, int r2, int c2, int id) {
        for (int r = r1; r < r2; r++) {
            for (int c = c1; c < c2; c++) {
                board[r][c] = id;
            }
        }
    }

    // 분리된 무리 제거
    static void removeSplitGroups(int maxId) {
        boolean[][] visited = new boolean[N][N];
        int[] compCnt = new int[maxId + 1];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] == 0 || visited[r][c]) continue;
                int id = board[r][c];
                compCnt[id]++;
                bfsMark(r, c, id, visited);
            }
        }

        // 연결 컴포넌트가 2개 이상이면 전체 삭제
        for (int id = 1; id <= maxId; id++) {
            if (compCnt[id] >= 2) {
                removeGroup(id);
            }
        }
    }

    static void bfsMark(int sr, int sc, int id, boolean[][] visited) {
        ArrayDeque<Cell> q = new ArrayDeque<>();
        q.offer(new Cell(sr, sc));
        visited[sr][sc] = true;

        while (!q.isEmpty()) {
            Cell cur = q.poll();

            for (int d = 0; d < 4; d++) {
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];

                if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                if (visited[nr][nc]) continue;
                if (board[nr][nc] != id) continue;

                visited[nr][nc] = true;
                q.offer(new Cell(nr, nc));
            }
        }
    }

    static void removeGroup(int id) {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] == id) {
                    board[r][c] = 0;
                }
            }
        }
    }

    // 현재 board에 남아있는 무리들 추출
    static ArrayList<Group> extractGroups(int maxId) {
        Group[] groups = new Group[maxId + 1];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int id = board[r][c];
                if (id == 0) continue;

                if (groups[id] == null) groups[id] = new Group(id);
                groups[id].addCell(r, c);
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

        // 넓이 내림차순, 같으면 먼저 투입된(id 작은) 순
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
        // 기준점(minR, minC) 기준으로 평행이동
        // 가능한 위치 중 (baseR, baseC)가 가장 작은 곳 선택
        for (int baseR = 0; baseR < N; baseR++) {
            for (int baseC = 0; baseC < N; baseC++) {
                if (canPlace(g, newBoard, baseR, baseC)) {
                    doPlace(g, newBoard, baseR, baseC);
                    return;
                }
            }
        }
        // 놓을 수 없으면 사라짐
    }

    static boolean canPlace(Group g, int[][] newBoard, int baseR, int baseC) {
        for (Cell cell : g.cells) {
            int nr = baseR + (cell.r - g.minR);
            int nc = baseC + (cell.c - g.minC);

            if (nr < 0 || nr >= N || nc < 0 || nc >= N) return false;
            if (newBoard[nr][nc] != 0) return false;
        }
        return true;
    }

    static void doPlace(Group g, int[][] newBoard, int baseR, int baseC) {
        for (Cell cell : g.cells) {
            int nr = baseR + (cell.r - g.minR);
            int nc = baseC + (cell.c - g.minC);
            newBoard[nr][nc] = g.id;
        }
    }

    // 실험 결과 계산
    static long calculateScore(int maxId) {
        int[] size = new int[maxId + 1];

        // 각 무리 넓이 계산
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] != 0) {
                    size[board[r][c]]++;
                }
            }
        }

        // 인접한 무리쌍 중복 없이 체크
        boolean[][] seen = new boolean[maxId + 1][maxId + 1];
        long answer = 0;

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int a = board[r][c];
                if (a == 0) continue;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;

                    int b = board[nr][nc];
                    if (b == 0 || a == b) continue;

                    int x = Math.min(a, b);
                    int y = Math.max(a, b);

                    if (!seen[x][y]) {
                        seen[x][y] = true;
                        answer += 1L * size[x] * size[y];
                    }
                }
            }
        }

        return answer;
    }
}
