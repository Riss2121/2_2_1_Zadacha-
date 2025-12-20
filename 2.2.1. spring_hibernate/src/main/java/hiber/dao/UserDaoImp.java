// Указывает, что класс находится в пакете hiber.dao — слой доступа к данным
package hiber.dao;

// Импорт модели User — сущности, с которой работает репозиторий
import hiber.model.User;

// SessionFactory — фабрика сессий Hibernate, предоставляет доступ к текущей сессии
import org.hibernate.SessionFactory;

// Автоматическое внедрение зависимостей Spring
import org.springframework.beans.factory.annotation.Autowired;

// Помечает класс как компонент уровня данных (DAO/Repository), Spring автоматически регистрирует его как бин
import org.springframework.stereotype.Repository;

// TypedQuery — типизированный запрос JPA/Hibernate, обеспечивает безопасность на этапе компиляции
import javax.persistence.TypedQuery;

// Импорт интерфейса List для работы с коллекциями
import java.util.List;

// Аннотация@Repository указывает Spring, что это компонент доступа к данным.
// Также обеспечивает автоматическую обработку исключений (например, преобразует исключения Hibernate в Spring DataAccessException)
@Repository
// Реализация интерфейса UserDao — предоставляет конкретную логику выполнения операций с БД
public class UserDaoImp implements UserDao {

   // Внедрение SessionFactory, настроенной в AppConfig.
   // Через неё получают сессию Hibernate для выполнения операций CRUD.
   @Autowired
   private SessionFactory sessionFactory;

   // Реализация метода добавления пользователя в базу данных
   @Override
   public void add(User user) {
      // Получает текущую сессию Hibernate (обычно привязанную к транзакции)
      // и сохраняет переданный объект user. Hibernate автоматически определит INSERT или UPDATE.
      sessionFactory.getCurrentSession().save(user);
   }

   // Реализация метода получения списка всех пользователей
   @Override
   public List<User> listUsers() {
      // Создаём типизированный HQL-запрос: "from User" означает "выбрать все сущности User"
      // Второй параметр User.class делает запрос типобезопасным — возвращается именно List<User>
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User", User.class);

      // Выполняем запрос и возвращаем список всех пользователей из БД
      return query.getResultList();
   }

   // Реализация метода поиска пользователей по модели и серии их автомобиля
   @Override
   public List<User> userBySeriesAndModel(String model, int series) {
      // HQL-запрос с параметрами:
      // - предполагается, что у User есть поле 'car' с типом Car (настроена связь @OneToOne или @ManyToOne)
      // - car.model и car.series — свойства связанного объекта
      // Используем именованные параметры (:paramModel, :paramSeries) для защиты от SQL-инъекций
      // ВАЖНО: убран лишний пробел после двоеточия (было ": paramModel" — ошибка!)
      String hql = "FROM User u WHERE u.car.model = :paramModel AND u.car.series = :paramSeries";

      // Создаём типизированный запрос для безопасного возврата List<User>
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);

      // Привязываем значения к именованным параметрам
      query.setParameter("paramModel", model);
      query.setParameter("paramSeries", series);

      // Выполняем запрос и возвращаем список найденных пользователей
      return query.getResultList();
   }
}