package gal.arffEraiki;

import gal.arffEraiki.fitxategiKudeatzaileak.ABLSTMIrteeraKudeatzailea;
import gal.arffEraiki.fitxategiKudeatzaileak.EHealthKDKudeatzailea;
import gal.arffEraiki.salbuespenak.EHealthKDEntitateaEzDaBaliozkoa;
import gal.arffEraiki.salbuespenak.EHealthKDErlazioaEzDaBaliozkoa;

import java.io.IOException;

public class Nagusia {
    public static void main(String[] args) throws EHealthKDErlazioaEzDaBaliozkoa, IOException, EHealthKDEntitateaEzDaBaliozkoa {
        ArgumentParser ap = ArgumentParser.getArgumentParser();
        ap.argumentuakHartu(args);

        EHealthKDKudeatzailea eHealthKDKudeatzailea = EHealthKDKudeatzailea.geteHealthKDKudeatzailea();
        eHealthKDKudeatzailea.fitxategiakKargatu();

        eHealthKDKudeatzailea.erlazionatutaDaudenakLortu();
        eHealthKDKudeatzailea.geteHealthKD().getErlazioAtaza().erlazionatutaEzDaudenakLortu();

        ABLSTMIrteeraKudeatzailea ablstmIrteeraKudeatzailea = new ABLSTMIrteeraKudeatzailea();
        ablstmIrteeraKudeatzailea.fitxategiaGorde();
    }
}
