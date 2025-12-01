package org.example.flightservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class GlobalExceptionHandlerTest {

    @RestController
    static class DummyController {
        @GetMapping("/cityNotFound")
        public void city() { throw new CityNotFoundException("city not found"); }

        @GetMapping("/flightNotFound")
        public void flight() { throw new FlightNotFoundException("flight not found"); }

        @GetMapping("/scheduleConflict")
        public void conflict() { throw new ScheduleConflictException("conflict"); }

        @GetMapping("/invalidScheduleTime")
        public void invalidTime() { throw new InvalidScheduleTimeException("invalid time"); }

        @GetMapping("/scheduleNotFound")
        public void schedule() { throw new ScheduleNotFoundException("schedule not found"); }

        @GetMapping("/generic")
        public void generic() { throw new RuntimeException("generic"); }
    }

    @Test
    void testAllExceptionHandlers() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mockMvc.perform(get("/cityNotFound"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("city not found"));

        mockMvc.perform(get("/flightNotFound"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("flight not found"));

        mockMvc.perform(get("/scheduleConflict"))
                .andExpect(status().isConflict())
                .andExpect(content().string("conflict"));

        mockMvc.perform(get("/invalidScheduleTime"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid time"));

        mockMvc.perform(get("/scheduleNotFound"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("schedule not found"));

        mockMvc.perform(get("/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("generic"));
    }
}
