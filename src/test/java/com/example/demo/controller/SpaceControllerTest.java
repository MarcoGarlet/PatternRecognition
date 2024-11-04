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
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for SpaceController.
 * Validates the endpoint for retrieving all points in space.
 */
@WebMvcTest(SpaceController.class)
public class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointRepository pointRepository; // Mocking the repository

    /**
     * Test for the findAllPoints endpoint.
     * Verifies that the endpoint returns all points correctly.
     *
     * @throws Exception if the test fails due to an unexpected error.
     */
    @Test
    public void findAllPoints_shouldReturnListOfPoints() throws Exception {
        // Arrange: Create a list of mock points to be returned by the repository
        Point point1 = new Point(10.0f, 20.0f);
        point1.setId(1L);
        Point point2 = new Point(30.0f, 40.0f);
        point2.setId(2L);
        List<Point> points = Arrays.asList(point1, point2);

        // Mock the findAll method to return the mock points
        when(pointRepository.findAll()).thenReturn(points);

        // Act & Assert: Perform GET request and check the response
        mockMvc.perform(get("/space")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2)) // Expecting two points
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].x").value(10.0))
                .andExpect(jsonPath("$[0].y").value(20.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].x").value(30.0))
                .andExpect(jsonPath("$[1].y").value(40.0));
    }

    /**
     * Test for the deleteAllPoints endpoint.
     * Verifies that the endpoint calls deleteAll in the repository.
     *
     * @throws Exception if the test fails due to an unexpected error.
     */
    @Test
    public void deleteAllPoints_shouldCallDeleteAllInRepository() throws Exception {
        // Act: Perform DELETE request
        mockMvc.perform(delete("/space"))
                .andExpect(status().isOk()); // Expect HTTP 200 OK

        // Assert: Verify that deleteAll was called once
        Mockito.verify(pointRepository, times(1)).deleteAll();
    }
}
