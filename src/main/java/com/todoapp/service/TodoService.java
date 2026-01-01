package com.todoapp.service;

import com.todoapp.dto.CreateTodoRequest;
import com.todoapp.dto.TodoResponse;
import com.todoapp.entity.Todo;
import com.todoapp.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {
  private final TodoRepository repo;

  // TODO: password를 설정 가능하게 만들기
  private final String password = "69420";

  public TodoService(TodoRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public TodoResponse createTodo(CreateTodoRequest req) {
    if (!password.equals(req.getPassword())) {
      throw new WrongPasswordException();
    }

    Todo todo =
        new Todo(req.getTodoAuthor(), req.getTodoTitle(), req.getTodoBody(), req.getTodoDate());

    repo.save(todo);

    return todoToTodoResponse(todo);
  }

  @Transactional(readOnly = true)
  public List<TodoResponse> getTodos(Optional<String> author) {
    if (author.isPresent()) {
      return repo.findByAuthor(author.get()).stream()
          .map(this::todoToTodoResponse)
          .collect(Collectors.toList());
    } else {
      return repo.findAll().stream().map(this::todoToTodoResponse).collect(Collectors.toList());
    }
  }

  @Transactional(readOnly = true)
  public TodoResponse getTodoById(Long id) {
    Todo todo = repo.findById(id).orElseThrow(() -> new IllegalStateException("존재하지 않는 일정 입니다."));
    return todoToTodoResponse(todo);
  }

  private TodoResponse todoToTodoResponse(Todo todo) {
    return new TodoResponse(
        todo.getId(),
        todo.getAuthor(),
        todo.getTitle(),
        todo.getBody(),
        todo.getDate(),
        todo.getCreatedAt(),
        todo.getModifiedAt());
  }
}
