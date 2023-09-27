package SerialPortManager;

import entity.Measurement;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.Queue;

public class SerialPortReceiver implements Runnable, SerialPortDataListener {
    private Queue<Measurement> dataQueue;
    public SerialPortReceiver(Queue<Measurement> dataQueue) {
        this.dataQueue = dataQueue;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] newData = serialPortEvent.getReceivedData();

        StringBuilder dataBuffer = new StringBuilder(); // Inicjalizacja bufora dla danych

        for (byte b : newData) {
            char c = (char) b; // Konwersja bajtu na znak

            if (c == ';') { //Znak końca porcji danych
                try {
                    String line = dataBuffer.toString().trim(); // Usuń białe znaki z początku i końca
                    double value = Double.parseDouble(line);

                    Measurement measurement = new Measurement(value);
                    if (dataQueue.offer(measurement)){
                        System.out.println("Odebrane dane, utworzono obiekt typu Measurement: " + value);
                    }
                    else {
                        System.out.println("Kolejka pełna, błąd"); //do zmiany,ewentualne dodanie koljenego wątku, kwestia rozmiaru kolejki
                    }

                } catch (NumberFormatException e) {

                    System.err.println("Nieprawidłowe dane: " + dataBuffer); // Ignoruj linie, które nie mogą być przekształcone na liczbę
                }


                dataBuffer.setLength(0); // Wyczyść bufor
            } else {

                dataBuffer.append(c); // Dodaj znak do bufora
            }
        }

    }

    @Override
    public void run() {
    }
}
