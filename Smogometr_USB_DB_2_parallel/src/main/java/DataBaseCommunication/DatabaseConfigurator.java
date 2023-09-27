package DataBaseCommunication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConfigurator {

        private static final EntityManagerFactory entityManagerFactory;
        private static final EntityManager entityManager;
        static {
                entityManagerFactory = Persistence.createEntityManagerFactory("default");
                entityManager = entityManagerFactory.createEntityManager();
        }

        public static EntityManager getEntityManagerFactory() {
                return entityManager;
        }

}
