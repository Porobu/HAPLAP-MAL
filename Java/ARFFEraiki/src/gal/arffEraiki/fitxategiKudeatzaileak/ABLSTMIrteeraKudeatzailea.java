package gal.arffEraiki.fitxategiKudeatzaileak;

import gal.arffEraiki.ARFFEraikiKonstanteak;
import gal.arffEraiki.ArgumentParser;
import gal.arffEraiki.erlazioak.ABLSTMErlazioa;
import gal.arffEraiki.erlazioak.EHealthKDErlazioa;
import gal.arffEraiki.salbuespenak.EHealthKDErlazioaEzDaBaliozkoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ABLSTMIrteeraKudeatzailea {
    private final Logger logger;

    public ABLSTMIrteeraKudeatzailea() {
        this.logger = LogManager.getLogger();
    }

    public void fitxategiaGorde() throws IOException, EHealthKDErlazioaEzDaBaliozkoa {
        EHealthKDKudeatzailea eHealthKDKudeatzailea = EHealthKDKudeatzailea.geteHealthKDKudeatzailea();
        //Erlazio bakoitzeko...
        ArrayList<EHealthKDErlazioa> erlazioak = eHealthKDKudeatzailea.geteHealthKD().getErlazioAtaza().getDaudenErlazioak();
        StringBuilder gordetzeko = new StringBuilder(100000);

        for (EHealthKDErlazioa erlazioa : erlazioak) {
            ABLSTMErlazioa ablstmErlazioa = new ABLSTMErlazioa(erlazioa);
            String gorde = ablstmErlazioa.irteeraLortu();
            //Txarto egindako erlazioak ezabatu
            if (!gorde.contains(ARFFEraikiKonstanteak.DRUG_A) || !gorde.contains(ARFFEraikiKonstanteak.DRUG_B)) {
                this.logger.error("ERLAZIOA EZ DA BALIOZKOA ETA EZIN DA KONPONDU!" + ARFFEraikiKonstanteak.LERRO_BANATZAILEA + gorde);
                gorde = ARFFEraikiKonstanteak.LERRO_BANATZAILEA;
            }

            //TODO HAU LEKU HOBEAN IPINI!!!!!!!!!!!!!!
            gorde = gorde.replace(ARFFEraikiKonstanteak.DRUG_A,
                    ARFFEraikiKonstanteak.DRUG_A + ':' + erlazioa.getHasierakoEntitatea().getId());
            gorde = gorde.replace(ARFFEraikiKonstanteak.DRUG_B,
                    ARFFEraikiKonstanteak.DRUG_B + ':' + erlazioa.getAmaierakoEntitatea().getId());

            gordetzeko.append(gorde);
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(ArgumentParser.getArgumentParser().getAbLSTMIdaztekoPath())))) {
            bufferedWriter.write(gordetzeko.toString());
        }

    }
}
