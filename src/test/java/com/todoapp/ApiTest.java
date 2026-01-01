package com.todoapp;

import com.todoapp.dto.TodoResponse;
import jakarta.annotation.PostConstruct;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class ApiTest {

  @LocalServerPort private int port;

  @Autowired private RestTestClient testClient;

  private ApiTestHelper helper;
  private org.approvaltests.core.Options opt;

  @PostConstruct
  void setup() {
    helper = new ApiTestHelper(testClient, port);
    helper.setFieldsToCheckIfDate("createdAt", "modifiedAt");
    helper.addFieldsToReplace("1111-02-03T04:05:06.777777", "createdAt", "modifiedAt", "todoId");

    // spring 기본 에러 timestamp 제거
    helper.addFieldsToReplace("1111-02-03T04:05:06.666Z", "timestamp");
    helper.addFieldsToReplace("{todoId}", "todoId");

    // approvaltests는 option이 없으면 diff툴을 띄우도록 설정이 되어있습니다.
    // 누구 아이디어인지 모르겠지만 짜증나므로 off
    opt = new Options();
    opt = opt.withReporter(new org.approvaltests.reporters.ReportNothing());
  }

  // 왜 @Transactional이 아니고 @DirtiesContext를 쓰는지는 아래 링크 참고
  // https://stackoverflow.com/questions/64591281/transactional-annotation-in-spring-test
  // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/annotation/DirtiesContext.html
  @Test
  @DirtiesContext
  void postTest() {
    helper.begin();

    String todo1 =
        """
        {
          "password" : "69420",

          "todoAuthor" : "momo",
          "todoTitle" : "산책하러가기",
          "todoBody" : "밖에 산책 하러 가기",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    helper.sendRequest("/api/todos", HttpMethod.POST, todo1);
    Approvals.verify(helper.end(), opt);
  }

  @Test
  @DirtiesContext
  void postAndList() {
    helper.begin();

    String todo1 =
        """
        {
          "password" : "69420",

          "todoAuthor" : "momo",
          "todoTitle" : "햄버거 사기",
          "todoBody" : "맥도날드 가서 햄버거 사기",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    String todo2 =
        """
        {
          "password" : "69420",

          "todoAuthor" : "kiki",
          "todoTitle" : "momo의 햄버거 뺏기",
          "todoBody" : "momo의 햄버거 뺏어서 내가 먹기",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    helper.sendRequest("/api/todos", HttpMethod.POST, todo1);
    helper.sendRequest("/api/todos", HttpMethod.POST, todo2);

    helper.sendRequest("/api/todos", HttpMethod.GET);

    Approvals.verify(helper.end(), opt);
  }

  @Test
  @DirtiesContext
  void postAndListByAuthor() {
    helper.begin();

    String todo1 =
        """
        {
          "password" : "69420",

          "todoAuthor" : "momo",
          "todoTitle" : "햄버거 사기",
          "todoBody" : "맥도날드 가서 햄버거 사기",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    String todo2 =
        """
        {
          "password" : "69420",

          "todoAuthor" : "kiki",
          "todoTitle" : "momo의 햄버거 뺏기",
          "todoBody" : "momo의 햄버거 뺏어서 내가 먹기",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    helper.sendRequest("/api/todos", HttpMethod.POST, todo1);
    helper.sendRequest("/api/todos", HttpMethod.POST, todo2);

    helper.sendRequest("/api/todos?author=kiki", HttpMethod.GET);

    Approvals.verify(helper.end(), opt);
  }

  @Test
  @DirtiesContext
  void wrongPassword() {
    helper.begin();

    String todo =
        """
        {
          "password" : "",

          "todoAuthor" : "",
          "todoTitle" : "",
          "todoBody" : "",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    helper.sendRequest("/api/todos", HttpMethod.POST, todo);

    Approvals.verify(helper.end(), opt);
  }

  @Test
  @DirtiesContext
  void getById() {
    helper.begin();

    String todo1 =
        """
        {
          "password" : "69420",
          "todoAuthor" : "momo", "todoTitle" : "", "todoBody" : "",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;
    String todo2 =
        """
        {
          "password" : "69420",
          "todoAuthor" : "kiki", "todoTitle" : "", "todoBody" : "",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    String todo1Res = helper.sendRequest("/api/todos", HttpMethod.POST, todo1);
    helper.sendRequest("/api/todos", HttpMethod.POST, todo2);

    TodoResponse resObj = new ObjectMapper().readValue(todo1Res, TodoResponse.class);
    // todo1의 id로 GET을 요청
    helper.sendRequest(String.format("/api/todos/%s", resObj.getTodoId()), HttpMethod.GET);

    // 존재하지 않는 id 요청
    helper.sendRequest("/api/todos/123456789", HttpMethod.GET);

    Approvals.verify(helper.end(), opt);
  }

  @Test
  @DirtiesContext
  void putTest() {
    helper.begin();

    String todo =
        """
        {
          "password" : "69420",
          "todoAuthor" : "bad", "todoTitle" : "bad", "todoBody" : "good",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    String newTodo =
        """
        {
          "password" : "69420",
          "todoAuthor" : "good", "todoTitle" : "good", "todoBody" : "bad",
          "todoDate" : "2000-01-22T00:00:00.0000000"
        }
        """;

    String todoRes = helper.sendRequest("/api/todos", HttpMethod.POST, todo);

    TodoResponse resObj = new ObjectMapper().readValue(todoRes, TodoResponse.class);
    helper.sendRequest(String.format("/api/todos/%s", resObj.getTodoId()), HttpMethod.PUT, newTodo);

    Approvals.verify(helper.end(), opt);
  }

  @Test
  @DirtiesContext
  void deleteTest() {
    helper.begin();

    String todo1 =
        """
          {
            "password" : "69420",
            "todoAuthor" : "kiki", "todoTitle" : "고백", "todoBody" : "momo한테 고백하기",
            "todoDate" : "2000-01-22T00:00:00.0000000"
          }
        """;
    String todo2 =
        """
          {
            "password" : "69420",
            "todoAuthor" : "momo", "todoTitle" : "낮잠", "todoBody" : "낮잠 자기",
            "todoDate" : "2000-01-22T00:00:00.0000000"
          }
        """;

    String todo1Res = helper.sendRequest("/api/todos", HttpMethod.POST, todo1);
    helper.sendRequest("/api/todos", HttpMethod.POST, todo2);

    helper.sendRequest("/api/todos", HttpMethod.GET);

    // 삭제 시도

    // id 가져오기
    TodoResponse resObj = new ObjectMapper().readValue(todo1Res, TodoResponse.class);
    // todo1의 id로 DELETE를 요청
    helper.sendRequest(String.format("/api/todos/%s", resObj.getTodoId()), HttpMethod.DELETE);

    helper.sendRequest("/api/todos", HttpMethod.GET);

    // 존재하지 않는 id 삭제 요청
    helper.sendRequest("/api/todos/123456789", HttpMethod.DELETE);

    Approvals.verify(helper.end(), opt);
  }

  @Test
  void testCheckIfFieldsAreDates1() {
    Assertions.assertThrows(
        ApiTestHelper.FieldNotDateException.class,
        () -> {
          String json =
              """
                { "foo" : "bar" }
              """;
          ApiTestHelper.checkIfFieldsAreDates(json, new String[] {"foo"});
        });
  }

  @Test
  void testCheckIfFieldsAreDates2() {
    Assertions.assertThrows(
        ApiTestHelper.FieldNotDateException.class,
        () -> {
          String json =
              """
              {
                "foo" : {
                  "bar" : {
                    "fakeDate" : 1234
                  }
                }
              }
              """;
          ApiTestHelper.checkIfFieldsAreDates(json, new String[] {"fakeDate"});
        });
  }
}
