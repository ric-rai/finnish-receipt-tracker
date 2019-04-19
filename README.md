# Kuittiseuranta

Sovellus auttaa käyttäjää seuraamaan (suomalaisia) ostokuitteja, sekä pitämään niistä kirjaa.

## Dokumentaatio

[Vaatimusmäärittely](./dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](./dokumentaatio/arkkitehtuuri.md)

[Työaikakirjanpito](./dokumentaatio/tuntikirjanpito.md)

[Käyttöohje](./dokumentaatio/käyttöohje.md)

## Komentorivitoiminnot

## Suorittaminen

Tämä sovellus on JavaFX 11 -sovellus, joten JDK 11 tai 12 on oltava asennettuna. 
Lisää tietoa JavaFX 11- ja 12 -sovelluksista: https://openjfx.io/openjfx-docs/

Sovellus suoritetaan komennolla

```
mvn compile exec:java -Dexec.mainClass=fi.frt.MainApp
```

Jar-tiedosto generoidaan komennolla

```
mvn package
```

Jar-tiedosto löytyy _shade_ kansiosta. 
Pom.xml -tiedostosta voit muuttaa jar-tiedoston luomisessa käytettäviä asetuksia, 
kuten tallennuskansiota ja alustayhteensopivuuutta. Jar-tiedoston käynnistysluokaksi on
määritelty Launcher -luokka, mikä on vaatimus moduulittomalle ohjelmalle JavaFX 11 -versiossa.

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

