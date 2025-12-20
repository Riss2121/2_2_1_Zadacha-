// Пакет приложения (корневой)
package hiber;

// Импорт конфигурационного класса Spring
import hiber.config.AppConfig;

// Импорт модели автомобиля
import hiber.model.Car;

// Импорт модели пользователя
import hiber.model.User;

// Импорт сервисного интерфейса для работы с пользователями
import hiber.service.UserService;

// Контекст Spring, основанный на Java-аннотациях (без XML)
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// Импорт исключения SQLException (хотя оно не используется — см. замечание ниже)
import java.sql.SQLException;

// Импорт List для работы с коллекциями
import java.util.List;

// Главный класс приложения — точка входа
public class MainApp {

   // Точка входа в приложение
   // ❗ Исправлено: исключение SQLException не нужно — оно не выбрасывается в коде
   public static void main(String[] args) {
      // Создаём контекст Spring на основе Java-конфигурации (AppConfig)
      // Spring загрузит все бины, настроит Hibernate, DataSource и т.д.
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

      // Получаем бин UserService из контекста Spring (реализация — UserServiceImp)
      UserService userService = context.getBean(UserService.class);

      // Создаём 4 объекта Car для привязки к пользователям
      Car car1 = new Car("model1", 1);
      Car car2 = new Car("model2", 2);
      Car car3 = new Car("model3", 3);
      Car car4 = new Car("model4", 4);

      // Добавляем 5 пользователей:
      // - первые 4 — с автомобилями,
      // - последний — без автомобиля (поле car = null)
      userService.add(new User("User1", "Lastname1", "user1@mail.ru", car1));
      userService.add(new User("User2", "Lastname2", "user2@mail.ru", car2));
      userService.add(new User("User3", "Lastname3", "user3@mail.ru", car3));
      userService.add(new User("User4", "Lastname4", "user4@mail.ru", car4));
      userService.add(new User("User5", "Lastname5", "user5@mail.ru")); // без машины

      // Получаем и выводим всех пользователей
      List<User> users = userService.listUsers();
      System.out.println("=== Список всех пользователей ===");
      for (User user : users) {
         System.out.println(user);
      }


      // Закрываем Spring-контекст — освобождает ресурсы (соединения с БД, пул и т.д.)
      context.close();
   }
}