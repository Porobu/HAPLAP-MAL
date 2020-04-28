package main.java.gal.iragarpenakKudeatu;

import main.java.gal.iragarpenakKudeatu.salbuespenak.ErlazioParaleloaEzDaParaleloaSalbuespena;

public class Nagusia {
    public static void main(String[] args) throws ErlazioParaleloaEzDaParaleloaSalbuespena {
        ArgumentParser.getArgumentParser().argumentuakHartu(args);
        ABLSTMKudeatzailea ablstmKudeatzailea = new ABLSTMKudeatzailea();
        if (ArgumentParser.getArgumentParser().isIdGabekoakSortu()) {
            ablstmKudeatzailea.ablstmFitxategiBatKargatu();
            ablstmKudeatzailea.erlazioeiIDaKendu();
            ablstmKudeatzailea.idGabekoaGorde();
        } else if (ArgumentParser.getArgumentParser().iseHealthKD2019sortu()) {
            ablstmKudeatzailea.ablstmParaleloakKargatu();
            ablstmKudeatzailea.erlazioakBBihurtu();
            ablstmKudeatzailea.bLerroakGorde();
        } else if (ArgumentParser.getArgumentParser().isBratStandoffSortu()) {
            ablstmKudeatzailea.ablstmParaleloakKargatu();
            ablstmKudeatzailea.erlazioakBratBihurtu();
            ablstmKudeatzailea.standoffErlazioakGorde();
        } else if (ArgumentParser.getArgumentParser().isIragarpenBitarrakParalelizatu()) {
            ablstmKudeatzailea.ablstmParaleloakKargatu();
            ablstmKudeatzailea.norelationIragarpenakEzabatu();
        }
    }
}
