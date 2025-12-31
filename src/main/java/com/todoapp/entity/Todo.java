package com.todoapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "todos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String body;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime date;

  public Todo(String author, String title, String body, LocalDateTime date) {
    this.author = author;
    this.title = title;
    this.body = body;
    this.date = date;
  }
}
