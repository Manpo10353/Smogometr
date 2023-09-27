import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import entity.SmogometrDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.TimerTask;

public class TimerScheduleHandler extends TimerTask implements SerialPortDataListener {
    private final long timeStart;
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public TimerScheduleHandler(long timeStart) {
        this.timeStart = timeStart;
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void run() {
        // System.out.println("Time elapsed: " + (System.currentTimeMillis() - this.timeStart) + "milliseconds");
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] newData = event.getReceivedData();
        StringBuilder dataBuffer = new StringBuilder(); // Inicjalizacja bufora dla danych

        for (byte b : newData) {
            char c = (char) b; // Konwersja bajtu na znak

            if (c == ';') {
                try {
                    String line = dataBuffer.toString().trim(); // Usuń białe znaki z początku i końca
                    double value = Double.parseDouble(line);
                    System.out.println("Received value: " + value);

                    EntityTransaction transaction = entityManager.getTransaction();

                    try {
                        transaction.begin();
                        SmogometrDb measurement = new SmogometrDb();
                        measurement.setValue(value);
                        entityManager.persist(measurement);
                        transaction.commit();
                    } catch (Exception e) {
                        if (transaction.isActive()) {
                            transaction.rollback();
                        }
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    // Ignoruj linie, które nie mogą być przekształcone na liczbę
                    System.err.println("Ignoring invalid data: " + dataBuffer.toString());
                }

                // Wyczyść bufor
                dataBuffer.setLength(0);
            } else {
                // Dodaj znak do bufora
                dataBuffer.append(c);
            }
        }
    }
}