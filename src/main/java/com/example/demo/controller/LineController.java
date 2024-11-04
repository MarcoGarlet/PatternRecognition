package com.example.demo.controller;

import com.example.demo.entity.Point;
import com.example.demo.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for managing line segments that pass through multiple points.
 */
@RestController
@RequestMapping("/lines")
public class LineController {

    private final PointRepository pointRepository;

    /**
     * Constructs a LineController with the specified PointRepository.
     *
     * @param pointRepository the repository used to interact with Point entities in the database.
     */
    @Autowired
    public LineController(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    /**
     * Retrieves all line segments that pass through at least 'n' points.
     *
     * @param n the minimum number of points a line segment must pass through.
     * @return a list of line segments, each represented as a list of points.
     */
    @GetMapping("/{n}")
    public List<List<Point>> findLineSegments(@PathVariable int n) {
        List<Point> points = pointRepository.findAll();
        Map<Line, Set<Point>> lineMap = new HashMap<>();

        // Iterate through all pairs of points to define lines, 
        // Points belonging to the same line (i.e., with the same slope and intercept) 
        // will be grouped in the same entry in the map `lineMap`, which serves as a "bucket" 
        // for each unique line.        
         for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);

                Line line = new Line(p1, p2);
                lineMap.putIfAbsent(line, new HashSet<>());
                lineMap.get(line).add(p1);
                lineMap.get(line).add(p2);
            }
        }

        // Filter lines that pass through at least 'n' points and convert to list format
        return lineMap.values().stream()
                .filter(pointSet -> pointSet.size() >= n)
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    /**
     * Helper class to represent a line using slope and intercept.
     */
    private static class Line {
        private static final double TOLERANCE = 1e-6;
        private final double slope;
        private final double intercept;

        /**
         * Constructs a line passing through two points.
         *
         * @param p1 the first point.
         * @param p2 the second point.
         */
        public Line(Point p1, Point p2) {
            if (Math.abs(p1.getX() - p2.getX()) < TOLERANCE) {
                // Vertical line: undefined slope, use a special value
                this.slope = Double.POSITIVE_INFINITY;
                this.intercept = p1.getX(); // Use x-coordinate for vertical lines
            } else {
                // Regular line: calculate slope and intercept
                this.slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
                this.intercept = p1.getY() - this.slope * p1.getX();
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(slope, intercept);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Line)) return false;
            Line line = (Line) o;
            return Math.abs(this.slope - line.slope) < TOLERANCE &&
                   Math.abs(this.intercept - line.intercept) < TOLERANCE;
        }
    }
}
