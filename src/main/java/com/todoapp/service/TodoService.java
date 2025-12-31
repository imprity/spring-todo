package com.todoapp.service;

import com.todoapp.dto.CreateTodoRequest;
import com.todoapp.dto.TodoResponse;
import com.todoapp.entity.Todo;
import com.todoapp.repository.TodoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {
  private final TodoRepository repo;

  public TodoService(TodoRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public TodoResponse createTodo(CreateTodoRequest req) {
    // TODO: check password
    Todo todo =
        new Todo(req.getTodoAuthor(), req.getTodoTitle(), req.getTodoBody(), req.getTodoDate());

    repo.save(todo);

    return new TodoResponse(
        todo.getId(),
        todo.getAuthor(),
        todo.getTitle(),
        todo.getBody(),
        todo.getDate(),
        todo.getCreatedAt(),
        todo.getModifiedAt());
  }

  @Transactional
  public List<TodoResponse> getTodos() {
    return repo.findAll().stream()
        .map(
            todo ->
                new TodoResponse(
                    todo.getId(),
                    todo.getAuthor(),
                    todo.getTitle(),
                    todo.getBody(),
                    todo.getDate(),
                    todo.getCreatedAt(),
                    todo.getModifiedAt()))
        .collect(Collectors.toList());
  }
}
