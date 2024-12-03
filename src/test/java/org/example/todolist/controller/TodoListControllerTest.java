package org.example.todolist.controller;

import org.example.todolist.Repo.TodoListRepository;
import org.example.todolist.exception.NotFoundException;
import org.example.todolist.model.TodoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TodoListControllerTest {
    @Autowired
    private MockMvc client;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private JacksonTester<List<TodoItem>> todoListJson;

    @Autowired
    private JacksonTester<TodoItem> todoItemJson;

    @BeforeEach
    public void setUp() {
        todoListRepository.deleteAll();
        todoListRepository.save(new TodoItem("Buy milk", false));
        todoListRepository.save(new TodoItem("Buy eggs", false));
        todoListRepository.save(new TodoItem("Buy bread", false));
        todoListRepository.save(new TodoItem("Buy butter", false));
    }

    @Test
    public void should_return_all_todo_items_when_getAll_given_init() throws Exception {
        List<TodoItem> expectTodoList = todoListRepository.findAll();
        String todoListAsString = client.perform(MockMvcRequestBuilders.get("/todolist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andReturn().getResponse().getContentAsString();
        List<TodoItem> todoList = todoListJson.parseObject(todoListAsString);
        assertThat(todoList)
                .usingRecursiveComparison()
                .isEqualTo(expectTodoList);

    }

    @Test
    public void should_return_todo_item_when_create_given_todo_item() throws Exception {
        TodoItem todoItem = new TodoItem("Buy milk", false);
        JsonContent<TodoItem> todoJson = todoItemJson.write(todoItem);
        String todoItemAsString = todoJson.getJson();
        String todoItemResponse = client.perform(MockMvcRequestBuilders.post("/todolist")
                .contentType("application/json")
                .content(todoItemAsString))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TodoItem todoItemResponseObject = todoItemJson.parseObject(todoItemResponse);
        assertThat(todoItemResponseObject)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(todoItem);
    }

    @Test
    public void should_return_updated_todo_item_when_update_done_given_todo_item() throws Exception {
        TodoItem todoItem = new TodoItem("Buy water", false);
        todoItem = todoListRepository.save(todoItem);
        todoItem.setDone(true);
        JsonContent<TodoItem> todoJson = todoItemJson.write(todoItem);
        String todoItemAsString = todoJson.getJson();
        String todoItemResponse = client.perform(MockMvcRequestBuilders.put("/todolist/" + todoItem.getId())
                .contentType("application/json")
                .content(todoItemAsString))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TodoItem todoItemResponseObject = todoItemJson.parseObject(todoItemResponse);
        assertThat(todoItemResponseObject)
                .usingRecursiveComparison()
                .isEqualTo(todoItem);
    }

    @Test
    public void should_return_deleted_todo_item_when_delete_given_todo_item_id() throws Exception {
        TodoItem todoItem = new TodoItem("Buy water", false);
        todoItem = todoListRepository.save(todoItem);
        String todoItemResponse = client.perform(MockMvcRequestBuilders.delete("/todolist/" + todoItem.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TodoItem todoItemResponseObject = todoItemJson.parseObject(todoItemResponse);
        assertThat(todoItemResponseObject)
                .usingRecursiveComparison()
                .isEqualTo(todoItem);
    }

    @Test
    public void should_return_todo_item_when_getById_given_todo_item_id() throws Exception {
        TodoItem todoItem = new TodoItem("Buy water", false);
        todoItem = todoListRepository.save(todoItem);
        String todoItemResponse = client.perform(MockMvcRequestBuilders.get("/todolist/" + todoItem.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TodoItem todoItemResponseObject = todoItemJson.parseObject(todoItemResponse);
        assertThat(todoItemResponseObject)
                .usingRecursiveComparison()
                .isEqualTo(todoItem);
    }

    @Test
    public void should_throw_exception_when_getById_given_invalid_todo_item_id() throws Exception {
        client.perform(MockMvcRequestBuilders.get("/todolist/100"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(NotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("This todo item does not exist"));
    }
}
