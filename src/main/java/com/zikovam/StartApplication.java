package com.zikovam;

import com.zikovam.entity.Account;
import com.zikovam.entity.User;
import com.zikovam.utils.HibernateSessionFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class.
 */
@Slf4j
public class StartApplication {
    // Base URI the Grizzly HTTP server will listen on
    private static final String BASE_URI = "http://localhost:8080/api";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    private static HttpServer startServer () {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package
        final ResourceConfig rc = new ResourceConfig().packages("com.zikovam.services.");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    private static void fillingDatabase () {
        User user1 = new User("Elon Musk");
        User user2 = new User("Bear Grills");

        Account account1 = new Account(user1,2000);
        Account account2 = new Account(user1, 5000);
        List<Account> accountList1 = new ArrayList<>();
        accountList1.add(account1);
        accountList1.add(account2);
        user1.setAccounts(accountList1);

        Account account3 = new Account(user2, 9000);
        List<Account> accountList2 = new ArrayList<>();
        accountList2.add(account3);
        user2.setAccounts(accountList2);

        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction tx;

        try {
            //Get Session
            session = HibernateSessionFactoryUtil.getSession();
            System.out.println("Session created");
            //start transaction
            tx = session.beginTransaction();

            //Save the Model objects
            session.save(user1);
            session.save(user2);
            session.save(account1);
            session.save(account2);
            session.save(account3);

            //Commit transaction
            tx.commit();
        } catch (Exception e) {
            System.out.println("Exception occured. " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateSessionFactoryUtil.closeSession();
        }
    }

    /**
     * Main method.
     *
     * @param args
     *
     * @throws IOException
     */
    public static void main (String[] args) throws IOException {
        final HttpServer server = startServer();
        fillingDatabase();

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HibernateSessionFactoryUtil.closeSession();
        HibernateSessionFactoryUtil.closeSessionFactory();
        server.shutdown();
    }
}