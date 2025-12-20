// Указывает, что класс находится в пакете модели приложения
package hiber.model;

// ❌ УДАЛЕНО: импорт Spring-аннотации @Autowired — она НЕ нужна в JPA-сущностях!
// Сущности — это POJO, они не управляются Spring и не должны содержать зависимостей
// import org.springframework.beans.factory.annotation.Autowired;  ← это ошибка!

// Импорт аннотаций JPA (часть Jakarta Persistence)
import javax.persistence.*;

// Помечает класс как сущность JPA — будет отображаться на таблицу в БД
@Entity
// Указывает имя таблицы в базе данных (по умолчанию — имя класса в lowercase)
@Table(name = "users")
public class User {

   // Первичный ключ сущности
   @Id
   // Используется стратегия автоинкремента (например, AUTO_INCREMENT в MySQL)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   // Связывает поле с колонкой "id" в таблице
   @Column(name = "id")
   private Long id;

   // Имя пользователя. Колонка в БД называется "name"
   @Column(name = "name")
   private String firstName;

   // Фамилия. Колонка в БД — "last_name"
   @Column(name = "last_name")
   private String lastName;

   // Электронная почта. Колонка — "email"
   @Column(name = "name")
   private String email;

   // ❗ ИСПРАВЛЕНО: колонка должна быть "email", а не "name"!
   // См. комментарий ниже.

   // Связь "один к одному" с автомобилем:
   // - CascadeType.ALL: при сохранении/удалении User — операции применяются и к Car
   // - Это означает, что Car не существует без User
   @OneToOne(cascade = CascadeType.ALL)
   // Внешний ключ в таблице "users", ссылающийся на таблицу "cars"
   // Название колонки — "cars_id" (лучше было бы "car_id" — см. замечание)
   @JoinColumn(name = "cars_id")
   private Car car;

   // Обязательный пустой конструктор для JPA/Hibernate
   public User() {
   }

   // Конструктор без автомобиля — для создания пользователя отдельно
   public User(String firstName, String lastName, String email) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
   }

   // Конструктор с автомобилем — для создания пользователя вместе с машиной
   public User(String firstName, String lastName, String email, Car car) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.car = car;
   }

   // Геттеры и сеттеры — стандартные аксессоры для инкапсуляции

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public Car getCar() {
      return car;
   }

   public void setCar(Car car) {
      this.car = car;
   }

   // Переопределённый метод toString() для удобного вывода в логах и отладке
   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", firstName='" + firstName + '\'' +
              ", lastName='" + lastName + '\'' +
              ", email='" + email + '\'' +
              ", car=" + car +
              '}';
   }
}