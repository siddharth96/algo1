import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {

    private final int numSegs;
    private final List<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] ps) {
        validateArg(ps);

        PointWrapper[] points = wrap(ps);
        List<LineSegment> segments = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            PointWrapper pw1 = points[i];
            for (int j = 0; j < points.length; j++) {
                PointWrapper pw2 = points[j];
                if (i == j) {
                    continue;
                }
                for (int k = 0; k < points.length; k++) {
                    PointWrapper pw3 = points[k];
                    if (i == k || j == k) {
                        continue;
                    }
                    for (int l = 0; l < points.length; l++) {
                        PointWrapper pw4 = points[l];
                        if (i == l || j == l || k == l || pw1.isUsed() || pw2.isUsed() || pw3.isUsed() || pw4.isUsed()) {
                            continue;
                        }
                        Point p1 = pw1.getPoint();
                        Point p2 = pw2.getPoint();
                        Point p3 = pw3.getPoint();
                        Point p4 = pw4.getPoint();
                        if (p1.slopeTo(p2) == p3.slopeTo(p4) && p1.slopeTo(p3) == p2.slopeTo(p4)) {
                            pw1.setUsed(true);
                            pw2.setUsed(true);
                            pw3.setUsed(true);
                            pw4.setUsed(true);
                            segments.add(new LineSegment(min(p1, p2, p3, p4), max(p1, p2, p3, p4)));
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
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

    private Point min(Point... points) {
        Arrays.sort(points);
        return points[0];
    }

    private Point max(Point... points) {
        Arrays.sort(points, Comparator.reverseOrder());
        return points[0];
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

    private static class PointWrapper {
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
    }
}
