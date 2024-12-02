package org.example.todolist.service;

import org.example.todolist.Repo.TodoListRepository;
import org.example.todolist.exception.TodoListException;
import org.example.todolist.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListService {
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
            TodoListException.NotFoundException();
        }
        existingTodoItem.setText(todoItem.getText());
        existingTodoItem.setDone(todoItem.getDone());
        return todoListRepository.save(existingTodoItem);
    }


    public TodoItem delete(Integer id) {
        TodoItem todoItem = todoListRepository.findById(id).orElse(null);
        if (todoItem == null) {
            TodoListException.NotFoundException();
        }
        todoListRepository.delete(todoItem);
        return todoItem;
    }

    public TodoItem get(Integer id) {
        TodoItem todoItem = todoListRepository.findById(id).orElse(null);
        if (todoItem == null) {
            TodoListException.NotFoundException();
        }
        return todoItem;
    }
}
