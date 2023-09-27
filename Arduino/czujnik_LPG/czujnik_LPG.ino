void setup() { 
  Serial.begin(115200); 
  pinMode(A0, INPUT);
  pinMode(8, OUTPUT);
  pinMode(7, OUTPUT);
} 
  
double sensor_volt;  
double RS_air; // Rs in clean air 
double R0;  // R0 in 1000 ppm LPG 
double sensorValue; 
int i = 0;
boolean sendMeasurements = false; // Flaga do kontroli wysyłania pomiarów

void loop() { 
  if (Serial.available() > 0) {
    char incomingChar = Serial.read();
    
    if (incomingChar == 'm') {
      // Jeśli otrzymano 'm', ustaw flagę do wysyłania pomiarów
      digitalWrite(7, HIGH);
      delay(500);
      digitalWrite(7, LOW);
      sendMeasurements = true;
    }
  }

  if (sendMeasurements) {
    // Average   
    for (int x = 0; x < 100; x++) { 
      sensorValue = sensorValue + analogRead(A0); 
    } 
    sensorValue = sensorValue / 100.0; 

    sensor_volt = (sensorValue / 1024) * 5.0; 
    RS_air = (5.0 - sensor_volt) / sensor_volt; 
    R0 = RS_air / 9.9; 
    
    // Wysyłanie pomiarów
    
    
    Serial.print(R0);
    Serial.print(";");
    

    digitalWrite(8, HIGH);
    delay(500);
    digitalWrite(8, LOW);
    delay(4500); 


    sendMeasurements = false; // Wyzerowanie flagi do wysyłania pomiarów


  }
}