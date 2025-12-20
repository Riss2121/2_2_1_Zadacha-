// Объявляет пакет, в котором находится класс конфигурации
package hiber.config;

// Импорт класса Car из пакета модели
import hiber.model.Car;
// Импорт класса User из пакета модели
import hiber.model.User;

// Аннотация Spring для автоматического внедрения зависимостей
import org.springframework.beans.factory.annotation.Autowired;
// Аннотация, указывающая, что метод возвращает бин (компонент Spring)
import org.springframework.context.annotation.Bean;
// Включает автоматическое сканирование компонентов в указанном пакете
import org.springframework.context.annotation.ComponentScan;
// Указывает, что класс является конфигурационным (содержит определения бинов)
import org.springframework.context.annotation.Configuration;
// Указывает Spring загрузить свойства из файла db.properties
import org.springframework.context.annotation.PropertySource;
// Интерфейс для доступа к свойствам окружения (например, из .properties файлов)
import org.springframework.core.env.Environment;
// Реализация DataSource для простого подключения к БД через JDBC
import org.springframework.jdbc.datasource.DriverManagerDataSource;
// Менеджер транзакций для Hibernate
import org.springframework.orm.hibernate5.HibernateTransactionManager;
// Фабрика для создания SessionFactory в Hibernate 5
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
// Включает поддержку аннотаций @Transactional
import org.springframework.transaction.annotation.EnableTransactionManagement;

// Интерфейс для источника данных (DataSource — стандарт JDBC)
import javax.sql.DataSource;
// Класс для хранения пар "ключ-значение", используется для настройки Hibernate
import java.util.Properties;

// Помечает класс как конфигурационный — содержит определения Spring-бинов
@Configuration
// Указывает Spring загрузить настройки из файла db.properties из classpath
@PropertySource("classpath:db.properties")
// Включает поддержку управления транзакциями через аннотации (например, @Transactional)
@EnableTransactionManagement
// Указывает Spring автоматически сканировать компоненты (например, @Service, @Repository) в пакете "hiber" и его подпакетах
@ComponentScan(value = "hiber")
public class AppConfig {

   // Автоматически внедряет Spring-объект Environment, который позволяет читать свойства из .properties файлов
   @Autowired
   private Environment env;

   // Объявляет бин DataSource — источник подключения к базе данных
   @Bean
   public DataSource getDataSource() {
      // Создаёт экземпляр источника данных на основе DriverManager (простое JDBC-подключение)
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      // Устанавливает драйвер БД из свойства db.driver (например, com.mysql.cj.jdbc.Driver)
      dataSource.setDriverClassName(env.getProperty("db.driver"));
      // Устанавливает URL подключения к БД из свойства db.url
      dataSource.setUrl(env.getProperty("db.url"));
      // Устанавливает имя пользователя из свойства db.username
      dataSource.setUsername(env.getProperty("db.username"));
      // Устанавливает пароль из свойства db.password
      dataSource.setPassword(env.getProperty("db.password"));
      // Возвращает настроенный источник данных как Spring-бин
      return dataSource;
   }

   // Объявляет бин SessionFactory для Hibernate (через LocalSessionFactoryBean — обёртку Spring)
   @Bean
   public LocalSessionFactoryBean getSessionFactory() {
      // Создаёт фабрику сессий Hibernate
      LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
      // Привязывает источник данных (DataSource) к фабрике сессий
      factoryBean.setDataSource(getDataSource());
      // Создаёт объект Properties для настройки Hibernate
      Properties props = new Properties();
      // Включает/выключает вывод SQL в консоль (значение берётся из hibernate.show_sql в db.properties)
      props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
      // Указывает Hibernate, что делать со схемой БД при запуске (create, update, validate и т.д.)
      props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

      // Передаёт свойства Hibernate в фабрику сессий
      factoryBean.setHibernateProperties(props);
      // Регистрирует классы сущностей, которые Hibernate должен учитывать (User и Car)
      factoryBean.setAnnotatedClasses(User.class, Car.class);
      // Возвращает настроенную фабрику сессий как Spring-бин
      return factoryBean;
   }

   // Объявляет бин менеджера транзакций для работы с Hibernate
   @Bean
   public HibernateTransactionManager getTransactionManager() {
      // Создаёт экземпляр менеджера транзакций
      HibernateTransactionManager transactionManager = new HibernateTransactionManager();
      // Устанавливает SessionFactory (полученную из фабрики) для управления транзакциями
      // getObject() вызывается, потому что LocalSessionFactoryBean — это FactoryBean, и реальный объект — SessionFactory
      transactionManager.setSessionFactory(getSessionFactory().getObject());
      // Возвращает настроенный менеджер транзакций как Spring-бин
      return transactionManager;
   }
}