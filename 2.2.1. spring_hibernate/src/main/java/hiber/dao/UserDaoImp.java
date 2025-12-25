package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("FROM User", User.class);
        return query.getResultList();
    }

    @Override
    public User getUserByCar(String model, int series) {
        String hql = "FROM User u WHERE u.car.model = :paramModel AND u.car.series = :paramSeries";
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);
        query.setParameter("paramModel", model);
        query.setParameter("paramSeries", series);
        return query.setMaxResults(1).getSingleResult();
    }

    @Override
    public User getUserCar(Car car) {
        String hql = "from User user where user.car.model = :model and user.car.series = :series";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("model", car.getModel()).setParameter("series", car.getSeries());
        return (User) query.setMaxResults(1).getSingleResult();
    }
}