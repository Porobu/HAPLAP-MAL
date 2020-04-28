package gal.arffEraldatu;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class ArgumentParser {
    private static final ArgumentParser nireArgumentParser = new ArgumentParser();
    private final Logger logger;
    private String arffIdaztekoPath;
    private String arffIrakurtzekoPath;
    private int portzentaiaInstantziak;
    private boolean shuffle;
    private boolean partiketaBitarraLortu;
    private String erlazioParaleloakPath;
    private boolean noRelationEzabatu;

    private ArgumentParser() {
        this.logger = LogManager.getLogger();
    }

    public static ArgumentParser getArgumentParser() {
        return ArgumentParser.nireArgumentParser;
    }

    public void argumentuakHartu(String[] args) {
        Options aukerak = new Options();

        Option irteera = new Option("i", "irteera", true, "ABLSTM fitxategia gordetzeko karpeta");
        irteera.setRequired(true);
        aukerak.addOption(irteera);

        Option sarrera = new Option("s", "sarrera", true, "ABLSTM fitxategia kargatzeko patha");
        sarrera.setRequired(true);
        aukerak.addOption(sarrera);

        Option laguntza = new Option("h", "help", false, "Laguntza hau inprimatu");
        laguntza.setRequired(false);
        aukerak.addOption(laguntza);

        Option portzentaia = new Option("o", "hold-out", true,
                "Hold-out egin, emandako instantzien portzentaia train multzoan utziz");
        portzentaia.setRequired(false);
        aukerak.addOption(portzentaia);

        Option bitarra = new Option("b", "bitarra", false, "Partiketa bitarra lortuko da, "
                + ARFFEraldatuKonstanteak.RELATION + " eta " + ARFFEraldatuKonstanteak.NO_RELATION + " etiketak erabiliz");
        bitarra.setRequired(false);
        aukerak.addOption(bitarra);

        Option multiClass = new Option("m", "multiclass", true,
                "Multi-class erlazioak lortu iragarpen bitarretan fitxategi originaletik");
        multiClass.setRequired(false);
        aukerak.addOption(multiClass);

        Option shuffle = new Option("r", "randomize", false, "Instantziei Randomize iragazia aplikatu");
        shuffle.setRequired(false);
        aukerak.addOption(shuffle);

        Option noRelation = new Option("n", "norelationezabatu", false, "NO_RELATION Klaseak ezabatzen ditu sarreratik, bitarrak lortu eta gero sailkatzailea entrenatzeko");
        noRelation.setRequired(false);
        aukerak.addOption(noRelation);


        String header = "Programak Joint AB-LSTM erlazio fitxategiak eraldatzen ditu.";
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(formatter.getWidth() * 2);
        CommandLine cmd = null;

        try {
            cmd = parser.parse(aukerak, args);
        } catch (ParseException e) {
            this.logger.fatal(e.getMessage());
            formatter.printHelp("java -jar ArffEraldatu.jar", header, aukerak, "", true);
            System.exit(1);
        }

        if (cmd.hasOption("help")) {
            formatter.printHelp("java -jar ArffEraldatu.jar", header, aukerak, "", true);
            System.exit(0);
        }

        this.partiketaBitarraLortu = cmd.hasOption("bitarra");
        this.arffIdaztekoPath = cmd.getOptionValue("irteera");
        this.arffIrakurtzekoPath = cmd.getOptionValue("sarrera");
        this.noRelationEzabatu = cmd.hasOption("norelationezabatu");

        this.logger.info("ARGUMENTUAK:");
        this.logger.info("ABLSTM fitxategia irakurtzeko patha: " + this.arffIrakurtzekoPath);
        this.logger.info("ABLSTM fitxategia gordetzeko karpeta: " + this.arffIdaztekoPath);

        if (cmd.hasOption("multiclass") && this.partiketaBitarraLortu) {
            this.logger.fatal("Ezin da fitxategi berean partiketa bitarra lortu eta hau bitar bihurtu!");
            System.exit(2);
        } else if (cmd.hasOption("multiclass")) {
            this.erlazioParaleloakPath = cmd.getOptionValue("multiclass");
            this.logger.info("ABLSTM klase originalak dituen fitxategia: " + this.erlazioParaleloakPath);
            this.logger.info("Iragarpen bitarrei klase originala ipiniko zaie");
        } else if (this.partiketaBitarraLortu)
            this.logger.info("Partiketa bitarra lortuko da, "
                    + ARFFEraldatuKonstanteak.RELATION + " eta " + ARFFEraldatuKonstanteak.NO_RELATION + " etiketak erabiliz");

        this.portzentaiaInstantziak = -1;
        if (cmd.hasOption("hold-out")) {
            this.portzentaiaInstantziak = Integer.parseInt(cmd.getOptionValue("hold-out"));
            if (this.portzentaiaInstantziak < 1)
                this.portzentaiaInstantziak *= 100;

            if (this.portzentaiaInstantziak > 100) {
                this.logger.fatal("Ezin da banaketa egin!");
                System.exit(1);
            }
            this.logger.info("Instantzien %" + this.portzentaiaInstantziak + " geratuko da train multzoan");
        }

        this.shuffle = cmd.hasOption("randomize");
        if (this.shuffle) {
            this.logger.info("Instantziei Randomize filtroa aplikatuko zaie");
        }

        if (this.noRelationEzabatu)
            this.logger.info("NO_RELATION klaseak ezabatuko dira train datu sortatik sailkatzailea entrenatzeko");
    }

    public String getArffIdaztekoPath() {
        return this.arffIdaztekoPath;
    }

    public boolean isPartiketaBitarraLortu() {
        return this.partiketaBitarraLortu;
    }

    public String getArffIrakurtzekoPath() {
        return this.arffIrakurtzekoPath;
    }

    public int getPortzentaiaInstantziak() {
        return this.portzentaiaInstantziak;
    }

    public boolean isShuffle() {
        return this.shuffle;
    }

    public String getErlazioParaleloakPath() {
        return this.erlazioParaleloakPath;
    }

    public boolean isNoRelationEzabatu() {
        return this.noRelationEzabatu;
    }
}
