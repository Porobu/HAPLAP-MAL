package gal.arffEraiki.fitxategiak;

import gal.arffEraiki.ARFFEraikiKonstanteak;
import gal.arffEraiki.erlazioak.EHealthKDEntitatea;
import gal.arffEraiki.salbuespenak.EHealthKDEntitateaEzDaBaliozkoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class IdentifikazioAtaza {
    private final transient ArrayList<String> lerroak;
    private final Logger logger;
    //ID      START/END               LABEL           TEXT (optional)
    //1       3 7                     Concept         asma
    //2       15 25                   Concept         enfermedad
    private final ArrayList<EHealthKDEntitatea> entitateak;

    public IdentifikazioAtaza(ArrayList<String> lerroak) {
        this.lerroak = lerroak;
        this.logger = LogManager.getLogger();
        this.entitateak = new ArrayList<>();
    }

    public void entitateakLortu() throws EHealthKDEntitateaEzDaBaliozkoa {
        for (String lerroBat : this.lerroak) {
            String[] lauZatiak = lerroBat.split("\t");
            //FIXME Testua ez denez egon behar, momentuz hau beti gehitu
            if (lauZatiak.length == 3)
                lauZatiak = new String[]{lauZatiak[0], lauZatiak[1], lauZatiak[2], ARFFEraikiKonstanteak.ENTITATEAK_EZ_DU_TESTURIK};
            if (lauZatiak.length != 4)
                throw new EHealthKDEntitateaEzDaBaliozkoa("Ezin izan dira lau zutabeak lortu!");
            int id = Integer.parseInt(lauZatiak[0]);
            String hasieraAmaiera = lauZatiak[1];
            ArrayList<ArrayList<Integer>> hasieraAmaieraKudeatuta = this.hasieraAmaieraKudeatu(hasieraAmaiera);
            ArrayList<Integer> hasierak = hasieraAmaieraKudeatuta.get(0);
            ArrayList<Integer> amaierak = hasieraAmaieraKudeatuta.get(1);
            String etiketa = lauZatiak[2];
            String testua = lauZatiak[3];
            this.entitateak.add(new EHealthKDEntitatea(id, hasierak, amaierak, etiketa, testua));
        }
    }

    protected void entitateakBistaratu() {
        for (EHealthKDEntitatea eHealthKDEntitatea : this.entitateak)
            this.logger.debug(eHealthKDEntitatea.toString());
    }

    private ArrayList<ArrayList<Integer>> hasieraAmaieraKudeatu(String hasieraAmaieraBanatuGabe) {
        ArrayList<ArrayList<Integer>> emaitza = new ArrayList<>();
        String[] banatuak = hasieraAmaieraBanatuGabe.split(";");

        this.logger.trace("ZENBAKIAK: " + hasieraAmaieraBanatuGabe);
        this.logger.trace("ZENBAKI PARE KOP BANATU ETA GERO: " + banatuak.length);
        ArrayList<Integer> hasierakTemp = new ArrayList<>();
        ArrayList<Integer> amaierakTemp = new ArrayList<>();
        for (String hasieraAmaieraBat : banatuak) {
            String[] hasieraAmaieraBanatuBat = hasieraAmaieraBat.split(" ");
            hasierakTemp.add(Integer.valueOf(hasieraAmaieraBanatuBat[0]));
            amaierakTemp.add(Integer.valueOf(hasieraAmaieraBanatuBat[1]));

        }
        emaitza.add(hasierakTemp);
        emaitza.add(amaierakTemp);
        return emaitza;
    }

    public ArrayList<EHealthKDEntitatea> getEntitateak() {
        return this.entitateak;
    }
}
