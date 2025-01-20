package com.project.cwmsgradle.utils;

import com.project.cwmsgradle.entity.Appointment;
import com.project.cwmsgradle.entity.Client;
import com.project.cwmsgradle.entity.User;
import com.project.cwmsgradle.entity.Vehicle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure(); // loads hibernate.cfg.xml

            // Register all entities
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Appointment.class);
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(Vehicle.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}