package DataBaseCommunication;

import entity.Measurement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.Queue;

public class DatabaseWriter implements Runnable {
    private final EntityManager entityManager;
    private final Queue<Measurement> dataQueue;

    public DatabaseWriter(EntityManager entityManager, Queue<Measurement> dataQueue) {
        this.entityManager = entityManager;
        this.dataQueue = dataQueue;
    }

    @Override
    public void run() {
        while (true) {
            // Pobieranie danych z kolejki i zapisywanie ich do bazy danych
            Measurement measurement = dataQueue.poll();
            if (measurement != null) {
                EntityTransaction transaction = entityManager.getTransaction();
                try {
                    transaction.begin();
                    entityManager.persist(measurement);
                    transaction.commit();
                    System.out.println("Zapisano do bazy danych, wartośc pomiaru: " + measurement.getValue());
                } catch (Exception e) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}
