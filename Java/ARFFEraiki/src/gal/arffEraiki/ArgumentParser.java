package gal.arffEraiki;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ArgumentParser {
    private static final ArgumentParser nireArgumentParser = new ArgumentParser();
    private final Logger logger;
    private String abLSTMIdaztekoPath;
    private String ehealthKDFitxategia;
    private String aAtazaGoldStandard;
    private String bAtazaGoldStandard;
    private boolean lerroakBanatu;

    private ArgumentParser() {
        this.logger = LogManager.getLogger();
    }

    public static ArgumentParser getArgumentParser() {
        return ArgumentParser.nireArgumentParser;
    }

    public void argumentuakHartu(String[] args) {
        Options aukerak = new Options();

        Option irteera = new Option("i", "irteera", true, "Joint AB-LSTMarentzako sarrera fitxategia gordetzeko patha");
        irteera.setRequired(true);
        aukerak.addOption(irteera);

        Option laguntza = new Option("h", "help", false, "Laguntza hau inprimatu");
        laguntza.setRequired(false);
        aukerak.addOption(laguntza);

        Option ehealthKDFitxategia = new Option("e", "ehealthkdfitxategia", true, "EHeathKD entrenamendu fitxategiaren patha");
        ehealthKDFitxategia.setRequired(true);
        aukerak.addOption(ehealthKDFitxategia);

        Option aGoldStandard = new Option("ag", "identifikazioataza", true, "EHealthKDTestua identifikazio atazaren Gold Standarra");
        aGoldStandard.setRequired(true);
        aukerak.addOption(aGoldStandard);

        Option bGoldStandard = new Option("bg", "erlazioataza", true, "EHealthKDTestua erlazioen atazaren Gold Standarra");
        bGoldStandard.setRequired(true);
        aukerak.addOption(bGoldStandard);

        Option lerroakBanatu = new Option("l", "lerroakbanatu", false, "Lerroak banatu");
        lerroakBanatu.setRequired(false);
        aukerak.addOption(lerroakBanatu);


        String header = "Programak eHealthKD fitxategiak emanda, Joint AB-LSTMarentzako sarrera fitxategi bat sortzen du";
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(formatter.getWidth() * 2);
        CommandLine cmd = null;

        try {
            cmd = parser.parse(aukerak, args);
        } catch (ParseException e) {
            this.logger.fatal(e.getMessage());
            formatter.printHelp("java -jar ArffEraiki.jar", header, aukerak, "", true);
            System.exit(1);
        }

        if (cmd.hasOption("help")) {
            formatter.printHelp("java -jar ArffEraiki.jar", header, aukerak, "", true);
            System.exit(0);
        }

        this.abLSTMIdaztekoPath = cmd.getOptionValue("irteera");

        if (!cmd.hasOption("ehealthkdfitxategia")) {
            this.logger.fatal("Ez da EHealthKDTestua fitxategiaren patha lortu");
            System.exit(2);
        }


        this.ehealthKDFitxategia = cmd.getOptionValue("ehealthkdfitxategia");

        this.logger.info("ARGUMENTUAK:");
        this.logger.info("Joint AB-LSTM fitxategi sarrera gordetzeko patha: " + this.abLSTMIdaztekoPath);
        this.logger.info("EHealthKDTestua esaldiak kargatzeko patha: " + this.ehealthKDFitxategia);

        this.aAtazaGoldStandard = cmd.getOptionValue("identifikazioataza");
        this.logger.info("Identifikazio atazaren gold standard fitxategia kargatzeko patha: " + this.aAtazaGoldStandard);

        this.bAtazaGoldStandard = cmd.getOptionValue("erlazioataza");
        this.logger.info("Erlazio atazaren gold standard fitxategia kargatzeko patha: " + this.bAtazaGoldStandard);

    }


    public String getAbLSTMIdaztekoPath() {
        return this.abLSTMIdaztekoPath;
    }

    public String getEhealthKDFitxategia() {
        return this.ehealthKDFitxategia;
    }

    public String getaAtazaGoldStandard() {
        return this.aAtazaGoldStandard;
    }

    public String getbAtazaGoldStandard() {
        return this.bAtazaGoldStandard;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
