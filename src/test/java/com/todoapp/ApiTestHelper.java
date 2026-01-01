package com.todoapp;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.ExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

class ApiTestHelper {

  private final int port;
  private final RestTestClient client;
  private final StringBuilder builder = new StringBuilder();

  private final List<Pair<String[], String>> fieldsToReplace = new ArrayList<>();
  private String[] fieldsToCheckIfDate = new String[] {};

  public ApiTestHelper(RestTestClient client, int port) {
    this.client = client;
    this.port = port;
  }

  public void addFieldsToReplace(String toReplaceWith, String... fields) {
    fieldsToReplace.add(Pair.of(fields, toReplaceWith));
  }

  public void setFieldsToCheckIfDate(String... fields) {
    fieldsToCheckIfDate = fields;
  }

  public void begin() {
    builder.setLength(0);
  }

  private String sendRequestImpl(
      String uri,
      String replacedUri,
      boolean replaceUri,
      HttpMethod method,
      String body,
      boolean sendBody) {
    if (replaceUri) {
      builder.append(String.format("%s %s\n\n", method.name(), replacedUri));
    } else {
      builder.append(String.format("%s %s\n\n", method.name(), uri));
    }

    if (sendBody) {
      builder.append("== REQUEST ==\n\n");
      builder.append(body);
      builder.append("\n");
    }

    var req = client.method(method).uri(uri);

    ExchangeResult res;

    if (sendBody) {
      res = req.contentType(MediaType.APPLICATION_JSON).body(body).exchange().returnResult();
    } else {
      res = req.exchange().returnResult();
    }

    String resBody = new String(res.getResponseBodyContent(), StandardCharsets.UTF_8);

    try {
      checkIfFieldsAreDates(resBody, fieldsToCheckIfDate);
    } catch (FieldNotDateException e) {
      throw new FieldNotDateException(
          String.format("%s %s : %s", method.name(), uri, e.getMessage()), e.getCause());
    }

    builder.append("== RESPONSE ==\n\n");
    builder.append(String.format("status code : %s\n\n", res.getStatus()));

    // field값 교체
    String replaced = resBody;
    for (final var pair : fieldsToReplace) {
      replaced = replaceJsonFields(replaced, pair.getFirst(), pair.getSecond());
    }

    // pretty print
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(replaced);
    builder.append(node.toPrettyString().replace("\r\n", "\n"));

    builder.append("\n\n");

    return resBody;
  }

  public String sendRequest(String uri, HttpMethod method) {
    return sendRequestImpl(uri, "", false, method, "", false);
  }

  public String sendRequest(String uri, HttpMethod method, String body) {
    return sendRequestImpl(uri, "", false, method, body, true);
  }

  public String sendRequest(String uri, String replacedUri, HttpMethod method) {
    return sendRequestImpl(uri, replacedUri, true, method, "", false);
  }

  public String sendRequest(String uri, String replacedUri, HttpMethod method, String body) {
    return sendRequestImpl(uri, replacedUri, true, method, body, true);
  }

  public String end() {
    return builder.toString();
  }

  public static void traverseNode(JsonNode node, Consumer<JsonNode> cb) {
    cb.accept(node);

    if (node.isObject() || node.isArray()) {
      node.forEach((child) -> traverseNode(child, cb));
    }
  }

  public static String replaceJsonFields(String jsonString, String[] fields, String with) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(jsonString);

    traverseNode(
        node,
        (n) -> {
          if (n.isObject()) {
            ObjectNode obj = (ObjectNode) n;
            for (String field : fields) {
              if (obj.has(field)) {
                obj.put(field, with);
              }
            }
          }
        });
    return node.toString();
  }

  public static String removeJsonFields(String jsonString, String[] fields) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(jsonString);

    traverseNode(
        node,
        (n) -> {
          if (n.isObject()) {
            ObjectNode obj = (ObjectNode) n;
            for (String field : fields) {
              obj.remove(field);
            }
          }
        });
    return node.toString();
  }

  public static class FieldNotDateException extends RuntimeException {
    public FieldNotDateException(String msg, Throwable cause) {
      super(msg, cause);
    }

    public FieldNotDateException(String msg) {
      super(msg);
    }
  }

  public static void checkIfFieldsAreDates(String jsonString, String[] fields)
      throws FieldNotDateException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(jsonString);

    traverseNode(
        node,
        (n) -> {
          if (!n.isObject()) {
            return;
          }

          for (String field : fields) {
            if (n.has(field)) {
              JsonNode fieldNode = n.get(field);

              // 실제로 문자열인지 확인
              if (!fieldNode.isString()) {
                throw new FieldNotDateException(
                    String.format("%s is not a string it's a %s", field, fieldNode.getNodeType()));
              }

              String maybeDateString = fieldNode.asString();

              // 시간으로 해석 될수 있는지 확인
              try {
                LocalDateTime.parse(maybeDateString);
              } catch (DateTimeParseException e) {
                throw new FieldNotDateException(
                    String.format("%s : %s is not a date", field, maybeDateString), e);
              }
            }
          }
        });
  }
}
