/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package m06uf2ex1;

import Modelo.Stock;
import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Albert
 */
public class M06UF2EX1 {

    private static SessionFactory factory;
    private static Session session;
    private static final Logger logger = LogManager.getLogger(M06UF2EX1.class);

    public static void main(String[] args) {

        try {

            factory = new Configuration().configure("hibernateConfig/hibernate.cfg.xml").buildSessionFactory();

            logger.trace("Iniciamos session");
            session = factory.openSession();

            logger.trace("Iniciamos transaccion...");
            Transaction transaction = session.beginTransaction();

            logger.trace("Creamos el objeto Faker");
            Faker faker = new Faker();

            logger.trace("Añadimos 1000 objetos inventados al ArrayList y guardamos la sesion por cada objeto creado.");
            List<Stock> stocks = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                Stock stock = new Stock();
                String a =faker.idNumber().valid();
                        
                stock.setStockCode(a);
                stock.setStockName(faker.commerce().productName());
                stocks.add(stock);
            }
            for(Stock stock: stocks){
                session.persist(stock);
            }
                        
            
            logger.info("Finalitzem transacciÃ³ i desem a BBDD...");
            transaction.commit();
            
        } catch (ConstraintViolationException ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            logger.error("Violacion de la restriccion: " + ex.getMessage());

        } catch (HibernateException ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            logger.error("Excepcion de hibernate: " + ex.getMessage());

        } catch (IllegalStateException ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            logger.error("Excepcion Ilegal: " + ex.getMessage());
        } catch (Exception ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            logger.error("Excepcion: " + ex.getMessage());

        } finally {

            //Tanquem la sessiÃ³
          //  session.close();

            //Finalitzem Hibernate
            //factory.close();
        }
    }
}
