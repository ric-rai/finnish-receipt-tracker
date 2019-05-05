# Käyttöohje

## Käyttöliittymän osat

#### Kuittitaulukko
Vasemmalla puolella on taulukossa lista kaikista järjestelmään syötetyistä kuiteista. Talukosta voidaan valita jokin kuitti, jolloin kuitin tiedot tulevat näkyviin käyttöliittymän keskellä olevaan osioon. Huomioithan, että kaikki tallentamattomat muutokset menetetään, kun listasta valitaan uusi kuitti.

#### Kuittitiedot
Keskellä olevassa osiosso näkyvät kuittitiedot. Keskellä oleva taulukko on ostostaulukko, jossa on kuitin sisältämät ostokset. 

#### Kuvaosio
Oikealla puolella on kuva-osio, jossa näkyy kuitista mahdollisesti tallennettu kuva. 

## Kuittitietojen käsittely

#### Uuden kuitin lisääminen
Valitsemalla _Tiedosto -> Uusi kuitti_ tai painamalla _Uusi kuitti_ -painiketta, voit aloittaa uuden kuitin tallentamisen. Ostopaíkan ja Ostajan nimi, sekä ostopäivä ovat pakollisia tietoja. Syötetyt tiedot tarkistetaan. Jos syöte ei ole kelvollinen, niin tekstikenttä muuttuu punaiseksi. Huomaathan, että kun painat tai valitset _Uusi kuitti_ aloittaaksesi uuden kuitin lisäämisen, niin kaikki tallentamattomat muutokset menetetään. Kuitin tietojen tallennus tapahtuu "_Tallenna_" -painikkeella.

#### Ostosten lisääminen
Kuittiin voi lisätä uuden ostosrivin klikkaamalla kahdesti viimeistä "_Lisää uusi..._" -riviä tia painamalla _Lisää ostos_ -painiketta. Tämän jälkeen voit klikata kahdesti ostosrivin soluja syöttääksesi niihin tarvittavat tiedot. Ostostyypin voit syöttää normaalisti Tyyppi-sarakkeeseen, tai sitten painaa Ehdotukset-sarakkeen ehdotuspainikkeita, jolloin Tyyppi-sarakkeeseen päivittyy painikkeen teksti. Tämä nopeuttaa kuittitietojen syöttämistä.
 
 Ostosten yhteissumma päivittyy ostostaulukon yläpuolelle "Yhteensä" -kohtaan. Ostosrivin voit poistaa jättämällä sen tiedot epäkelvoiksi. Huomioi siis, että epäkelpojen ostosrivien tietoja ei tallenneta kuittiin. Ostosten ja koko kuitin tallennus tapahtuu "_Tallenna_" -painikkeella.

#### Kuitin tietojen muuttaminen
Voi muuttaa kuitin tietoja valitsemalla sen ensin kuittitaulukosta ja sen jälkeen muuttamalla tietoja ja painamalla lopuksi "_Tallenna_" -painiketta.

#### Kuitin poistaminen
Valitsemalla _Muokkaa -> Poista kuitti_ voit poistaa valittuna olevan kuitin.

#### Kuvan lisääminen kuittiin
Voit lisätä kuitille kuvan "_Lisää kuva_" painikkella. Sovelluksen kuva-toiminnot vaativat internetyhteyden. Kuva tallennetaan tietokantaan painamalla "_Tallenna_" -paniketta.


## Sovelluksen konfigurointi

Toimiakseen ohjelma vaatii jar-tiedoston kanssa samaan kansioon sijoitetun application.properties -konfiguraatiotiedoston.

### Tietokannan konfigurointi
Sovellus käyttää Spring Framework -ohjelmistokehystä erityisesti tietokantatoimintojen suorittamiseen. Application.properties -tiedostossa voit määritellä tietokanta-asetukset seuraavasti:

<pre><code>spring.datasource.driverClassName=org.h2.Driver
->  Käytettävä tietokanta-ajuri

spring.datasource.url=jdbc:h2:./kuitit;
->  Tietokannan sijainti. Jos tietokantatiedostoa ei ole, 
    niin se luodaan ohjelman käynnistyessä.

spring.datasource.username=sa
spring.datasource.password=sa
->  Tietokannan käyttäjänimi ja salasana
</code></pre> 

### Tinify -avaimen konfigurointi

Sovellus käyttää Tinify nimistä Web-API:a kuvien kompressoimiseen. Määrittele avain application.properties -tiedostoon seuraavasti:

<pre><code>tinify-key=oSYQ7WOerulfGcerfPvHsF0msrOlE3gB</code></pre>

Lisää Tinify API:sta: https://github.com/tinify

### Ostostyyppiavainsanalistojen konfigurointi

Jotta sovellus osaisi ehdottaa oikeita ostostyyppejä syötetyille ostoksille, niin application.properties tiedostoon tulee määritellä erillisiä avainsanalistoja kutakin ostostyyppiä kohden. Sanalista on tekstitiedosto, jonka jokaisella rivillä on yksi ostostyyppiin liittyvä avainsana. Sanalistojen tulee sijaita samassa kansiossa kuin jar-tiedosto tai kansiossa, jonka jokin emokansio sijaitsee samassa kansiossa kuin jar-tiedosto. Määrittely tapahtuu esimerkiksi seuraavasti:

<pre><code>purchase-type.hedelmä.keywords=purchase-type-keywords/hedelmat.txt
->  Tämä määrittelee ostostyyppiin 'hedelmä' liittyvän sanalistan sijainnin.

purchase-type.vihannes.keywords=purchase-type-keywords/vihannekset.txt
purchase-type.makeinen.keywords=purchase-type-keywords/makeiset.txt
purchase-type.lisäravinne.keywords=purchase-type-keywords/lisaravinteet.txt
purchase-type.ruoka.keywords=purchase-type-keywords/ruoat.txt

purchase-type.ruoka.include=hedelmä, vihannes, makeinen, lisäravinne
->  Tämä määrittelee, että ostostyyppien hedelmä, vihannes, makeinen ja lisäravinne 
    avainsanat ovat myös ruoka -ostostyypin avainsanoja.
</code></pre>

