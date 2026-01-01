package com.todoapp.dto;

import lombok.Getter;

@Getter
public class UpdateTodoRequest {
  private String password;

  private String todoAuthor;
  private String todoTitle;
}
