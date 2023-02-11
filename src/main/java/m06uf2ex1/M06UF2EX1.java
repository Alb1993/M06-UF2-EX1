/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package m06uf2ex1;

import Modelo.Stock;
import com.github.javafaker.Faker;
import jakarta.transaction.Transaction;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        
        
            try{
            
            factory = new Configuration().configure("hibernateConfig/hibernate.cfg.xml").buildSessionFactory();
            
            logger.trace("Iniciamos session");
            session = factory.openSession();
            
            logger.trace("Iniciamos transaccion...");
            session.beginTransaction().begin();
            
            logger.trace("Creamos el objeto Faker");
            
            Faker faker = new Faker();

            logger.trace("Añadimos 1000 objetos inventados al ArrayList y guardamos la sesion por cada objeto creado.");
            
            for(int i=0; i<1000; i++){
                Stock stock = new Stock(faker.idNumber().valid(),faker.commerce().productName());
                session.save(stock);
            }
            
            logger.info("Finalitzem transacciÃ³ i desem a BBDD...");
            
            session.getTransaction().commit();
            
        
        } catch (ConstraintViolationException ex){
             if (session.getTransaction() !=null) 
                 session.getTransaction().rollback();
             logger.error("ViolaciÃ³ de la restricciÃ³: " + ex.getMessage());
            
        } catch (HibernateException ex) {
            if (session.getTransaction()!=null) 
                session.getTransaction().rollback();
            
            logger.error("ExcepciÃ³ d'hibernate: " + ex.getMessage());
      } catch (Exception ex){
             if (session.getTransaction() !=null) 
                 session.getTransaction().rollback();
             logger.error("ExcepciÃ³: " + ex.getMessage());
            
        }
        finally {
            
         //Tanquem la sessiÃ³
         session.close();
         
         //Finalitzem Hibernate
         factory.close();
      }
    }
}
