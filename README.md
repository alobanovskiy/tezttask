# REST Assured + Allure автотесты (Maven)

## Что внутри
- **RestAssured**: HTTP-клиент для API тестов
- **JUnit 5**: тестовый раннер
- **Allure**: аннотации + прикрепление request/response
- **AssertJ**: “красивые” ассерты
- **Logback/SLF4J**: логгирование в консоль

## Как запустить (Windows)
1) Установите **JDK** и убедитесь, что команда `java` доступна в терминале (PATH).
2) При необходимости переопредели через env или `-D...`:

- `BASE_URL` (по умолчанию `https://testslotegrator.com`)
- `EMAIL` / `PASSWORD` (по умолчанию — `LoginConfig.java`)

2) Запуск тестов:

```bash
.\mvnw.cmd test
```

Если `mvnw.cmd` пишет `Java not found`, добавь `C:\Program Files\Java\jdk-21...\bin` в `PATH` (или задай `JAVA_HOME`).

3) Сгенерировать Allure отчёт:

```bash
.\mvnw.cmd allure:report
```

Отчёт будет в `target/site/allure-maven-plugin/`.

## Пример теста
См.:
- `src/test/java/org/example/test/PlayersSmokeTest.java` (ваш API)
- `src/test/java/org/example/test/ReqresUserTest.java` (пример на публичном API)

