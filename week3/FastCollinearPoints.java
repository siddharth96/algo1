import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private static final int STEP = 3;

    private final int numSegs;
    private final List<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] ps) {
        validateArg(ps);
        PointWrapper[] points = wrap(ps);
        Arrays.sort(points);
        ArrayList<LineSegment> segments = new ArrayList<>();
        for (PointWrapper p : points) {
            if (p.isUsed) {
                continue;
            }
            List<PointWrapper> otherPoints = getAllExcept(p.getPoint(), points);
            if (otherPoints.size() < STEP) {
                continue;
            }
            otherPoints.sort(p.slopeOrder());

            int i = 0;
            while (i < otherPoints.size() - STEP + 1) {
                PointWrapper p2 = otherPoints.get(i);
                PointWrapper p3 = otherPoints.get(i + 1);
                PointWrapper p4 = otherPoints.get(i + 2);
                if (p.getPoint().slopeTo(p2.getPoint()) == p3.getPoint().slopeTo(p4.getPoint()) &&
                        p2.getPoint().slopeTo(p4.getPoint()) == p.getPoint().slopeTo(p3.getPoint())) {
                    segments.add(new LineSegment(p.getPoint(), p4.getPoint()));
                    i += STEP;
                    p.setUsed(true);
                    p2.setUsed(true);
                    p3.setUsed(true);
                    p4.setUsed(true);
                } else {
                    i++;
                }
            }
        }
        this.segments = segments;
        this.numSegs = segments.size();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private List<PointWrapper> getAllExcept(Point p, PointWrapper[] points) {
        ArrayList<PointWrapper> filteredPoints = new ArrayList<>();
        for (PointWrapper point : points) {
            if (point.getPoint() == p || point.isUsed()) {
                continue;
            }
            filteredPoints.add(point);
        }
        return filteredPoints;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numSegs;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[]{});
    }

    private void validateArg(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
    }

    private PointWrapper[] wrap(Point[] points) {
        PointWrapper[] pointWrappers = new PointWrapper[points.length];
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null || isRepeated(points, p, i + 1)) {
                throw new IllegalArgumentException();
            }
            pointWrappers[i] = new PointWrapper(p);
        }
        return pointWrappers;
    }

    // Not using set as per instructions
    private boolean isRepeated(Point[] points, Point pToCheck, int offset) {
        for (int i = offset; i < points.length; i++) {
            Point p = points[i];
            if (p != null && p.compareTo(pToCheck) == 0) {
                return true;
            }
        }
        return false;
    }

    private static class PointWrapper implements Comparable<PointWrapper> {
        private final Point point;
        private boolean isUsed;

        public PointWrapper(Point point) {
            this.point = point;
        }

        public Point getPoint() {
            return point;
        }

        public boolean isUsed() {
            return isUsed;
        }

        public void setUsed(boolean used) {
            isUsed = used;
        }

        public Comparator<PointWrapper> slopeOrder() {
            return (p1, p2) -> this.getPoint().slopeOrder().compare(p1.getPoint(), p2.getPoint());
        }

        @Override
        public int compareTo(PointWrapper o) {
            return this.getPoint().compareTo(o.getPoint());
        }
    }
}
