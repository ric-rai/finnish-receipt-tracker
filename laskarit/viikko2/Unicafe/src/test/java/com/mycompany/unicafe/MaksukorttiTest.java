package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti != null);
    }

    @Test
    public void saldoAlussaOikein(){
        assertTrue(kortti.saldo() == 10);
    }

    @Test
    public void lataaminenKasvattaaSaldoaOikein(){
        kortti.lataaRahaa(10);
        assertTrue(kortti.saldo() == 20);
    }

    @Test
    public void rahanOttaminenToimii(){
        Boolean riitti = kortti.otaRahaa(5);
        Boolean eiRiittanyt = kortti.otaRahaa(10);
        assertTrue(kortti.saldo() == 5);
        assertTrue(riitti);
        assertFalse(eiRiittanyt);
    }

    @Test
    public void toStringToimiiOikein(){
        assertTrue(kortti.toString().equals("saldo: " + (10/100) + "." + (10%100)));
    }
}
