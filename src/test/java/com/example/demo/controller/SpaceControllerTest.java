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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;

import com.example.demo.utils.MathUtils;

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
        Point point1 = new Point(10.0d, 20.0d);
        point1.setId(1L);
        Point point2 = new Point(30.0d, 40.0d);
        point2.setId(2L);
        List<Point> points = Arrays.asList(point1, point2);

        // Mock the findAll method to return the mock points
        when(pointRepository.findAll()).thenReturn(points);

        // Act & Assert: Perform GET request and check the response
        String responseContent = mockMvc.perform(get("/space")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseContent);

        // Assert: Check that there are exactly 2 points
        Assertions.assertEquals(2, rootNode.size());

        // Check each point within range
        JsonNode pointNode1 = rootNode.get(0);
        Assertions.assertEquals(1L, pointNode1.get("id").asLong());
        Assertions.assertTrue(MathUtils.areEqual(10.0d, pointNode1.get("x").asDouble()));
        Assertions.assertTrue(MathUtils.areEqual(20.0d, pointNode1.get("y").asDouble()));

        JsonNode pointNode2 = rootNode.get(1);
        Assertions.assertEquals(2L, pointNode2.get("id").asLong());
        Assertions.assertTrue(MathUtils.areEqual(30.0d, pointNode2.get("x").asDouble()));
        Assertions.assertTrue(MathUtils.areEqual(40.0d, pointNode2.get("y").asDouble()));
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
