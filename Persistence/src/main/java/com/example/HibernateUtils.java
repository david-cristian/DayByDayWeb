package com.example;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties dbProperties = new Properties();
                try (InputStream input = HibernateUtils.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (input != null) {
                        dbProperties.load(input);
                        configuration.addProperties(dbProperties);
                    }
                }

                configuration.configure("hibernate.cfg.xml");

                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Group.class);
                configuration.addAnnotatedClass(Task.class);
                configuration.addAnnotatedClass(Objective.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());

                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.err.println("Eroare la initializarea Hibernate SessionFactory: " + e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}