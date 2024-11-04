package com.example.demo.controller;

import com.example.demo.entity.Point;
import com.example.demo.repository.PointRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for LineController.
 * Validates the endpoint for finding line segments with at least 'n' points.
 */
@WebMvcTest(LineController.class)
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointRepository pointRepository;

    /**
     * Test for the findLineSegments endpoint.
     * Verifies that the endpoint returns the correct line segments passing through at least 'n' points.
     *
     * @throws Exception if the test fails due to an unexpected error.
     */
    @Test
    public void findLineSegments_shouldReturnLineSegmentsWithAtLeastNPoints() throws Exception {
        // Arrange: Define points that lie on the same lines
        Point p1 = new Point(0.0d, 0.0d);
        p1.setId(1L);
        Point p2 = new Point(1.0d, 1.0d); // Lies on y = x with p1 and p3
        p2.setId(2L);
        Point p3 = new Point(2.0d, 2.0d);
        p3.setId(3L);
        Point p4 = new Point(3.0d, 3.0d); // Lies on y = x with p1, p2, p3
        p4.setId(4L);
        Point p5 = new Point(0.0d, 1.0d); // Independent point, doesn't fall on y = x

        List<Point> points = Arrays.asList(p1, p2, p3, p4, p5);

        // Mock the findAll method to return our predefined points
        when(pointRepository.findAll()).thenReturn(points);

        // Act & Assert: Perform GET request and validate response
        mockMvc.perform(get("/lines/3")  // Request lines passing through at least 3 points
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // Expect 1 line segment
                .andExpect(jsonPath("$[0].length()").value(4)); // Line segment has 4 points
    }
}
