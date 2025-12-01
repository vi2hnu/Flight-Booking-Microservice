package org.example.bookingservice.exception;


import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class GlobalHandlerTest {

    @RestController
    static class DummyController {
        @GetMapping("/userNotFound")
        public void user() { throw new UsersNotFoundException("user not found"); }

        @GetMapping("/ticketNotFound")
        public void ticket() { throw new TicketNotFoundException("ticket not found"); }

        @GetMapping("/invalidScheduleTime")
        public void invalidTime() { throw new InvalidScheduleTimeException("invalid schedule"); }

        @GetMapping("/seatNotAvailable")
        public void seat() { throw new SeatNotAvailableException("seat not available"); }

        @GetMapping("/generic")
        public void generic() { throw new RuntimeException("generic error"); }
    }

    @Test
    void testAllExceptionHandlers() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mockMvc.perform(get("/userNotFound"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("user not found"));

        mockMvc.perform(get("/ticketNotFound"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("ticket not found"));

        mockMvc.perform(get("/invalidScheduleTime"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid schedule"));

        mockMvc.perform(get("/seatNotAvailable"))
                .andExpect(status().isConflict())
                .andExpect(content().string("seat not available"));

        mockMvc.perform(get("/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("generic error"));
    }
}
