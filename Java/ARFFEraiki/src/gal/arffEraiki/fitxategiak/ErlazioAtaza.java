package gal.arffEraiki.fitxategiak;

import gal.arffEraiki.ARFFEraikiKonstanteak;
import gal.arffEraiki.erlazioak.EHealthKDEntitatea;
import gal.arffEraiki.erlazioak.EHealthKDErlazioa;
import gal.arffEraiki.erlazioak.EntitateParea;
import gal.arffEraiki.fitxategiKudeatzaileak.EHealthKDKudeatzailea;
import gal.arffEraiki.salbuespenak.EHealthKDErlazioaEzDaBaliozkoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ErlazioAtaza {
//    FORMATUA:
//    LABEL           SOURCE-ID       DEST-ID
//    subject         3               1
//    target          3               4
//    in-context      5               6

    private final transient ArrayList<String> lerroak;
    private final ArrayList<EHealthKDErlazioa> daudenErlazioak;
    private final Logger logger;

    public ErlazioAtaza(ArrayList<String> lerroak) {
        this.lerroak = lerroak;
        this.daudenErlazioak = new ArrayList<>();
        this.logger = LogManager.getLogger();
    }

    public void erlazioakLortu(ArrayList<EHealthKDEntitatea> entitateak) throws EHealthKDErlazioaEzDaBaliozkoa {
        for (String erlazioLerroa : this.lerroak) {
            if (!erlazioLerroa.trim().isEmpty()) {
                String[] banatuta = erlazioLerroa.split("\t");
                if (banatuta.length != 3)
                    throw new EHealthKDErlazioaEzDaBaliozkoa("Erlazioak ez ditu beharreko 3 zutabeak!");
                String erlazioarenEtiketa = banatuta[0];

                EHealthKDEntitatea ezkerrekoa = null;
                for (EHealthKDEntitatea entitatea : entitateak) {
                    if (entitatea.getId() == Integer.parseInt(banatuta[1])) {
                        ezkerrekoa = entitatea;
                        break;
                    }
                }

                EHealthKDEntitatea eskumakoa = null;
                for (EHealthKDEntitatea entitatea : entitateak) {
                    if (entitatea.getId() == Integer.parseInt(banatuta[2])) {
                        eskumakoa = entitatea;
                        break;
                    }
                }
                this.daudenErlazioak.add(new EHealthKDErlazioa(erlazioarenEtiketa, ezkerrekoa, eskumakoa));
            }
        }
    }

    protected void erlazioakPantailaratu() {
        for (EHealthKDErlazioa eHealthKDErlazioa : this.daudenErlazioak) {
            this.logger.debug(eHealthKDErlazioa.toString());
        }
    }

    public void erlazionatutaEzDaudenakLortu() {
        IdentifikazioAtaza identifikazioAtaza = EHealthKDKudeatzailea.geteHealthKDKudeatzailea().geteHealthKD().getIdentifikazioAtaza();
        for (EHealthKDEntitatea entitateaEzkerra : identifikazioAtaza.getEntitateak()) {
            for (EHealthKDEntitatea entitateaEskuma : identifikazioAtaza.getEntitateak()) {
                if (entitateaEzkerra != entitateaEskuma) {
                    if (entitateaEzkerra.getLerroZenbakia() == entitateaEskuma.getLerroZenbakia()) {
                        EntitateParea parea = new EntitateParea(entitateaEzkerra.getEtiketa(), entitateaEskuma.getEtiketa());
                        //FIXME HAU MOMENTUZ KOMENTATUTA, TEST BLINDEAN BESTELA EZ DU SORTZEN NO RELATION, HAU ARGUMENTPARSEREAN SARTU!
                        //if (EHealthKDKudeatzailea.geteHealthKDKudeatzailea().getErlazionatutaDaudenak().contains(parea)) {
                        this.logger.trace("PAREA DAGO: " + parea);
                        this.logger.trace("ENTITATEEN IDak: " + entitateaEzkerra.getId() + ", " + entitateaEskuma.getId());
                        EHealthKDErlazioa erlazioBerria = new EHealthKDErlazioa(ARFFEraikiKonstanteak.ERLAZIORIK_EZ, entitateaEzkerra, entitateaEskuma);
                        //Bilatu erlazio hau badago
                        boolean badago = false;
                        this.logger.trace("ERLAZIO BERRIA: " + erlazioBerria);
                        for (EHealthKDErlazioa dagoenErlazioa : this.daudenErlazioak) {
                            if (dagoenErlazioa.getHasierakoEntitatea().equals(erlazioBerria.getHasierakoEntitatea()) &&
                                    dagoenErlazioa.getAmaierakoEntitatea().equals(erlazioBerria.getAmaierakoEntitatea())) {
                                badago = true;
                                this.logger.trace("DAGOEN ERLAZIOA: " + dagoenErlazioa);
                                break;
                            }
                        }
                        if (!badago) {
                            this.logger.trace("LISTARA SARTUKO DEN ERLAZIOA: " + erlazioBerria);
                            this.daudenErlazioak.add(erlazioBerria);
                        }
                        //}
                    }
                }
            }
        }
    }

    public ArrayList<EHealthKDErlazioa> getDaudenErlazioak() {
        return this.daudenErlazioak;
    }
}
