#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 128
#define SCREEN_ROTATION 0
#define DOWNLOADED_IMG "/download.jpg"
// #define FULL_FONT 1
// #define CHINESE_FONT 1

#define BLK_PIN 19
#define BTN_PIN 0

#include <WiFiManager.h>
#include <EspMQTTClient.h>
#include <ArduinoJson.h>
#include <Effortless_SPIFFS.h>

WiFiManager wm;

WiFiManagerParameter mqtt_host_field; 
WiFiManagerParameter mqtt_port_field; 
WiFiManagerParameter mqtt_topic_field; 
WiFiManagerParameter mqtt_client_field;
WiFiManagerParameter mqtt_user_field;
WiFiManagerParameter mqtt_password_field;  

String mqtt_host_value = "";
short mqtt_port_value = 1883;
String mqtt_client_postfix = "";
String mqtt_topic_value = "";
String mqtt_user_value = "";
String mqtt_client_value = "";
String mqtt_password_value = "";

//EspMQTTClient mclient(
//  WIFI_SSID,
//  WIFI_PASSWORD,
//  MQTT_IP,  
//  MQTT_USER,   
//  MQTT_PASSWORD,  
//  MQTT_CLIENT_NAME, 
//  MQTT_PORT           
//);
EspMQTTClient mclient;

#ifdef CHINESE_FONT
  #ifdef FULL_FONT
    #define AA_FONT_CUBIC "Cubic1112"
  #else
    #include "cubic_12.h"
  #endif
#endif


// #include "SPI.h"
#include <TFT_eSPI.h> 
TFT_eSPI tft = TFT_eSPI(); 

#include <NTPClient.h>

#ifdef ESP8266
  #include <ESP8266HTTPClient.h>
  #define BEEP_PIN D8
  #define IMG_SCALE 2
  #define TXT_SCALE 2
#else
  #include "SPIFFS.h" // Required for ESP32 only
  #define IMG_SCALE 2
  #define TXT_SCALE 2
  #define BEEP_PIN 22
  #include <HTTPClient.h>
#endif

#include <TJpg_Decoder.h>
#include <EasyButton.h>
EasyButton clearbtn(BTN_PIN);

#include <WiFiUdp.h>
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP,"ntp1.aliyun.com",60*60*8,60000);

//flag for saving data
bool shouldSaveConfig = false;

//callback notifying us of the need to save config
void saveConfigCallback () {
  Serial.println("Should save config");
  shouldSaveConfig = true;
}

DynamicJsonDocument jsonDocument(1024);

void setup() {
  Serial.begin(115200);
  mclient.enableDebuggingMessages();
  clearbtn.begin();
  clearbtn.onPressed(clear_config);

  tft.begin();
  pinMode(BLK_PIN, OUTPUT);
  digitalWrite(BLK_PIN, HIGH);

  // tft.setRotation(2); // 屏幕方向
  tft.fillScreen(TFT_BLACK);
  tft.setTextColor(0xFFFF,0x0000);tft.setCursor(0, 0, 1);tft.setTextSize(TXT_SCALE);tft.println("Init ...");
  Serial.println("tft init");

  if (!SPIFFS.begin()) {
    Serial.println("SPIFFS initialisation failed!");
    while (1) yield(); // Stay here twiddling thumbs waiting
  }
  Serial.println("SPIFFS init");

  eSPIFFS fileSystem;

  // Check Flash Size - Always try to incorrperate a check when not debugging to know if you have set the SPIFFS correctly
  if (!fileSystem.checkFlashConfig()) {
    Serial.println("Flash size was not correct! Please check your SPIFFS config and try again");
    delay(100000);
    ESP.restart();
  }

  TJpgDec.setJpgScale(IMG_SCALE);
  TJpgDec.setSwapBytes(true);
  TJpgDec.setCallback(tft_output);

  Serial.println("TJpgDec init");


  // 检测本地配置文件
  if( SPIFFS.exists( "/config.json" ) )
  {
     Serial.println("config.json exist");
     fileSystem.openFromFile("/config.json", jsonDocument);
     Serial.print("JSON Document is: ");
     serializeJson(jsonDocument, Serial);
     Serial.println();

     Serial.println(jsonDocument["wifi_ssid"].as<String>());
     Serial.println(jsonDocument["wifi_password"].as<String>());
     
     // 链接wifi
     WiFi.begin( jsonDocument["wifi_ssid"].as<const char*>(), jsonDocument["wifi_password"].as<const char*>() );

     mqtt_host_value = jsonDocument["mqtt_host"].as<String>();
     mqtt_port_value = jsonDocument["mqtt_port"].as<short>();
     mqtt_user_value = jsonDocument["mqtt_user"].as<String>();
     mqtt_password_value = jsonDocument["mqtt_password"].as<String>();
     mqtt_topic_value = jsonDocument["mqtt_topic"].as<String>();
     mqtt_client_value = jsonDocument["mqtt_client"].as<String>();
     
  }else
  {
    Serial.println("config.json not exist");
    tft.fillScreen( TFT_BLACK );
    tft.setCursor(0, 0, 1);
    tft.println("Connect to DeerEspWiFi, go 192.168.4.1");
  
    WiFi.mode(WIFI_STA);
    wm.resetSettings();
  
    // add a custom input field
    int customFieldLength = 40;
    
    new (&mqtt_host_field) WiFiManagerParameter("mqtt_host", "MQTT IP", "broker.emqx.io", customFieldLength,"placeholder=\"MQTT server IP\"");
    new (&mqtt_port_field) WiFiManagerParameter("mqtt_port", "MQTT Port", "1883", customFieldLength,"placeholder=\"MQTT server port, 1883 as default\"");
    new (&mqtt_topic_field) WiFiManagerParameter("mqtt_topic", "MQTT Topic", "LB2312", customFieldLength,"placeholder=\"MQTT base topic\"");
    new (&mqtt_client_field) WiFiManagerParameter("mqtt_client", "MQTT Client ID", "DeerESP0001", customFieldLength,"placeholder=\"MQTT client id, can be empty\"");
    new (&mqtt_user_field) WiFiManagerParameter("mqtt_user", "MQTT User", "", customFieldLength,"placeholder=\"MQTT user, can be empty\"");
    new (&mqtt_password_field) WiFiManagerParameter("mqtt_password", "MQTT Password", "", customFieldLength,"placeholder=\"MQTT password, can be empty\"");
    
    
    wm.addParameter(&mqtt_host_field);
    wm.addParameter(&mqtt_port_field);
    wm.addParameter(&mqtt_topic_field);
    wm.addParameter(&mqtt_client_field);
    wm.addParameter(&mqtt_user_field);
    wm.addParameter(&mqtt_password_field);
    
    wm.setSaveParamsCallback(saveParamCallback);
    wm.setSaveConfigCallback(saveConfigCallback);
    
    bool res;
    res = wm.autoConnect("DeerEspWiFi"); // anonymous ap
  
    if(!res) {
          Serial.println("Failed to connect");
          ESP.restart();
    } 
    else {
          //if you get here you have connected to the WiFi    
          Serial.println("connected...yeey :)");
    }
  
  }
  

  

  mclient.enableDebuggingMessages(true);
  mclient.setMqttClientName(mqtt_client_value.c_str());
  mclient.setMqttServer(mqtt_host_value.c_str(), mqtt_user_value.c_str(), mqtt_password_value.c_str(), mqtt_port_value);


  if (shouldSaveConfig)
  {
    
    jsonDocument["mqtt_host"] = mqtt_host_value;
    jsonDocument["mqtt_port"] = mqtt_port_value;
    jsonDocument["mqtt_user"] = mqtt_user_value;
    jsonDocument["mqtt_password"] = mqtt_password_value;
    jsonDocument["mqtt_topic"] = mqtt_topic_value;
    jsonDocument["mqtt_client"] = mqtt_client_value;
    jsonDocument["wifi_ssid"] = WiFi.SSID();
    jsonDocument["wifi_password"] = WiFi.psk();
    
    fileSystem.saveToFile("/config.json", jsonDocument);

    Serial.print("JSON Document is: ");
    serializeJson(jsonDocument, Serial);
    Serial.println();

    if(SPIFFS.exists("/config.json")) Serial.print("config.json exists ");
    else Serial.print("config.json not exists ");

    

//    File root = SPIFFS.open("/","r");
// 
//    File file = root.openNextFile();
//   
//    while(file){
//   
//        Serial.print("FILE: ");
//        Serial.println(file.name());
//   
//        file = root.openNextFile();
//    }
  
  }


}

