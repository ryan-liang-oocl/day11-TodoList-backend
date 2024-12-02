package org.example.todolist.controller;

import org.example.todolist.model.TodoItem;
import org.example.todolist.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todolist")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoListController {
    @Autowired
    private TodoListService todoListService;

    @GetMapping
    public List<TodoItem> getAll() {
        return todoListService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoItem add(@RequestBody TodoItem todoItem) {
        return todoListService.save(todoItem);
    }

    @PutMapping("/{id}")
    public TodoItem update(@PathVariable Integer id, @RequestBody TodoItem todoItem) {
        return todoListService.update(id, todoItem);
    }

    @DeleteMapping("/{id}")
    public TodoItem delete(@PathVariable Integer id) {
        return todoListService.delete(id);
    }

    @GetMapping("/{id}")
    public TodoItem getById(@PathVariable Integer id) {
        return todoListService.get(id);
    }

}
