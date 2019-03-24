# Vaatimusmäärittely

## Sovelluksen tarkoitus

Sovellus auttaa käyttäjää seuraamaan (suomalaisia) ostokuitteja, sekä pitämään niistä kirjaa.


## Käyttäjät

Sovelluksella on yksi käyttäjärooli eli _normaali käyttäjä_.


## Käyttöliittymäluonnos

Sovelluksessa on aluksi vain yksi näkymä, joka on jaettu kolmeen osaan.

<img src="./kuvat/UI-luonnos.png" width="750">

Oikeanpuoleisessa kuittitaulukossa on lista kaikista järjestelmään syötetyistä kuiteista.
Keskellä näkyy kuittitaulukosta kulloinkin valitun kuitin tiedot. Vasemmalla on kuva kuitista. 


## Perusversion tarjoama toiminnallisuus

- käyttäjä voi lisätä järjestelmään uuden kuitin

  - kuitista tallennetaan ostajan nimi, ostopaikan nimi, päivämäärä, ostokset riveittäin sekä yhteissumma
  
  - yksi ostosrivi koostuu vähintään yhdestä ostostyypistä (esim. "parturi"), kappalemäärästä ja yksikköhinnasta
  
- käyttäjä voi tarkastella kaikkia järjestelmään lisättyjä kuitteja  
  
- käyttäjä voi poistaa järjestelmästä kuitin


## Jatkokehitysideoita

Järjestelmää täydennetään mahdollisuuksien mukaan esim. seuraavilla toiminnallisuuksilla

- käyttäjä voi pyytä yksinkertaisen yhteenvedon kuittien sisältämistä ostoksista

- järjestelmään jo lisättyjen kuittien tietojen editointi

- kuittiin voi tallentaa pääostostyypin lisäksi lisäostostyyppejä (esim. päätyyppi: elintarvike, lisätyyppi: hedelmä)

- käyttäjä voi hakea ja listata ostoksia tai kuitteja niihin liittyvien tietojen perusteella

- kuitin kuvan tallentaminen järjestelmään kuittitietojen yhteyteen

- avusteinen kuittien lisäys tekstintunnistusta hyödyntämällä

  - kuitin kuva voidaan lähettää esim. Google Cloud Visionille, jonka antamien tietojen perusteella pyritään esitäyttämään kuitin tiedot
  
  - ostostyypit voidaan esitäyttää ostopaikan nimen perusteella (esim. jos nimessä esiintyy sana "apteekki" niin esitäytetään päätyypit lääkkeiksi)
  
  - ostostyypit voidaan esitäyttää käyttäjän aiemmin samaan kuittirivin nimikkeeseen liittämästä ostostyypistä (kuittirivin nimike on siis ostopaikan käyttämä nimitys tuotteesta, esim. "Belbake puolikarkea vehnj.")
  
  - ostostyypit voidaan esitäyttää sanalistoja hyödyntämällä (esim. Finelin järjestelmästä lista elintarvikkeiden nimistä)
  
  - ostostyypit voidaan esitäyttää pyytämällä Cloud Visionilta nimikkeen tunnistamista kuvahakuun perustuen
  
- käyttäjä voi luoda erilaisia raportteja ostoksista 
