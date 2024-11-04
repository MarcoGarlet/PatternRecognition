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
        Point point = new Point(12.34f, 56.78f);
        point.setId(1L); // Assume the ID is set after saving to the database

        // Mock the save method to return our point
        when(pointRepository.save(any(Point.class))).thenReturn(point);

        // Create a request object with x and y values
        PointController.PointRequest request = new PointController.PointRequest();
        request.setX(12.34f);
        request.setY(56.78f);

        // Act: Perform the POST request
        mockMvc.perform(post("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Convert request to JSON
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().json(objectMapper.writeValueAsString(point))) // Expect JSON response matching the saved point
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.x").value(12.34))
                .andExpect(jsonPath("$.y").value(56.78));
    }       
}
