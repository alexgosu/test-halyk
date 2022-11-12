# Тест

### Инструкция по запуску через docker

1. Собрать проект
```shell
./gradlew clean build
```

2. Запустить
```shell
docker compose up --build
```

### Запуск в ручном режиме

1. Создать БД в PostgreSQL и указать в конфигурации `spring.datasource`
2. Сконфигурировать коннект Hazelcast в секции `hazelcast`
3. Собрать проект
```shell
./gradlew clean build
```
4. Запустить
```shell
java -jar ./build/libs/halyk-finance-1.0.0.jar
```

### Проверка

Вызывать нужный метод API через swagger в браузере
```
http://localhost:8080/swagger-ui/index.html
```