package com.example.demo.controller;

import com.example.demo.entity.Point;
import com.example.demo.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller for managing endpoints related to space, including points.
 * Provides an API to retrieve all points in space.
 */
@RestController
@RequestMapping("/space")
public class SpaceController {

    private final PointRepository pointRepository;

    /**
     * Constructs a SpaceController with the specified PointRepository.
     *
     * @param pointRepository the repository used to interact with Point entities in the database.
     */
    @Autowired
    public SpaceController(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    /**
     * Retrieves all points in space.
     * 
     * <p>This endpoint returns a list of all points stored in the database,
     * each represented by x and y coordinates.</p>
     *
     * @return a list of all points available in the database.
     */
    @GetMapping
    public List<Point> findAllPoints() {
        return pointRepository.findAll();
    }
}
