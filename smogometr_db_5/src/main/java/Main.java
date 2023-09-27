import com.fazecast.jSerialComm.SerialPort;
import entity.SmogometrDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();


        SerialPort sp = SerialPort.getCommPort("COM3");
        sp.setComPortParameters(115200, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        boolean hasOpened = sp.openPort();
        if (!hasOpened) {
            throw new IllegalStateException("Failed to open serial port\n");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sp.closePort();
        }));

        Timer timer = new Timer();
        TimerScheduleHandler timedSchedule = new TimerScheduleHandler(timeStart);

        sp.addDataListener(timedSchedule);

        // System.out.println("Listen: " + timedSchedule.getListeningEvents());
        timer.schedule(timedSchedule, 1000, 5000);


    }
}
