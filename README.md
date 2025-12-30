# Sparta TODO-app Project

## Formatter

Formatter는 [google-java-format](https://github.com/google/google-java-format)을 사용합니다. 저는 간단한 batch파일을 이용해서 포맷합니다.

(fmt.bat 참고)

# API 명세서

## /api/todos

### `GET` 모든 todo 반환

### RESPONSE
```
[
    {
        "todoId" : "1",
        "todoAuthor" : "momo",
        "todoTitle" : "햄버거 사기",
        "todoBody" : "맥도날드 가서 햄버거 사기",
        "todoDate" : "2000-01-22T00:00:00.0000000",

        "createdAt" : "2000-01-22T00:00:00.0000000", 
        "modifiedAt" : "2000-01-22T00:00:00.0000000"
    },
    {
        "todoAuthor" : "kiki",
        "todoId" : "2",
        "todoTitle" : "momo 햄버거 뺏기",
        "todoBody" : "momo의 맛있는 햄버거 뺏어먹기",
        "todoDate" : "2000-01-22T00:00:00.0000000",

        "createdAt" : "2000-01-22T00:00:00.0000000", 
        "modifiedAt" : "2000-01-22T00:00:00.0000000"
    }
]
```

### `POST` todo 추가

### REQUEST
```
{
    "password" : "69420",

    "todoAuthor" : "momo",
    "todoTitle" : "햄버거 사기",
    "todoBody" : "맥도날드 가서 햄버거 사기",
    "todoDate" : "2000-01-22T00:00:00.0000000"
}
```

### RESPONSE
```
{
    "todoId" : "1",
    "todoAuthor" : "momo",
    "todoTitle" : "햄버거 사기",
    "todoBody" : "맥도날드 가서 햄버거 사기",
    "todoDate" : "2000-01-22T00:00:00.0000000",

    "createdAt" : "2000-01-22T00:00:00.0000000", 
    "modifiedAt" : "2000-01-22T00:00:00.0000000"
}
```

## /api/todos?todoAuthor={todoAuthor-name}

### `GET` 해당 작성자의 todo 반환

### RESPONSE
```
[
    {
        "todoId" : "1",
        "todoAuthor" : "momo",
        "todoTitle" : "햄버거 사기",
        "todoBody" : "맥도날드 가서 햄버거 사기",
        "todoDate" : "2000-01-22T00:00:00.0000000",

        "createdAt" : "2000-01-22T00:00:00.0000000", 
        "modifiedAt" : "2000-01-22T00:00:00.0000000"
    },
]
```

## /api/todos/{todo-id}

### `GET` 해당 todo-id의 todo 반환

### RESPONSE
```
{
    "todoId" : "1",
    "todoAuthor" : "momo",
    "todoTitle" : "햄버거 사기",
    "todoBody" : "맥도날드 가서 햄버거 사기",
    "todoDate" : "2000-01-22T00:00:00.0000000",

    "createdAt" : "2000-01-22T00:00:00.0000000", 
    "modifiedAt" : "2000-01-22T00:00:00.0000000"
},
```

### `PUT` 해당 todo-id의 todo 수정

### REQUEST
```
{
    "password" : "69420",

    "todoAuthor" : "momo",
    "todoTitle" : "햄버거 사기",
},
```

### RESPONSE
```
{
    "todoId" : "1",
    "todoAuthor" : "momo",
    "todoTitle" : "햄버거 사기",
    "todoBody" : "맥도날드 가서 햄버거 사기",
    "todoDate" : "2000-01-22T00:00:00.0000000",

    "createdAt" : "2000-01-22T00:00:00.0000000", 
    "modifiedAt" : "2000-01-22T00:00:00.0000000"
},
```

### `DELETE` 해당 todo-id의 todo 삭제

## git convention

- FEAT:     feature 추가
- FIX:      버그 수정
- REFACTOR: refactoring
- MISC:     기타

