//#include <Thread.h>
//#include <ThreadController.h>
#include <SoftwareSerial.h>
#include <BH1750.h>
#include <Wire.h>
#include<DHT.h>
#include <time.h>
#include<DS1302.h>

#define DHTTYPE DHT11
int led = 12;
int dhtPin = A0;//온습도
int RST_PIN = 6;//시간
int DATA_PIN = 7;//시간
int CLK_PIN = 8;//시간
int AA = 4;//물모터
int AB = 5;//물모터
BH1750 lightMeter;//조도 센서
DHT dht(dhtPin, DHTTYPE);//온습도 센서
DS1302 rtc(RST_PIN,DATA_PIN,CLK_PIN);
String data = "";
int message[7];
boolean ledStatue = false, waterStatue = false;
String settime;

void setup() {
  pinMode(led, OUTPUT);
  pinMode(AA, OUTPUT);
  pinMode(AB, OUTPUT);
  rtc.halt(false);//동작 모드로 설정
  rtc.writeProtect(false); //시간 변경을 가능하게 설정
  rtc.setTime(12,32,00);
  rtc.setDate(25, 3, 2022);

  Serial.begin(9600);
  Serial1.begin(9600);
  Wire.begin();
  lightMeter.begin();
}

String getBTData(){
  String str="";
 while(Serial1.available()){
    str +=(char)Serial1.read();
    delay(5);
  }
  return str;
}
void loop() { 
  data = getBTData();//블루투스 값
  uint16_t lux = lightMeter.readLightLevel();//조도 센서
  int h = dht.readHumidity(); //습도
  int t = dht.readTemperature();//온도
  int soil = map(analogRead(A1),1023,260,0,100);//토양 수분
  delay(200);

//  String current = rtc.getTimeStr();
//    Serial.println(current);
  
  if(ledStatue){//led 상태 확인
    String current = rtc.getTimeStr();
    Serial.println(current);
    if(current.equals(settime)){//현재 시간 = 설정 시간 -> LED off
      analogWrite(led,0);
      ledStatue = false;
    }
  }
  if(waterStatue){ //토양 수분 확인
    if(soil>=90){ //98%(78)이상이라면 끄기
      digitalWrite(AA,LOW);
      digitalWrite(AB,LOW);
//      waterStatue = false;
    }
    else if(soil<=30){
      digitalWrite(AA,HIGH);
      digitalWrite(AB,HIGH);
    }
  }
  if(!data.equals("")){//블루투스 수신

    if(data.indexOf("on")!=-1){  //led ON
      settime = data.substring(3,data.length());
      ledStatue = true;
      analogWrite(led,255);
      delay(1000);
    }
    else if(data.equals("off")){ //led Off
      analogWrite(led,0);
      ledStatue = false;
    }
    else if(data.equals("WaterOn")){ //water ON
      if(soil<=30){ //물이 30%보다 작을 때 물 틀기(soil>=84)
        digitalWrite(AA,HIGH);
        digitalWrite(AB,LOW);
        waterStatue = true;
      }
    }
    else if(data.equals("WaterOff")){ //water Off
      digitalWrite(AA,LOW);
      digitalWrite(AB,LOW);
      waterStatue = false;
    }
    else if(data.equals("reset")){ //reset버튼
      message[0] = t;
      message[1] = h;
      message[2] = soil;
      
      int temp = lux;
      temp = lux/127;
      if(temp>127){
        message[3] = -(temp/127);
        message[4] = temp%127;
        message[5] =  lux%(127*temp);
      }
      else{
        message[3] = temp;
        message[4] = lux%127;
        message[5] = 0;
      }
      message[6] = -128;
      for(int i=0;i<7;i++){
        Serial1.write(message[i]);
//        Serial.println(message[i]);
        delay(100);
      }
      
      Serial.print("temperature: ");
      Serial.println(t);
      Serial.print("humidity: ");
      Serial.println(h);
      Serial.print("soil: ");
      Serial.println(soil);
      Serial.print("lux: ");
      Serial.println(lux);
    }
    else{
      ;
    }
    data = "";
  }
  
}
