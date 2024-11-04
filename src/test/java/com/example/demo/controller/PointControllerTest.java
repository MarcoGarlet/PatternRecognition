package com.example.demo.controller;

import com.example.demo.entity.Point;
import com.example.demo.repository.PointRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.utils.MathUtils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;

@WebMvcTest(PointController.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointRepository pointRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPoint_shouldReturnSavedPoint() throws Exception {
        // Arrange: Create a Point object and set expectations on the repository
        Point point = new Point(12.34d, 56.78d);
        point.setId(1L); // Assume the ID is set after saving to the database

        // Mock the save method to return our point
        when(pointRepository.save(any(Point.class))).thenReturn(point);

        // Create a request object with x and y values
        PointController.PointRequest request = new PointController.PointRequest();
        request.setX(12.34d);
        request.setY(56.78d);

        // Act: Perform the POST request
        String responseContent = mockMvc.perform(post("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Convert request to JSON
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andReturn().getResponse().getContentAsString();

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode pointNode1 = objectMapper.readTree(responseContent);

        // Assert: Check that there are exactly 1 points
        Assertions.assertEquals(1L, pointNode1.get("id").asLong());
        Assertions.assertTrue(MathUtils.areEqual(12.34d, pointNode1.get("x").asDouble()));
        Assertions.assertTrue(MathUtils.areEqual(56.78d, pointNode1.get("y").asDouble()));
    }       
}
