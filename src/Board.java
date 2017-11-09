import edu.princeton.cs.algs4.Queue;

public class Board {
    private int N;  //Dimensions of puzzle board
    private byte[][] tiles = null; //Puzzle board

    public Board(int[][] blocks) {
        N = blocks.length;

        if (N < 2 || N >= 128)
            throw new IndexOutOfBoundsException("size not supported : " + N);
        this.tiles = new byte[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = (byte) blocks[i][j];
            }
        }
    }

    private Board(byte[][] blocks) {
        this.tiles = copySquareArray(blocks);

        N = tiles.length;
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0)
                    continue;
                if (tiles[i][j] != (N * i + j + 1))
                    hamming++;
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0)
                    continue;
                int row = (tiles[i][j] - 1) / N;
                int col = (tiles[i][j] - 1) % N;

                manhattan += (Math.abs(i - row) + Math.abs(j - col));
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        return (hamming() == 0);
    }

    public Board twin() {
        byte[][] copy = copySquareArray(tiles);

        if (N <= 1)
            return new Board(copy);

        int row = 0;
        int col = 0;
        byte value = 0;
        byte lastValue = tiles[0][0];

        zerosearch:
        for (row = 0; row < N; row++) {
            for (col = 0; col < N; col++) {
                value = tiles[row][col];
                if (value != 0 && lastValue != 0 && col > 0)
                    break zerosearch;
                lastValue = value;
            }
        }
        copy[row][col] = lastValue;
        copy[row][col - 1] = value;

        return new Board(copy);
    }

    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<>();

        int row = 0;
        int col = 0;

        zerosearch:
        for (row = 0; row < N; row++) {
            for (col = 0; col < N; col++) {
                if (tiles[row][col] == 0)
                    break zerosearch;
            }
        }

        if (row > 0)
            queue.enqueue(new Board(swap(tiles, row, col, row - 1, col)));
        if (row < N - 1)
            queue.enqueue(new Board(swap(tiles, row, col, row + 1, col)));
        if (col > 0)
            queue.enqueue(new Board(swap(tiles, row, col, row, col - 1)));
        if (col < N - 1)
            queue.enqueue(new Board(swap(tiles, row, col, row, col + 1)));

        return queue;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(N).append("\n");

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                stringBuilder.append(String.format("%2d ", tiles[i][j]));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private byte[][] swap(byte[][] array, int fromRow, int fromCol, int toRow, int toCol) {
        byte[][] copy = copySqaureArray(array);
        byte tmp = copy[toRow][toCol];

        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = tmp;

        return copy;
    }

    private byte[][] copySquareArray(byte[][] original) {
        int len = original.length;
        byte[][] copy = new byte[len][len];

        for (int row = 0; row < len; row++) {
            assert original[row].length == len;

            System.arraycopy(original[row], 0, copy[row], 0, len);
        }

        return copy;
    }

    private byte[][] copySqaureArray(byte[][] array) {
        int len = array.length;
        byte[][] copy = new byte[len][len];

        for (int row = 0; row <  len; row++) {
            assert array[row].length == len;
            System.arraycopy(array[row], 0, copy[row], 0, len);
        }

        return copy;
    }
}
