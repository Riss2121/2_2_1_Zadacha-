// Указывает, что класс находится в пакете модели приложения
package hiber.model;

// Импорт аннотаций JPA (ныне часть Jakarta Persistence, но в Spring Boot 2.x и Hibernate 5 всё ещё javax.persistence)
import javax.persistence.*;

// Помечает класс как сущность JPA — он будет отображаться на таблицу в базе данных
@Entity
// Указывает имя таблицы в БД, с которой связана сущность (по умолчанию — имя класса в lowercase)
@Table(name = "cars")
public class Car {

    // Первичный ключ сущности
    @Id
    // Указывает стратегию генерации значения ID: автоинкремент (обычно используется с IDENTITY в MySQL, PostgreSQL и др.)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Связывает поле с колонкой "id" в таблице "cars"
    @Column(name = "id")
    private Long id;

    // Отображает поле на колонку "model" в таблице
    // По умолчанию тип VARCHAR, nullable = true. Можно уточнить при необходимости.
    @Column(name = "model")
    private String model;

    // Отображает поле на колонку "series" (целое число)
    // Например, может означать год выпуска или серию модели
    @Column(name = "series")
    private int series;

    // Пустой конструктор ОБЯЗАТЕЛЕН для JPA/Hibernate (используется при восстановлении объекта из БД)
    public Car() {
    }

    // Конструктор для удобного создания объекта с параметрами
    public Car(String model, int series) {
        this.model = model;
        this.series = series;
    }

    // Геттер для id — возвращает уникальный идентификатор автомобиля
    public Long getId() {
        return id;
    }

    // Сеттер для id — обычно не используется вручную при GenerationType.IDENTITY,
    // но может понадобиться при тестировании или миграции
    public void setId(Long id) {
        this.id = id;
    }

    // Геттер для модели автомобиля
    public String getModel() {
        return model;
    }

    // Сеттер для модели автомобиля
    public void setModel(String model) {
        this.model = model;
    }

    // Геттер для серии (например, 2020, 2023 и т.д.)
    public int getSeries() {
        return series;
    }

    // Сеттер для серии
    public void setSeries(int series) {
        this.series = series;
    }

    // Переопределённый метод toString() для удобного вывода объекта в логах или отладке
    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", series=" + series +
                '}';
    }
}