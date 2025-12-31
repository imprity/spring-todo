package com.todoapp.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoResponse {
  private final Long todoId;

  private final String todoAuthor;
  private final String todoTitle;
  private final String todoBody;
  private final LocalDateTime todoDate;

  private final LocalDateTime createdAt;
  private final LocalDateTime modifiedAt;
}