String getParam(String name){
  //read parameter from server, for customhmtl input
  String value;
  if(wm.server->hasArg(name)) {
    value = wm.server->arg(name);
  }
    return value;
 }

 void saveParamCallback(){
    mqtt_host_value = getParam("mqtt_host");
    mqtt_user_value = getParam("mqtt_user");
    mqtt_password_value = getParam("mqtt_password");
    mqtt_topic_value = getParam("mqtt_topic");
    mqtt_client_value = getParam("mqtt_client");
    mqtt_port_value = getParam("mqtt_port").toInt();
    Serial.println(String(mqtt_port_value));
    if( mqtt_port_value < 1 ) mqtt_port_value = 1883;
    Serial.println("[CALLBACK] saveParamCallback fired ");
 }


void onConnectionEstablished()
{
  Serial.println("connected");
  tft.fillScreen(TFT_BLACK);tft.setTextColor(0xFFFF,0x0000);tft.setCursor(0, 0, 1);tft.println("Waiting for messages at "+mqtt_topic_value+"...");
  
  mclient.subscribe(mqtt_topic_value+"_text", [] (const String &payload)  
  {
    Serial.println(payload);
    
    if (SPIFFS.exists(DOWNLOADED_IMG) == true) TJpgDec.drawFsJpg(0, 0, DOWNLOADED_IMG);
    else tft.fillScreen( TFT_BLACK );
    
    #ifdef CHINESE_FONT
      #ifdef FULL_FONT
        tft.loadFont(AA_FONT_CUBIC);
      #else
        tft.loadFont(cubic_11);
      #endif
    #endif
    
    
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
      tft.setTextColor(0xFFFF);tft.setCursor(base, now_base);tft.println(found);
      line++;

    }
      
    #ifdef CHINESE_FONT
      tft.unloadFont();
    #endif
    

    show_time(true);

    #ifdef BEEP_PIN
    if(payload.indexOf("♪") >= 0) tone(BEEP_PIN, 1000, 100);
    #endif
    
    
  });
  
  mclient.subscribe(mqtt_topic_value+"_bg_url", [] (const String &payload)  
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
  clearbtn.read();
}

void clear_config()
{
    SPIFFS.remove("/config.json");
    tone(BEEP_PIN, 1000, 100);
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

#ifdef ESP32
void tone(byte pin, int freq, int timeout) {
  ledcSetup(0, 2000, 8); // setup beeper
  ledcAttachPin(pin, 0); // attach beeper
  ledcWriteTone(0, freq); // play tone
  delay(timeout);
  ledcWriteTone(0, 0);
  
}
#endif
