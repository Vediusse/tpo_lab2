# Function System Integration Testing Lab

Maven-проект на Java 17 для лабораторной работы по интеграционному тестированию системы функций. В проекте есть production-код, табличные заглушки, экспорт значений в CSV, unit tests, integration tests и покрытие JaCoCo.

## Формула системы функций

Для `x <= 0`:

```text
((((tan(x)^2 + tan(x) + cos(x)) * sec(x)) / (csc(x) * tan(x) - tan(x))) - cos(x))
```

Для `x > 0`:

```text
((((log_2(x)^3)^2 + log_2(x))^2) - (log_5(x) + (ln(x) * ((log_3(x)^2) + (log_2(x)^3)))))
```

## Структура пакетов

- `model` - общие модели данных
- `approximation` - численные базовые приближения
- `function.trig` - тригонометрические функции
- `function.log` - логарифмические функции
- `function.system` - система функций
- `stub` - табличные заглушки и чтение из CSV
- `io` - сэмплирование и экспорт в CSV
- `util` - служебные классы
- `factory` - фабрика production-конфигурации

## Стратегия интеграции

Выбрана поэтапная интеграция сверху вниз с подменой еще не подключенных модулей табличными заглушками:

1. Проверка тригонометрической ветки системы с заглушками логарифмов.
2. Проверка логарифмической ветки системы с заглушками тригонометрии.
3. Проверка полной системы на реальных реализациях.
4. Проверка интеграции экспорта значений в CSV.

Такой подход удобно защищать на лабораторной работе: видно, как система постепенно собирается из модулей.

## Запуск тестов

Unit tests:

```bash
mvn test
```

Integration tests:

```bash
mvn failsafe:integration-test failsafe:verify
```

Все тесты вместе:

```bash
mvn verify
```

## Coverage report

```bash
mvn clean verify
```

Отчет будет в:

```text
target/site/jacoco/index.html
```

## Генерация CSV

Пример запуска:

```bash
mvn -q exec:java -Dexec.mainClass="com.example.lab2.Application"
```

Либо после сборки:

```bash
java -cp target/lab2-function-system-1.0-SNAPSHOT.jar com.example.lab2.Application
```

CSV-файлы создаются в `target/generated-csv/` в формате `x,result`.
