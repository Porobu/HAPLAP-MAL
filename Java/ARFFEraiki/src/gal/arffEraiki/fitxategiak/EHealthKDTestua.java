package gal.arffEraiki.fitxategiak;

import gal.arffEraiki.ARFFEraikiKonstanteak;
import gal.arffEraiki.erlazioak.EHealthKDEntitatea;
import gal.arffEraiki.salbuespenak.EHealthKDEntitateaEzDaBaliozkoa;
import gal.arffEraiki.salbuespenak.EHealthKDErlazioaEzDaBaliozkoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class EHealthKDTestua {
    private final ArrayList<String> lerroak;
    private final IdentifikazioAtaza identifikazioAtaza;
    private final ErlazioAtaza erlazioAtaza;
    private final Logger logger;

    public EHealthKDTestua(ArrayList<String> lerroak, ArrayList<String> identifikazioLerroak,
                           ArrayList<String> erlazioLerroak) throws EHealthKDEntitateaEzDaBaliozkoa, EHealthKDErlazioaEzDaBaliozkoa {
        this.logger = LogManager.getLogger();
        this.lerroak = lerroak;
        this.identifikazioAtaza = new IdentifikazioAtaza(identifikazioLerroak);
        this.identifikazioAtaza.entitateakLortu();
        this.entitateakEsaldiekinLotu();
        if (this.logger.isDebugEnabled())
            this.identifikazioAtaza.entitateakBistaratu();

        this.erlazioAtaza = new ErlazioAtaza(erlazioLerroak);
        this.erlazioAtaza.erlazioakLortu(this.identifikazioAtaza.getEntitateak());
        if (this.logger.isDebugEnabled())
            this.erlazioAtaza.erlazioakPantailaratu();
    }

    public IdentifikazioAtaza getIdentifikazioAtaza() {
        return this.identifikazioAtaza;
    }

    public ErlazioAtaza getErlazioAtaza() {
        return this.erlazioAtaza;
    }

    private void entitateakEsaldiekinLotu() {
        ArrayList<EHealthKDEntitatea> entitateGuztiaK = this.identifikazioAtaza.getEntitateak();
        for (EHealthKDEntitatea eHealthKDEntitatea : entitateGuztiaK) {
            int kontatutakoKaraktereak = 0;
            int lerroZenbakia = 0;
            for (String esaldiBat : this.lerroak) {
                lerroZenbakia++;
                int hasiera = kontatutakoKaraktereak;
                kontatutakoKaraktereak += esaldiBat.toCharArray().length;
                // Lerro jauziak kontatu OSO INPORTANTE!!!!!!!!!!!!!!!!!!!!!!!
                kontatutakoKaraktereak++;

                boolean hitzakAurkituDira = true;
                String[] hitzak = eHealthKDEntitatea.getTestua().toLowerCase().split(" ");
                for (String hitzBat : hitzak) {
                    if (!esaldiBat.toLowerCase().contains(hitzBat)) {
                        hitzakAurkituDira = false;
                        break;
                    }
                }

                //FIXME ESALDIA HAU DELA SUPOSATUKO DUGU TESTURIK EZ BADAGO, ZENBAKIEN KONTAKETAK ONDO DOALA DIRUDI
                if (eHealthKDEntitatea.getTestua().equals(ARFFEraikiKonstanteak.ENTITATEAK_EZ_DU_TESTURIK))
                    hitzakAurkituDira = true;
                int amaieraPosizioa = eHealthKDEntitatea.getAmaierak().get(eHealthKDEntitatea.getAmaierak().size() - 1);
                if (kontatutakoKaraktereak >= amaieraPosizioa && hitzakAurkituDira) {
                    eHealthKDEntitatea.setAgertzenDenEsaldia(esaldiBat);
                    eHealthKDEntitatea.setEsaldiarenHasieranEgonDirenKaraketereKopurua(hasiera);
                    eHealthKDEntitatea.setLerroZenbakia(lerroZenbakia);
                    break;
                }

            }
        }
    }

}
