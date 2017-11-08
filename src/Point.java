import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double slopeTo(Point that) {
        if (this.y - that.y == 0) {
            if (this.x - that.x == 0) {
                return Double.NEGATIVE_INFINITY;
            }
            return +0;
        } else if (this.x - that.x == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return (that.y - this.y) / (double) (that.x - this.x);
    }

    @Override
    public int compareTo(Point that) {
        int diff = this.y - that.y;
        if (diff == 0) {
            diff = this.x - that.x;
        }
        return diff;
    }

    public Comparator<Point> slopeOrder() {
        return (pointA, pointB) -> {
            double slopeDiff = slopeTo(pointA) - slopeTo(pointB);
            if (slopeDiff > 0) {
                return 1;
            } else if (slopeDiff < 0) {
                return -1;
            } else {
                return 0;
            }
        };
    }
}
