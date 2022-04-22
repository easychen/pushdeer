#define WIFI_SSID "wifi名称"
#define WIFI_PASSWORD "wifi密码"
#define SCREEN_WIDTH 240


#define BEEP_BTN PIN_D0
#define IMG_BTN PIN_D6

#define BEEP_PIN PIN_D8 // 蜂鸣器

#include <ESP8266WiFi.h>
#include <TFT_eSPI.h> 
TFT_eSPI tft = TFT_eSPI(); 

#include <TJpg_Decoder.h>
#include <EasyButton.h>
EasyButton beep_btn(BEEP_BTN);
EasyButton image_btn(IMG_BTN);

void setup() {
  Serial.begin(115200);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  beep_btn.begin();
  beep_btn.onPressed(beep);

  image_btn.begin();
  image_btn.onPressed(show_image);

  tft.begin();
  
  tft.fillScreen(TFT_BLACK);
  tft.setTextColor(0xFFFF,0x0000);tft.setCursor(0, 0, 1);tft.setTextSize(1);tft.println("Init ...");
  Serial.println("tft init");

  if (!SPIFFS.begin()) {
    Serial.println("SPIFFS initialisation failed!");
    while (1) yield(); // Stay here twiddling thumbs waiting
  }
  Serial.println("SPIFFS init");
  tft.println("SPIFFS init ...");

  if( SCREEN_WIDTH == 128 )
  {
    TJpgDec.setJpgScale(2);
  }
  
  TJpgDec.setSwapBytes(true);
  TJpgDec.setCallback(tft_output);

  Serial.println("TJpgDec init");
  tft.println("TJpgDec init ...");

  if( WiFi.status() == WL_CONNECTED )
  tft.println("Wifi connected ...");

  tft.println("Press button ...");

  

}

void loop() {
  // put your main code here, to run repeatedly:
  beep_btn.read();
  image_btn.read();
}

void beep()
{
    tone(BEEP_PIN, 1000, 100);
    if (SPIFFS.exists("/cover.jpeg") == true) {
      TJpgDec.drawFsJpg(0, 0, "/cover.jpeg");
    }else
    {
      tft.fillScreen(TFT_BLACK);
    }
    tft.setCursor(0, 0, 1);
    tft.println("beep button Pressed ...");
    if( WiFi.status() == WL_CONNECTED )
      tft.println("Wifi connected ...");

}

void show_image()
{
    if (SPIFFS.exists("/cover.jpeg") == true) {
      TJpgDec.drawFsJpg(0, 0, "/cover.jpeg");
    }
    tft.setCursor(0, 0, 1);
    tft.println( "image button Pressed ...");
    if( WiFi.status() == WL_CONNECTED )
      tft.println("Wifi connected ...");

}

bool tft_output(int16_t x, int16_t y, uint16_t w, uint16_t h, uint16_t* bitmap)
{
  if ( y >= tft.height() ) return 0;
  tft.pushImage(x, y, w, h, bitmap);
  return 1;
}
