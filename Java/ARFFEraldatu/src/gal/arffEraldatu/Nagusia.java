package gal.arffEraldatu;

public class Nagusia {

    public static void main(String[] args) throws Exception {
        ArgumentParser.getArgumentParser().argumentuakHartu(args);
        ABLSTMOperazioak ablstmOperazioak = new ABLSTMOperazioak();

        if (ArgumentParser.getArgumentParser().isPartiketaBitarraLortu())
            ablstmOperazioak.erlazioBitarrakLortu();

        if (ArgumentParser.getArgumentParser().getErlazioParaleloakPath() != null &&
                !ArgumentParser.getArgumentParser().getErlazioParaleloakPath().isEmpty()) {
            ablstmOperazioak.erlazioduneiKlaseOriginalaIpini();
        }

        if (ArgumentParser.getArgumentParser().isShuffle())
            ablstmOperazioak.randomize();

        if (ArgumentParser.getArgumentParser().getPortzentaiaInstantziak() != -1)
            ablstmOperazioak.klaseenGainekoHoldOut();

        if (ArgumentParser.getArgumentParser().isNoRelationEzabatu())
            ablstmOperazioak.noRelationEzabatu();
    }
}
