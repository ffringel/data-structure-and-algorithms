import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        checkNotNull(p, "Cannot insert null as point");
        points.add(p);
    }

    public boolean contains(Point2D p) {
        checkNotNull(p, "Null is never contained in a PointSET");
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNotNull(rect, "Range cannot be null");

        List<Point2D> solution = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point))
                solution.add(point);
        }

        return solution;
    }

    public Point2D nearest(Point2D p) {
        checkNotNull(p, "Nearest point to a point cannot have value null");

        Point2D nearestPoint = null;
        for (Point2D point : points) {
            if (nearestPoint == null || point.distanceTo(p) < nearestPoint.distanceTo(p))
                nearestPoint = point;
        }

        return nearestPoint;
    }

    private static void checkNotNull(Object object, String message) {
        if (object == null)
            throw new NullPointerException(message);
    }

    public static void main(String[] args) {

    }
}
