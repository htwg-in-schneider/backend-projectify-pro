package de.htwg.in.schneider.saitenweise.backend.controller;

import de.htwg.in.schneider.saitenweise.backend.model.Task;
import de.htwg.in.schneider.saitenweise.backend.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@SpringBootTest
@Profile("test")
public class TaskControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        taskRepository.deleteAll();
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task t = new Task("Dokument schreiben", "Lucas", "2025-10-01",
                "2025-10-05", "5", "Erledigt");
        taskRepository.save(t);

        mockMvc.perform(get("/api/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Dokument schreiben"))
                .andExpect(jsonPath("$[0].user").value("Lucas"))
                .andExpect(jsonPath("$[0].startDate").value("2025-10-01"))
                .andExpect(jsonPath("$[0].endDate").value("2025-10-05"))
                .andExpect(jsonPath("$[0].duration").value("5"))
                .andExpect(jsonPath("$[0].status").value("Erledigt"));
    }

    @Test
    public void testGetTaskById() throws Exception {
        Task t = new Task("Mockup", "Marie", "2025-10-03",
                "2025-10-11", "9", "Erledigt");
        Long id = taskRepository.save(t).getId();

        mockMvc.perform(get("/api/task/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mockup"))
                .andExpect(jsonPath("$.user").value("Marie"))
                .andExpect(jsonPath("$.duration").value("9"));
    }

    @Test
    public void testGetTaskNotFound() throws Exception {
        mockMvc.perform(get("/api/task/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTask() throws Exception {
        String json = """
                {
                   "title": "Login implementieren",
                   "user": "Anna",
                   "startDate": "2025-02-01",
                   "endDate": "2025-02-05",
                   "duration": "4",
                   "status": "In Bearbeitung"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Login implementieren"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JsonNode n = new ObjectMapper().readTree(content);

        Long id = n.get("id").asLong();
        assertNotNull(id);

        Task saved = taskRepository.findById(id)
                .orElseThrow();

        assertEquals("Login implementieren", saved.getTitle());
        assertEquals("Anna", saved.getUser());
        assertEquals("4", saved.getDuration());
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task t = new Task("Alte Aufgabe", "Tim", "2025-01-01",
                "2025-01-03", "2", "Offen");
        Long id = taskRepository.save(t).getId();

        String updateJson = """
                {
                  "title": "Neue Aufgabe",
                  "user": "Tim",
                  "startDate": "2025-01-02",
                  "endDate": "2025-01-05",
                  "duration": "3",
                  "status": "Review"
                }
                """;

        mockMvc.perform(put("/api/task/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Neue Aufgabe"))
                .andExpect(jsonPath("$.status").value("Review"));

        Task updated = taskRepository.findById(id).orElseThrow();
        assertEquals("Neue Aufgabe", updated.getTitle());
        assertEquals("Review", updated.getStatus());
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task t = new Task("Zu löschen", "Paul", "2025-01-10",
                "2025-01-12", "2", "In Bearbeitung");
        Long id = taskRepository.save(t).getId();

        mockMvc.perform(delete("/api/task/" + id))
                .andExpect(status().isNoContent());

        Optional<Task> deleted = taskRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    
    // Iteration 8: Suche und filter
    

    @Test
    public void testSearchByTitle() throws Exception {
        taskRepository.save(new Task("Dokumentation schreiben", "Lucas",
                "2025-10-01", "2025-10-05", "5", "Erledigt"));

        taskRepository.save(new Task("Mockup erstellen", "Marie",
                "2025-10-03", "2025-10-11", "9", "Erledigt"));

        mockMvc.perform(get("/api/task").param("title", "Dok"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Dokumentation schreiben"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testFilterByStatus() throws Exception {
        taskRepository.save(new Task("Dokumentation", "Lucas",
                "2025-10-01", "2025-10-05", "5", "Erledigt"));

        taskRepository.save(new Task("Mockup erstellen", "Marie",
                "2025-10-03", "2025-10-11", "9", "In Bearbeitung"));

        taskRepository.save(new Task("Login bauen", "Chris",
                "2025-10-03", "2025-10-04", "2", "In Bearbeitung"));

        mockMvc.perform(get("/api/task").param("status", "In Bearbeitung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testSearchByTitleAndStatus() throws Exception {
        taskRepository.save(new Task("Dokumentation", "Lucas",
                "2025-10-01", "2025-10-05", "5", "Erledigt"));

        taskRepository.save(new Task("Mockup erstellen", "Marie",
                "2025-10-03", "2025-10-11", "9", "In Bearbeitung"));

        taskRepository.save(new Task("Login bauen", "Chris",
                "2025-10-03", "2025-10-04", "2", "In Bearbeitung"));

        mockMvc.perform(get("/api/task")
                .param("title", "Login")
                .param("status", "In Bearbeitung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Login bauen"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    
}
