#include <TFT_eSPI.h>

TFT_eSPI tft = TFT_eSPI();

void setup() {
  Serial.begin(115200);
  Serial.print("hello world\n");

  // Set up LCD
  tft.begin();
  tft.fillScreen(TFT_BLUE);

  pinMode(19, OUTPUT);
  digitalWrite(19, HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:
}
