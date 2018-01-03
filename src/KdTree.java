import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KdTree {

    private Node root;

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(final Node node) {
        if (node == null)
            return 0;
        else
            return node.size;
    }

    public void insert(final Point2D point) {
        checkNotNull(point, "Not supported to insert null as point");
        root = put(root, point, 0, new RectHV(0,0, 1, 1));
    }

    private Node put(final Node node, final Point2D point, final int level, final RectHV rect) {
        if (node == null)
            return new Node(level, point, rect);

        RectHV rectLeft = null;
        RectHV rectRight = null;
        double compare = node.compare(point);

        if (compare < 0 && node.left == null) {
            if (level % 2 == 0)
                rectLeft = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            else
                rectLeft = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.point.y());
        } else if (compare >= 0 && node.right == null) {
            if (level % 2 == 0)
                rectRight = new RectHV(node.point.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
            else
                rectRight = new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.rect.ymax());
        }

        if (compare < 0)
            node.left = put(node.left, point, level + 1, rectLeft);
        else
            if (compare > 0)
                node.right = put(node.right, point, level + 1, rectRight);
        else
            if (!point.equals(node.point))
                node.right = put(node.right, point, level + 1, rectRight);

        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    public boolean contains(final Point2D searchedPoint) {
        checkNotNull(searchedPoint, "Null is never contained in a PointSET");
        return get(root, searchedPoint, 0) != null;
    }

    private Point2D get(final Node node, final Point2D point, final int level) {
        if (node == null)
            return null;

        double compare = node.compare(point);
        if (compare < 0)
            return get(node.left, point, level + 1);
        else if (compare > 0)
            return get(node.right, point, level + 1);
        else if (!point.equals(node.point))
            return get(node.right, point, level + 1);
        else
            return node.point;
    }

    public void draw() {
        draw(root);
    }

    private void draw(final Node node) {
        if (node == null)
            return;

        StdDraw.point(node.point.x(), node.point.y());
        draw(node.left);
        draw(node.right);
    }

    public Iterable<Point2D> range(final RectHV queryRect) {
        checkNotNull(queryRect, "Can't calcalute range for a rect will point null");
        return range(queryRect, root);
    }

    private List<Point2D> range(final RectHV queryRect, final Node node) {
        if (node == null)
            return Collections.emptyList();

        if (node.doesSpittingLineIntersect(queryRect)) {
            List<Point2D> points = new ArrayList<>();
            if (queryRect.contains(node.point))
                points.add(node.point);

            points.addAll(range(queryRect, node.left));
            points.addAll(range(queryRect, node.right));

            return points;
        } else {
            if (node.isRightOf(queryRect))
                return range(queryRect, node.left);
            else return range(queryRect, node.right);
        }
    }

    public Point2D nearest(final Point2D point) {
        checkNotNull(point, "Can't calculate nearest point to a point with point null");
        if (root == null)
            return null;

        return nearest(point, root, root.point, point.distanceTo(root.point));
    }

    private Point2D nearest(final Point2D point, final Node node, final Point2D currentlyClosestPoint,
                            final double currentlyClosestDistance) {
        if (node == null)
            return null;
        Point2D closestPoint = currentlyClosestPoint;
        double closestDistance = currentlyClosestDistance;

        Point2D currentPoint = node.point;
        double currentDistance = point.distanceTo(currentPoint);
        if (currentDistance < closestDistance) {
            closestDistance = currentDistance;
            closestPoint = currentPoint;
        }

        double compare = node.compare(point);
        if (compare < 0)
            currentPoint = nearest(point, node.left, closestPoint, closestDistance);
        else
            currentPoint = nearest(point, node.right, closestPoint, closestDistance);

        if (currentPoint != null) {
            if (currentPoint != closestPoint) {
                closestDistance = currentPoint.distanceTo(point);
                closestPoint = currentPoint;
            }
        }

        double nodeRectDistance = -1;
        if (compare < 0 && node.right != null)
            nodeRectDistance = node.right.rect.distanceTo(point);
        else if (compare >= 0 && node.left != null)
            nodeRectDistance = node.left.rect.distanceTo(point);

        if (nodeRectDistance != -1 && nodeRectDistance < closestDistance) {
            if (compare < 0)
                currentPoint = nearest(point, node.right, closestPoint, closestDistance);
            else
                currentPoint = nearest(point, node.left, closestPoint, closestDistance);
        }

        if (currentPoint != null)
            closestPoint = currentPoint;

        return closestPoint;
    }

    private static void checkNotNull(final Object object, final String message) {
        if (object == null)
            throw new NullPointerException(message);
    }

    private static class Node {
        private final Point2D point;
        private final RectHV rect;
        private final int level;

        private Node left, right;
        private int size;

        public Node(final int level, final Point2D point, final RectHV rect) {
            this.level = level;
            this.point = point;
            this.rect = rect;
            this.size = 1;
        }

        public double compare(final Point2D pointToCompare) {
            if (level % 2 == 0)
                return pointToCompare.x() - point.x();
            else
                return pointToCompare.y() - point.y();
        }

        public boolean doesSpittingLineIntersect(final RectHV rectToCheck) {
            if (level % 2 == 0)
                return rectToCheck.xmin() <= point.x() && point.x() <= rectToCheck.xmax();
            else
                return rectToCheck.ymin() <= point.y() && point.y() <= rectToCheck.ymax();
        }

        public boolean isRightOf(final RectHV rectToCheck) {
            if (level % 2 == 0)
                return rectToCheck.xmin() < point.x() && rectToCheck.xmax() < point.x();
            else
                return rectToCheck.ymin() < point.y() && rectToCheck.ymax() < point.y();
        }
    }
}
