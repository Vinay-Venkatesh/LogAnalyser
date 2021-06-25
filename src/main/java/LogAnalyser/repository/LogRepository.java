package LogAnalyser.repository;

import LogAnalyser.model.Logs;
import org.springframework.stereotype.Repository;
import javax.persistence.*;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class LogRepository {
    @PersistenceUnit(unitName = "logs")
    private EntityManagerFactory emf;

    public void addLogs(Logs logData) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            //persist() method changes the state of the model object from transient state to persistence state
            em.persist(logData);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    public int getURICount() {
        try {
            EntityManager em = emf.createEntityManager();
            // Query to fetch the uris from database
            Query query = em.createQuery("select uri from Logs l");
            return query.getResultList().size();
        } catch (NoResultException nre) {
            return 0;
        }
    }
}