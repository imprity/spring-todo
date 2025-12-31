package com.todoapp.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateTodoRequest {
  private String password;

  private String todoAuthor;
  private String todoTitle;
  private String todoBody;
  private LocalDateTime todoDate;
}
