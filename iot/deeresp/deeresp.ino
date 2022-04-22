#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 128
#define SCREEN_ROTATION 0
#define WIFI_SSID "wifi名称"
#define WIFI_PASSWORD "wifi密码"
#define MQTT_CLIENT_NAME "DeerEsp-001" // 多个同名设备连接同一台服务器会导致其他下线，所以起一个唯一的名字吧
#define MQTT_TOPIC "LB2353" // 这里填PushDeer的Key

#define MQTT_IP "broker.emqx.io"
#define MQTT_USER ""
#define MQTT_PASSWORD ""
#define MQTT_PORT 1883


// ====== 以下不用修改 ===============
#define DOWNLOADED_IMG "/download.jpg"

#define IMG_SCALE 2
#define TXT_SCALE 1


#include <EspMQTTClient.h>

EspMQTTClient mclient(
  WIFI_SSID,
  WIFI_PASSWORD,
  MQTT_IP,  
  MQTT_USER,   
  MQTT_PASSWORD,  
  MQTT_CLIENT_NAME, 
  MQTT_PORT           
);

#include "cubic_12.h"
#include <TFT_eSPI.h> 
TFT_eSPI tft = TFT_eSPI(); 

#include <NTPClient.h>

#ifdef ESP8266
  #include <ESP8266HTTPClient.h>
  #define BEEP_PIN PIN_D8
#else
  #include "SPIFFS.h" // Required for ESP32 only
  #include <HTTPClient.h>
#endif

#include <TJpg_Decoder.h>

#include <WiFiUdp.h>
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP,"ntp1.aliyun.com",60*60*8,60000);

void setup() {
  Serial.begin(115200);
  mclient.enableDebuggingMessages();
  

  tft.begin();
  tft.setRotation(SCREEN_ROTATION); // 屏幕方向
  tft.fillScreen(TFT_BLACK);
  tft.setTextColor(0xFFFF,0x0000);tft.setCursor(0, 0, 1);tft.setTextSize(TXT_SCALE);tft.println("Init ...");
  Serial.println("tft init");

  if (!SPIFFS.begin()) {
    Serial.println("SPIFFS initialisation failed!");
    while (1) yield(); // Stay here twiddling thumbs waiting
  }
  Serial.println("SPIFFS init");

  TJpgDec.setJpgScale(IMG_SCALE);
  TJpgDec.setSwapBytes(true);
  TJpgDec.setCallback(tft_output);

  Serial.println("TJpgDec init");
  timeClient.begin();

}

void onConnectionEstablished()
{
  Serial.println("connected");
  tft.setTextColor(0xFFFF,0x0000);tft.setCursor(0, 0, 1);tft.println("Waiting for messages at "+String(MQTT_TOPIC)+"...");
  
  mclient.subscribe(String(MQTT_TOPIC)+"_text", [] (const String &payload)  
  {
    Serial.println(payload);
    
    if (SPIFFS.exists(DOWNLOADED_IMG) == true) TJpgDec.drawFsJpg(0, 0, DOWNLOADED_IMG);
    else tft.fillScreen( TFT_BLACK );

    show_time(true);
    
    #ifdef BEEP_PIN
    if(payload.indexOf("♪") >= 0) tone(BEEP_PIN, 1000, 100);
    #endif  

    tft.loadFont(cubic_11);
    
    if( payload.length() > 80 ) tft.setTextSize(TXT_SCALE/2);
    else tft.setTextSize(TXT_SCALE);

    char *found;
    short line = 0;
    int base = 5;
    int now_base = 0;
    char * payloads = const_cast<char*> ( payload.c_str() );
    while( (found = strsep( &payloads , "\n" ) ) != NULL )
    {
      now_base = base + line*14;
      tft.setTextColor(0x0000);tft.setCursor(base+1, now_base+1);tft.println(found);
      tft.setTextColor(0x0000);tft.setCursor(base+2, now_base+2);tft.println(found);
      tft.setTextColor(0xFFFF);tft.setCursor(base, now_base);tft.println(found);
      line++;

    }
      
    tft.unloadFont();
    
    
  });
  
  mclient.subscribe(String(MQTT_TOPIC)+"_bg_url", [] (const String &payload)  
  {
    Serial.println(payload);
    bool ret = file_put_contents(payload, DOWNLOADED_IMG);
    if (SPIFFS.exists(DOWNLOADED_IMG) == true) {
      TJpgDec.drawFsJpg(0, 0, DOWNLOADED_IMG);
      show_time(true);
    }
  });  
}

String lastTime = "2020";
String newTime = "";

void loop() {
  mclient.loop();
  show_time(false);
}

void show_time(bool force)
{
    timeClient.update();
    String hourStr = timeClient.getHours() < 10 ? "0" + String(timeClient.getHours()) :  String(timeClient.getHours());
    String minStr = timeClient.getMinutes() < 10 ? "0" + String(timeClient.getMinutes()) : String(timeClient.getMinutes());
    newTime = hourStr + ":" + minStr ;
    if( lastTime != newTime )
    {
      echo_time( newTime );  
      lastTime = newTime;
    }
    else
    {
      if( force ) echo_time( newTime );  
    }
}

void echo_time( String thetime )
{
  if( SCREEN_WIDTH == 128 )
  {
    tft.setCursor(96, 120, 1);
    tft.setTextSize(1);
  
  }
  if( SCREEN_WIDTH == 240 )
  {
    tft.setCursor(180, 210, 1);
    tft.setTextSize(2);
  
  } 
  
  
  tft.setTextColor(TFT_WHITE,TFT_BLACK);
  tft.println(thetime);
  
  
  tft.setTextSize(TXT_SCALE);
}

bool file_put_contents(String url, String filename) {

  Serial.println("Downloading "  + filename + " from " + url);

  // Check WiFi connection
  if (WiFi.status() == WL_CONNECTED) {

    Serial.print("[HTTP] begin...\n");

    WiFiClient client;
    HTTPClient http;
    http.setFollowRedirects(HTTPC_STRICT_FOLLOW_REDIRECTS);
    http.begin(client, url);


    Serial.print("[HTTP] GET...\n");
    int httpCode = http.GET();
    if (httpCode > 0) {
      fs::File f = SPIFFS.open(filename, "w+");
      if (!f) {
        Serial.println("file open failed");
        return 0;
      }
      Serial.printf("[HTTP] GET... code: %d\n", httpCode);
      if (httpCode == HTTP_CODE_OK) {

        int total = http.getSize();
        int len = total;

        uint8_t buff[128] = { 0 };
        WiFiClient * stream = http.getStreamPtr();

        while (http.connected() && (len > 0 || len == -1)) {
          size_t size = stream->available();

          if (size) {
            int c = stream->readBytes(buff, ((size > sizeof(buff)) ? sizeof(buff) : size));

            f.write(buff, c);

            if (len > 0) {
              len -= c;
            }
          }
          yield();
        }
        Serial.println();
        Serial.print("[HTTP] connection closed or file end.\n");
      }
      f.close();
    }
    else {
      Serial.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
      Serial.print(httpCode);

    }
    http.end();
  }
  return 1; 
}

bool tft_output(int16_t x, int16_t y, uint16_t w, uint16_t h, uint16_t* bitmap)
{
  if ( y >= tft.height() ) return 0;
  tft.pushImage(x, y, w, h, bitmap);
  return 1;
}
