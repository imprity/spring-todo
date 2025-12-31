package com.todoapp.controller;

import com.todoapp.dto.CreateTodoRequest;
import com.todoapp.dto.TodoResponse;
import com.todoapp.service.TodoService;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoController {

  private final TodoService todoService;

  @PostMapping("/api/todos")
  public ResponseEntity<TodoResponse> createTodo(@RequestBody CreateTodoRequest req) {
    TodoResponse res = todoService.createTodo(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @GetMapping("/api/todos")
  public ResponseEntity<List<TodoResponse>> getTodos(
      @RequestParam(required = false) Optional<String> author) {
    List<TodoResponse> res = todoService.getTodos(author);
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }
}
