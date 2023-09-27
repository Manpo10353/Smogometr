package SerialPortManager;

import com.fazecast.jSerialComm.SerialPort;

public class SerialPortSender implements Runnable{

    private final SerialPort arduinoPort;
    private final int interval;


    public SerialPortSender(SerialPort arduinoPort,int interval) {
        this.arduinoPort = arduinoPort;
        this.interval = interval;
    }

    public void sendData(String data) {
        byte[] dataToSend = data.getBytes();
        arduinoPort.writeBytes(dataToSend, dataToSend.length);
    }

    public void sendSignal(){
        byte[] dataToSend = "m".getBytes();
        arduinoPort.writeBytes(dataToSend, dataToSend.length);
    }

    @Override
    public void run() {
        while (true) {
            sendSignal();
            try {
                synchronized (this) {
                    wait(interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
