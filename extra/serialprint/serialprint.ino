#include "DHT.h"

/*
 * CAPTEUR INTERIEUR
 */
#define DHTPIN_INT 2
#define DHTTYPE_INT DHT22
DHT dht_int(DHTPIN_INT, DHTTYPE_INT);

/*
 * CAPTEUR EXTERIEUR
 */
#define DHTPIN_EXT 3  
#define DHTTYPE_EXT DHT11 
DHT dht_ext(DHTPIN_EXT, DHTTYPE_EXT);

/*
 * MODULE PELTIER
 */
#define PELTIER_PIN 4
#define CONSIGNE_BASE 23

int consigne = 0;

 
void setup() {
  Serial.begin(9600); 
  dht_int.begin();
  dht_ext.begin();
  pinMode(PELTIER_PIN, OUTPUT);
} // FIN DE LA FONCTION SETUP

 
void loop() {

  // MESURE INTERIEURE
  float h_int = dht_int.readHumidity();
  float t_int = dht_int.readTemperature();

  // MESURE EXTERIEUR
  float h_ext = dht_ext.readHumidity();
  float t_ext = dht_ext.readTemperature();
  
  // GESTION DES ERREURS POUR LES DEUX CAPTEURS (A DECOMMENTER SI BESOIN DE TEST BRANCHEMENT) 
  /* 
    if (isnan(h_int) || isnan(t_int)) {
      Serial.println("Erreur de lecture capteur interne!");
      return;
    }
    else if (isnan(h_ext) || isnan(t_ext)) {
      Serial.println("Erreur de lecture capteur externe!");
      return;
    }
   */

  // ENVOIE DES MESURES SOUS LA FORME (HUMIDITE_INT  TEMPERATURE_INT HUMIDITE_EXT  TEMPERATURE_EXT)
  Serial.print(h_int);
  Serial.print("\t");
  Serial.print(t_int);
  Serial.print("\t");
  Serial.print(h_ext);
  Serial.print("\t");
  Serial.println(t_ext);

  
  
  if (Serial.available() > 0) {
      consigne = Serial.read();
  }

  if(t_int < CONSIGNE_BASE && consigne == 0 || 
     t_int < consigne && consigne != 0) {
     digitalWrite(PELTIER_PIN, LOW);
  }
  else {
     digitalWrite(PELTIER_PIN, HIGH);
  }
  
  
 
  
  // ATTENTE DE 2SECONDES ENTRE CHAQUE MESURE
  delay(2000);
 
} // FIN DE LA FONCTION LOOP
