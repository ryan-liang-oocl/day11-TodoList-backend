package org.example.todolist.Repo;

import org.example.todolist.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoItem, Integer> {
}
