#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SMOGOMETR_SERVICE_UUID   "4fafc201-1fb5-459e-8fcc-c5c9c331914b"

//Sensors value UUID
#define PPM2_5_CHARACTERISTIC_UUID          "b816c7bc-b642-4921-bb70-577c46142eea"
#define PPM10_CHARACTERISTIC_UUID           "96ae0f7a-ba12-4812-94f9-6cbc2aec5fb3"
#define NOX_CHARACTERISTIC_UUID             "f948b965-1b14-47f5-bee3-c9c1db3110db"
#define SOX_CHARACTERISTIC_UUID             "6b3dcb36-764d-485a-9b7e-8355428ba76f"

#define SMOGOMETR_INITIAL_SERVICE_UUID      "408b99f0-7fdd-404a-956d-a44cbf66df22"
#define INITIAL_MESSAGE_CHARACTERISTIC_UUID "71261243-3540-4179-818f-91bfff21af94"


BLEServer* pServer;
BLEService* pSmogometrDataService;
BLECharacteristic* pPPM2_5Characteristic;
BLECharacteristic* pPPM10Characteristic;
BLECharacteristic* pNOXCharacteristic;
BLECharacteristic* pSOXCharacteristic;

BLEService* pSmogometrInitialService;
BLECharacteristic* pInitialMessageCharacteristic

#define MyName "Smogometr_1"


class MyCallbacks : public BLECharacteristicCallbacks {
    void onWrite(BLECharacteristic *pCharacteristic) {
        std::string value = pCharacteristic->getValue();

        if (value.length() > 0) {
            Serial.println("Received data from central device:");
            for (int i = 0; i < value.length(); i++)
                Serial.print(value[i]);
            Serial.println();
        }
    }
};

class MyServerCallbacks : public BLEServerCallbacks {
    void onConnect(BLEServer *pServer) {

        BLEService *pSmogometrService = pServer->getServiceByUUID(SMOGOMETR_SERVICE_UUID);
        BLECharacteristic* pPPM2_5Characteristic = pService->getCharacteristic(PPM2_5_CHARACTERISTIC_UUID);
        BLECharacteristic* pPPM10Characteristic = pService->getCharacteristic(PPM10_CHARACTERISTIC_UUID);
        BLECharacteristic* pNOXCharacteristic = pService->getCharacteristic(NOX_CHARACTERISTIC_UUID);
        BLECharacteristic* pSOXCharacteristic = pService->getCharacteristic(SOX_CHARACTERISTIC_UUID);

        
        pCharacteristic->setValue("Hello from peripheral");
        pCharacteristic->notify();
    }
};

void setup() {
    Serial.begin(115200);

    BLEDevice::init(MyName);
    BLEServer *pServer = BLEDevice::createServer();
    pServer->setCallbacks(new MyServerCallbacks());// do sprawdzenia

    BLEService *pSmogometrService = pServer->createService(SMOGOMETR_SERVICE_UUID);

    BLECharacteristic *pCharacteristic = pService->createCharacteristic(
                                         CHARACTERISTIC_UUID,
                                         BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_WRITE |
                                         BLECharacteristic::PROPERTY_NOTIFY
                                       );

    pCharacteristic->setCallbacks(new MyCallbacks());

    pCharacteristic->setValue("Hello World");
    pService->start();

    BLEAdvertising *pAdvertising = pServer->getAdvertising();
    pAdvertising->start();
}

void loop() {
    // put your main code here, to run repeatedly:
    delay(2000);
}