package org.example.todolist.service;

import org.example.todolist.Repo.TodoListRepository;
import org.example.todolist.exception.NotFoundException;
import org.example.todolist.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListService {
    public static final String TODO_ITEM_NOT_FOUND_MSG = "Todo item not found";
    @Autowired
    private TodoListRepository todoListRepository;
    public List<TodoItem> getAll() {
        return todoListRepository.findAll();
    }

    public TodoItem save(TodoItem todoItem) {
        return todoListRepository.save(todoItem);
    }

    public TodoItem update(Integer id, TodoItem todoItem) {
        TodoItem existingTodoItem = todoListRepository.findById(id).orElse(null);
        if (existingTodoItem == null) {
            throw new NotFoundException(TODO_ITEM_NOT_FOUND_MSG);
        }
        existingTodoItem.setText(todoItem.getText());
        existingTodoItem.setDone(todoItem.getDone());
        return todoListRepository.save(existingTodoItem);
    }


    public TodoItem delete(Integer id) {
        TodoItem todoItem = todoListRepository.findById(id).orElse(null);
        if (todoItem == null) {
            throw new NotFoundException(TODO_ITEM_NOT_FOUND_MSG);
        }
        todoListRepository.delete(todoItem);
        return todoItem;
    }

    public TodoItem get(Integer id) {
        TodoItem todoItem = todoListRepository.findById(id).orElse(null);
        if (todoItem == null) {
            throw new NotFoundException(TODO_ITEM_NOT_FOUND_MSG);
        }
        return todoItem;
    }
}
