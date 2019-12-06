/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.beans;

import es.ujaen.dae.ujabank.DTO.Conversion;
import es.ujaen.dae.ujabank.excepciones.RateNotAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author flo00008
 */
@Component
public class ConversorUJACoin {

    @Autowired
    RestTemplate rest;

    private final String url = "https://api.exchangeratesapi.io/latest?symbols={from}&base={to}";

    public float euroToUC() {
        String ujaCoin = "KRW";
        String base = "EUR";

        Conversion respuesta = rest.getForObject(this.url, Conversion.class, ujaCoin, base);

        if (respuesta == null) {
            throw new RateNotAvailable();
        }

        Float rate = respuesta.getRates().get(ujaCoin);

        if (rate == null) {
            throw new RateNotAvailable();
        }

        return rate;
    }

    public float UCToEuro() {
        String ujaCoin = "KRW";
        String base = "EUR";

        Conversion respuesta = rest.getForObject(this.url, Conversion.class, base, ujaCoin);

        if (respuesta == null) {
            throw new RateNotAvailable();
        }

        Float rate = respuesta.getRates().get(base);

        if (rate == null) {
            throw new RateNotAvailable();
        }

        return rate;
    }

}
