package com.todoapp.repository;

import com.todoapp.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
  List<Todo> findByAuthor(String author);
}
