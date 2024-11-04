package com.example.demo.controller;

import com.example.demo.entity.Point;
import com.example.demo.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing Point entities.
 * This controller provides an API to create and manage points with x and y coordinates.
 */
@RestController
@RequestMapping("/api/points")  
public class PointController {

    private final PointRepository pointRepository;

    @Autowired
    public PointController(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }
    
    /**
     * Creates a new Point with the given x and y coordinates.
     * 
     * @param pointRequest a JSON object containing the fields x and y (both required as float values).
     * @return the saved Point object, including its generated ID.
     *
     * @apiNote Example JSON request:
     * <pre>
     * {
     *   "x": 12.34,
     *   "y": 56.78
     * }
     * </pre>
     * 
     * Example JSON response:
     * <pre>
     * {
     *   "id": 1,
     *   "x": 12.34,
     *   "y": 56.78
     * }
     * </pre>
     */
    @PostMapping
    public Point createPoint(@RequestBody PointRequest pointRequest) {
        Point point = new Point(pointRequest.getX(), pointRequest.getY());
        return pointRepository.save(point);
    }

    public static class PointRequest {
        private float x;
        private float y;

        public float getX() { return x; }
        public void setX(float x) { this.x = x; }

        public float getY() { return y; }
        public void setY(float y) { this.y = y; }
    }
}
