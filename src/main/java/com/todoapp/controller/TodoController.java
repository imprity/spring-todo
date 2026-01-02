package com.todoapp.controller;

import com.todoapp.dto.CreateTodoRequest;
import com.todoapp.dto.DeleteTodoRequest;
import com.todoapp.dto.TodoResponse;
import com.todoapp.dto.UpdateTodoRequest;
import com.todoapp.service.TodoService;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/api/todos/{todoId}")
  public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long todoId) {
    TodoResponse res = todoService.getTodoById(todoId);
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

  @PutMapping("/api/todos/{todoId}")
  public ResponseEntity<TodoResponse> updateTodo(
      @PathVariable Long todoId, @RequestBody UpdateTodoRequest req) {
    TodoResponse res = todoService.updateTodo(todoId, req);
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

  @DeleteMapping("/api/todos/{todoId}")
  public ResponseEntity<Void> deleteTodo(
      @PathVariable Long todoId, @RequestBody DeleteTodoRequest req) {
    todoService.deleteTodo(todoId, req);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
