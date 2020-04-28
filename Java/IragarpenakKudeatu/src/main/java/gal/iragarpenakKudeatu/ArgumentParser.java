package main.java.gal.iragarpenakKudeatu;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ArgumentParser {
    private static final ArgumentParser nireArgumentParser = new ArgumentParser();
    private final Logger logger;
    private String idSarrera;
    private String irteera;
    private String iragarpenakSarrera;
    private boolean idGabekoakSortu;
    private boolean eHealthKD2019sortu;
    private boolean bratStandoffSortu;
    private boolean iragarpenBitarrakParalelizatu;

    private ArgumentParser() {
        this.logger = LogManager.getLogger();
    }

    public static ArgumentParser getArgumentParser() {
        return ArgumentParser.nireArgumentParser;
    }

    public void argumentuakHartu(String[] args) {
        Options aukerak = new Options();

        Option sarrera = new Option("sid", "sarreraID", true,
                "AB LSTMarentzako IDak dituen sarrera fitxategia kargatzeko patha");
        sarrera.setRequired(false);
        aukerak.addOption(sarrera);

        Option iragarpenakSarrera = new Option("sir", "sarreraIragarpenak", true,
                "Joint AB-LSTMak lortutako iragarpen sarrera fitxategia kargatzeko patha");
        iragarpenakSarrera.setRequired(false);
        aukerak.addOption(iragarpenakSarrera);

        Option irteera = new Option("i", "irteera", true, "Irteera fitxategia gordetzeko patha");
        irteera.setRequired(true);
        aukerak.addOption(irteera);

        Option eHealthKD2019Lortu = new Option("e2019", "eHealthKD2019", false,
                "Irteera fitxategian eHealthKD 2019 erlazioen fitxategia lortu");
        eHealthKD2019Lortu.setRequired(false);
        aukerak.addOption(eHealthKD2019Lortu);

        Option bratStandoffLortu = new Option("brat", "bratStandoff", false,
                "Irteera fitxategian Brat Standoff erlazioen fitxategia lortu");
        bratStandoffLortu.setRequired(false);
        aukerak.addOption(bratStandoffLortu);

        Option iragarpenBitarrakParalelizatuAukera = new Option("p", "bitarrakparalelizatu", false,
                "Bitarren ebaluazioa egitean, iragarpen bitarrak paralelizatu IDdunei NO_RELATION iragarritakoak kenduz");
        iragarpenBitarrakParalelizatuAukera.setRequired(false);
        aukerak.addOption(iragarpenBitarrakParalelizatuAukera);

        Option idKendu = new Option("ik", "idKendu", false,
                "Joint AB-LSTM fitxategiari IDak kendu corpus paraleloa lortzeko");
        idKendu.setRequired(false);
        aukerak.addOption(idKendu);

        Option laguntza = new Option("h", "help", false, "Laguntza hau inprimatu");
        laguntza.setRequired(false);
        aukerak.addOption(laguntza);

        String header = "Programak Joint AB-LSTM entrenamendu edo iragarpen fitxategiak kudetazen ditu";
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(formatter.getWidth() * 2);
        CommandLine cmd = null;

        try {
            cmd = parser.parse(aukerak, args);
        } catch (ParseException e) {
            this.logger.fatal(e.getMessage());
            formatter.printHelp("java -jar IragarpenakKudeatu.jar", header, aukerak, "", true);
            System.exit(1);
        }

        if (cmd.hasOption("help")) {
            formatter.printHelp("java -jar IragarpenakKudeatu.jar", header, aukerak, "", true);
            System.exit(0);
        }

        this.logger.info("ARGUMENTUAK:");
        if (cmd.hasOption("e2019")) {
            this.logger.info("eHealthKD 2019 formatuan iragarpenak gordeko dira.");
            this.eHealthKD2019sortu = true;
            this.iragarpenakSarrera = cmd.getOptionValue("sarreraIragarpenak");
            this.logger.info("Iragarpenak kargatzeko patha: " + this.iragarpenakSarrera);
            this.idSarrera = cmd.getOptionValue("sarreraID");
            this.logger.info("IDa duten instantzien fitxategia kargatzeko patha: " + this.idSarrera);
            this.irteera = cmd.getOptionValue("irteera");
            this.logger.info("Iragarpenak eHealthKD 2019 formatuan gordetzeko patha: " + this.irteera);
        } else if (cmd.hasOption("brat")) {
            this.logger.info("Brat Standoff formatuan iragarpenak gordeko dira.");
            this.bratStandoffSortu = true;
            this.iragarpenakSarrera = cmd.getOptionValue("sarreraIragarpenak");
            this.logger.info("Iragarpenak kargatzeko patha: " + this.iragarpenakSarrera);
            this.idSarrera = cmd.getOptionValue("sarreraID");
            this.logger.info("IDa duten instantzien fitxategia kargatzeko patha: " + this.idSarrera);
            this.irteera = cmd.getOptionValue("irteera");
            this.logger.info("Iragarpenak Brat Standoff formatuan gordetzeko patha: " + this.irteera);
        } else if (cmd.hasOption("bitarrakparalelizatu")) {
            this.logger.info("Iragarpen bitarrei " + IragarpenakKudeatuKonstanteak.ERLAZIORIK_EZ +
                    " kenduko zaie, eta IDa duen test fitxategia paralelizatuko da");
            this.iragarpenBitarrakParalelizatu = true;
            this.idSarrera = cmd.getOptionValue("sarreraID");
            this.logger.info("IDa duten instantzien fitxategia kargatzeko patha: " + this.idSarrera);
            this.iragarpenakSarrera = cmd.getOptionValue("sarreraIragarpenak");
            this.logger.info("Iragarpen bitarrak kargatzeko patha: " + this.iragarpenakSarrera);
            this.irteera = cmd.getOptionValue("irteera");
            this.logger.info(IragarpenakKudeatuKonstanteak.ERLAZIORIK_EZ +
                    " kenduta dituzten iragarpenak gordetzeko patha: " + this.irteera);

        } else if (cmd.hasOption("idKendu")) {
            this.logger.info("Joint AB-LSTM formatuan dauden instantziei IDa kenduko zaie.");
            this.idGabekoakSortu = true;
            this.idSarrera = cmd.getOptionValue("sarreraID");
            this.logger.info("IDa duten instantzien fitxategia kargatzeko patha: " + this.idSarrera);
            this.irteera = cmd.getOptionValue("irteera");
            this.logger.info("ID gabeko instantziak gordetzeko patha: " + this.irteera);
        } else {
            this.logger.fatal("Programari ez zaio eman zer egin behar duen!");
            formatter.printHelp("java -jar IragarpenakKudeatu.jar", header, aukerak, "", true);
            System.exit(1);
        }


    }

    public String getIdSarrera() {
        return this.idSarrera;
    }

    public String getIrteera() {
        return this.irteera;
    }

    public String getIragarpenakSarrera() {
        return this.iragarpenakSarrera;
    }

    public boolean isIdGabekoakSortu() {
        return this.idGabekoakSortu;
    }

    public boolean iseHealthKD2019sortu() {
        return this.eHealthKD2019sortu;
    }

    public boolean isBratStandoffSortu() {
        return this.bratStandoffSortu;
    }

    public boolean isIragarpenBitarrakParalelizatu() {
        return this.iragarpenBitarrakParalelizatu;
    }
}
