# Kuittiseuranta

Sovellus auttaa käyttäjää seuraamaan (suomalaisia) ostokuitteja, sekä pitämään niistä kirjaa.

## Dokumentaatio

[Vaatimusmäärittely](./dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](./dokumentaatio/arkkitehtuuri.md)

[Työaikakirjanpito](./dokumentaatio/tuntikirjanpito.md)

[Käyttöohje](./dokumentaatio/käyttöohje.md)

[Releaset](https://github.com/ric-rai/finnish-receipt-tracker/releases)

## Komentorivitoiminnot

### Suorittaminen

Sovellus on JavaFX 11 -sovellus, joten JDK 11 tai 12 on oltava asennettuna. 
Lisää tietoa JavaFX 11- ja 12 -sovelluksista: https://openjfx.io/openjfx-docs/

Sovellus suoritetaan komennolla

```
mvn compile exec:java -Dexec.mainClass=fi.frt.MainApp
```

Jar-tiedosto generoidaan komennolla

```
mvn package
```

Jar-tiedosto löytyy _shade_ -kansiosta. Se käynnistetään _shade_ -kansiosta komennolla:

```
java -jar finnish-receipt-tracker.jar
```

Käynnistyksen yhteydessä ohjelma luo application.properties-tiedostossa määritellyn tietokantatiedoston.

Pom.xml -tiedostosta voit muuttaa jar-tiedoston luomisessa käytettäviä asetuksia, 
kuten tallennuskansiota ja alustayhteensopivuuutta. Jar-tiedoston käynnistysluokaksi on
määritelty Launcher -luokka, mikä on vaatimus moduulittomalle ohjelmalle JavaFX 11 -versiossa. Kehitysympäristössä järjestelmätestaukseen käytettyä data.sql tiedostoa ei sisällytetä jar-tiedostoon.

### Testaus


Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn test jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_


Checkstyle-tarkistus tehdään komennolla

```
mvn jxr:jxr checkstyle:checkstyle
```

