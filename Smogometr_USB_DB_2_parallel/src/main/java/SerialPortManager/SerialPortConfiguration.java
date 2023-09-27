package SerialPortManager;
import com.fazecast.jSerialComm.SerialPort;

public class SerialPortConfiguration {
    public static SerialPort serialPortConfiguration(String portDescription, int baudRade) {

    //Ustawienie wstępnych parametrów
    System.out.println("Konfiguracja połączenia.");
    SerialPort arduinoPort = SerialPort.getCommPort(portDescription);
    arduinoPort.setComPortParameters(baudRade,Byte.SIZE,SerialPort.ONE_STOP_BIT,SerialPort.NO_PARITY);
    arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING,0,0);
    System.out.println("Skonfigurowano.");

    //Sprawdzenie podłączenia arduino
    boolean hasOpened = arduinoPort.openPort();
    if(!hasOpened) {
        throw new IllegalStateException("Failed to open serial port\n");
    }

    //Przygotowanie wątku, kóry zamknie port w momencie zamykania programu
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("Zamykanie portu: " + portDescription);
        arduinoPort.closePort();
    }));

    return arduinoPort;
    }
}
