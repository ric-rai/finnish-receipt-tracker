# Kuittiseuranta

Sovellus auttaa käyttäjää seuraamaan (suomalaisia) ostokuitteja, sekä pitämään niistä kirjaa.

## Dokumentaatio

[Vaatimusmäärittely](./dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](./dokumentaatio/arkkitehtuuri.md)

[Työaikakirjanpito](./dokumentaatio/tuntikirjanpito.md)

## Releases

[Viikko 5](https://github.com/ric-rai/finnish-receipt-tracker/releases/tag/viikko5)

## Komentorivitoiminnot

## Suorittaminen

Sovellus suoritetaan komennolla

```
mvn compile exec:java -Dexec.mainClass=fi.frt.MainApp
```

Jar-tiedosto generoidaan komennolla

```
mvn package
```

Jar-tiedosto löytyy _shade_ kansiosta

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

