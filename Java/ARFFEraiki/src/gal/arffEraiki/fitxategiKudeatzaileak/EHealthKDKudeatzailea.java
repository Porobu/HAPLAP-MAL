package gal.arffEraiki.fitxategiKudeatzaileak;

import gal.arffEraiki.ArgumentParser;
import gal.arffEraiki.erlazioak.EHealthKDErlazioa;
import gal.arffEraiki.erlazioak.EntitateParea;
import gal.arffEraiki.fitxategiak.EHealthKDTestua;
import gal.arffEraiki.salbuespenak.EHealthKDEntitateaEzDaBaliozkoa;
import gal.arffEraiki.salbuespenak.EHealthKDErlazioaEzDaBaliozkoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


public class EHealthKDKudeatzailea {
    private static EHealthKDKudeatzailea eHealthKDKudeatzailea;

    static {
        EHealthKDKudeatzailea.eHealthKDKudeatzailea = new EHealthKDKudeatzailea();
    }

    private final Logger logger;
    private HashSet<EntitateParea> erlazionatutaDaudenak;
    private EHealthKDTestua eHealthKD;

    private EHealthKDKudeatzailea() {
        this.logger = LogManager.getLogger();

    }

    public static EHealthKDKudeatzailea geteHealthKDKudeatzailea() {
        return EHealthKDKudeatzailea.eHealthKDKudeatzailea;
    }

    public EHealthKDTestua geteHealthKD() {
        return this.eHealthKD;
    }

    public void fitxategiakKargatu() throws IOException, EHealthKDEntitateaEzDaBaliozkoa, EHealthKDErlazioaEzDaBaliozkoa {
        ArgumentParser argumentParser = ArgumentParser.getArgumentParser();
        String esaldiakPath = argumentParser.getEhealthKDFitxategia();
        String aAtazaPath = argumentParser.getaAtazaGoldStandard();
        String bAtazaPath = argumentParser.getbAtazaGoldStandard();

        ArrayList<String> trainLerroak = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(esaldiakPath))) {
            String lerroBat;
            while ((lerroBat = bufferedReader.readLine()) != null) {
                trainLerroak.add(lerroBat);
            }
        }

        ArrayList<String> aAtazaLerroak = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(aAtazaPath))) {
            String lerroBat;
            while ((lerroBat = bufferedReader.readLine()) != null) {
                aAtazaLerroak.add(lerroBat);
            }
        }

        ArrayList<String> bAtazaLerroak = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(bAtazaPath))) {
            String lerroBat;
            while ((lerroBat = bufferedReader.readLine()) != null) {
                bAtazaLerroak.add(lerroBat);
            }
        }
        this.eHealthKD = new EHealthKDTestua(trainLerroak, aAtazaLerroak, bAtazaLerroak);
    }

    //NO_RELATION egin bakarrik A -> B entitateen artean erlazio bat lortu bada
    public void erlazionatutaDaudenakLortu() {
        this.erlazionatutaDaudenak = new HashSet<>();
        for (EHealthKDErlazioa eHealthKDErlazioa : this.eHealthKD.getErlazioAtaza().getDaudenErlazioak()) {
            EntitateParea parea = new EntitateParea(eHealthKDErlazioa.getHasierakoEntitatea().getEtiketa(),
                    eHealthKDErlazioa.getAmaierakoEntitatea().getEtiketa());
            this.logger.debug("LORTU DEN PAREA: " + parea);

            boolean sartua = this.erlazionatutaDaudenak.add(parea);
            this.logger.debug("PAREA SARTU DA: " + sartua);
        }

        for (EntitateParea pareBat : this.erlazionatutaDaudenak) {
            this.logger.info("Erlazio parea: " + pareBat.getEzkerrekoEntitatea() + " -> " + pareBat.getEskumakoEntitatea());
        }
    }

    public HashSet<EntitateParea> getErlazionatutaDaudenak() {
        return this.erlazionatutaDaudenak;
    }
}
