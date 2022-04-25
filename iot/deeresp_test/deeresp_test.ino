#define WIFI_SSID "wifi名称"
#define WIFI_PASSWORD "wifi密码"
#define SCREEN_WIDTH 240



#define BEEP_PIN PIN_D8 // 蜂鸣器

#include <ESP8266WiFi.h>
#include <TFT_eSPI.h> 
TFT_eSPI tft = TFT_eSPI(); 

#include <TJpg_Decoder.h>
#include <EasyButton.h>
EasyButton d0_btn(PIN_D0);
EasyButton d3_btn(PIN_D3);
EasyButton d6_btn(PIN_D6);

void setup() {
  Serial.begin(115200);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  d0_btn.begin();
  d0_btn.onPressed(d0_pressed);

  d3_btn.begin();
  d3_btn.onPressed(d3_pressed);

  d6_btn.begin();
  d6_btn.onPressed(d6_pressed);


  tft.begin();
  
  tft.fillScreen(TFT_BLACK);
  tft.setTextColor(0xFFFF,0x0000);tft.setCursor(0, 0, 1);tft.setTextSize(2);tft.println("Init ...");
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
  d0_btn.read();
  d3_btn.read();
  d6_btn.read();
}

int i = 0;

void d0_pressed()
{
  btn_pressed("D0 button pressed");
}

void d3_pressed()
{
  btn_pressed("D3 button pressed");
}

void d6_pressed()
{
  btn_pressed("D6 button pressed");
}

void btn_pressed(String name)
{
    tone(BEEP_PIN, 1000, 100);
    
    i++;
    short color = TFT_BLACK;
    if( i % 3 == 0 ){ color = TFT_RED; }
    if( i % 3 == 1 ){ color = TFT_YELLOW; }
    if( i % 3 == 2 ){ color = TFT_BLUE; }
    
    tft.fillScreen(color);
    tft.setTextColor(0xFFFF,color);
    tft.setCursor(0, 0, 1);
    
    tft.setCursor(0, 0, 1);
    tft.println(name + " button Pressed ...");
    if( WiFi.status() == WL_CONNECTED )
      tft.println("Wifi connected ...");

}

//void show_image()
//{
//    if (SPIFFS.exists("/cover.jpeg") == true) {
//      TJpgDec.drawFsJpg(0, 0, "/cover.jpeg");
//    }
//    tft.setCursor(0, 0, 1);
//    tft.println( "image button Pressed ...");
//    if( WiFi.status() == WL_CONNECTED )
//      tft.println("Wifi connected ...");
//
//}

bool tft_output(int16_t x, int16_t y, uint16_t w, uint16_t h, uint16_t* bitmap)
{
  if ( y >= tft.height() ) return 0;
  tft.pushImage(x, y, w, h, bitmap);
  return 1;
}
