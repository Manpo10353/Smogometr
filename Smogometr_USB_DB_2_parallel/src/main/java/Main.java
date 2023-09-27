import com.fazecast.jSerialComm.SerialPort;
import SerialPortManager.*;
import DataBaseCommunication.*;
import entity.Measurement;
import jakarta.persistence.EntityManager;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {

        Queue<Measurement> dataQueue = new ConcurrentLinkedQueue<>(); //kolejka dla podejścia wielowątkowego

        SerialPort arduinoPort = SerialPortConfiguration.serialPortConfiguration("COM3",115200); //konstruktor konfiguracji portu szeregowego

        SerialPortReceiver communicator = new SerialPortReceiver(dataQueue); //utworzenie obiektu klasy komuniaktora i przypisanie go do portu
        arduinoPort.addDataListener(communicator);
        Thread communicationThread = new Thread(communicator);  //uruchomienie wątku
        communicationThread.start();

        EntityManager entityManager = DatabaseConfigurator.getEntityManagerFactory(); //utworzenie entitymanagera do komunikacji z bazą danych
        Thread dataWriterThread = new Thread(new DatabaseWriter(entityManager, dataQueue)); //uruchomienie wątku
        dataWriterThread.start();

        SerialPortSender dataSender = new SerialPortSender(arduinoPort,5000); // wątek wysyłający sygnał wykonania pomiaru
        Thread senderThread = new Thread(dataSender);
        senderThread.start(); // Uruchom wątek




    }
}
