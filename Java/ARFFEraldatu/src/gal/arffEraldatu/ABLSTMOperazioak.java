package gal.arffEraldatu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ABLSTMOperazioak {
    private static final Pattern arffIzenaAldatu = Pattern.compile(".txt", Pattern.LITERAL);
    private final Logger logger;
    private final ArrayList<Erlazioa> sarrera;
    private final ArrayList<Erlazioa> irteeraTestPartiketa;
    private final ArrayList<Erlazioa> irteeraTrainPartiketa;


    public ABLSTMOperazioak() {
        this.logger = LogManager.getLogger();
        this.sarrera = new ArrayList<>();
        this.irteeraTestPartiketa = new ArrayList<>();
        this.irteeraTrainPartiketa = new ArrayList<>();
    }

    private static void gorde(String amaiera, ArrayList<Erlazioa> lista) throws IOException {
        String karpeta = ArgumentParser.getArgumentParser().getArffIdaztekoPath();
        String fitxategiIzena = ArgumentParser.getArgumentParser().getArffIrakurtzekoPath();

        if (fitxategiIzena.contains(File.separator))
            fitxategiIzena = fitxategiIzena.substring(fitxategiIzena.lastIndexOf(File.separator));

        String trainPath = karpeta + ABLSTMOperazioak.arffIzenaAldatu.matcher(fitxategiIzena).replaceAll(Matcher.quoteReplacement(amaiera));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(trainPath)))) {
            for (Erlazioa gordetzeko : lista)
                bw.write(gordetzeko.toString());
        }
    }

    public void randomize() {
        Collections.shuffle(this.sarrera);
    }

    public void klaseenGainekoHoldOut() throws IOException {
        this.erlazioakKargatu(ArgumentParser.getArgumentParser().getArffIrakurtzekoPath());
        HashSet<String> klaseak = new HashSet<>();
        for (Erlazioa e : this.sarrera) {
            klaseak.add(e.getErlazioa());
        }
        this.logger.info("Lortu diren klase kopurua: " + klaseak.size());

        int trainTamaina = ArgumentParser.getArgumentParser().getPortzentaiaInstantziak();

        ArrayList<Erlazioa> gehitzeko = new ArrayList<>(this.sarrera.size());

        for (String erlazioMotaBat : klaseak) {
            for (Iterator<Erlazioa> iterator = this.sarrera.iterator(); iterator.hasNext(); ) {
                Erlazioa oraingoa = iterator.next();
                if (oraingoa.getErlazioa().equals(erlazioMotaBat)) {
                    gehitzeko.add(oraingoa);
                    iterator.remove();
                }
            }
            //Randomize klase guztien gainean egin!
            Collections.shuffle(gehitzeko);
            int zenbat = (gehitzeko.size() * trainTamaina / 100);
            for (int i = 0; i < zenbat; i++)
                this.irteeraTrainPartiketa.add(gehitzeko.remove(0));

            this.irteeraTestPartiketa.addAll(gehitzeko);
            gehitzeko.clear();
        }

        ABLSTMOperazioak.gorde("_train.txt", this.irteeraTrainPartiketa);
        ABLSTMOperazioak.gorde("_test.txt", this.irteeraTestPartiketa);
    }


    @SuppressWarnings("AssignmentToForLoopParameter")
    private void erlazioakKargatu(String path) throws IOException {
        ArrayList<String> lerroak = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String lerroBat;
            while ((lerroBat = bufferedReader.readLine()) != null) {
                lerroak.add(lerroBat);
            }
        }
        for (int i = 0; i < lerroak.size(); i++) {
            if (!lerroak.get(i).isEmpty()) {
                String lerroa = lerroak.get(i);
                i++;
                String entitateak = lerroak.get(i);
                i++;
                String erlazioa = lerroak.get(i);
                i++;
                Erlazioa gordetzeko = new Erlazioa(lerroa, entitateak, erlazioa);
                this.logger.debug("LORTUTAKO ERLAZIOA:" + ARFFEraldatuKonstanteak.LERRO_BANATZAILEA + gordetzeko);
                this.sarrera.add(gordetzeko);
            }
        }

    }

    public void erlazioBitarrakLortu() throws IOException {
        this.erlazioakKargatu(ArgumentParser.getArgumentParser().getArffIrakurtzekoPath());
        ArrayList<Erlazioa> bitarrak = new ArrayList<>();
        for (Erlazioa erlazioBat : this.sarrera) {
            String erlazioBerria = erlazioBat.getErlazioa().equalsIgnoreCase(ARFFEraldatuKonstanteak.NO_RELATION)
                    ? ARFFEraldatuKonstanteak.NO_RELATION : ARFFEraldatuKonstanteak.RELATION;
            Erlazioa bitarra = new Erlazioa(erlazioBat.getLerroa(), erlazioBat.getEntitateak(), erlazioBerria);
            bitarrak.add(bitarra);
        }
        ABLSTMOperazioak.gorde("_bitarrak.txt", bitarrak);
    }

    public void erlazioduneiKlaseOriginalaIpini() throws IOException {
        //Sarrera -> Iragarpenak
        this.erlazioakKargatu(ArgumentParser.getArgumentParser().getArffIrakurtzekoPath());
        ArrayList<Erlazioa> iragarpenak = new ArrayList<>(this.sarrera);
        this.sarrera.clear();
        //Paraleloak -> Multiclass
        this.erlazioakKargatu(ArgumentParser.getArgumentParser().getErlazioParaleloakPath());
        ArrayList<Erlazioa> originalak = new ArrayList<>(this.sarrera);
        this.sarrera.clear();
        if (iragarpenak.size() != originalak.size()) {
            this.logger.fatal("Bi sarrera fitxategiak ez daukate tamaina bera, ez dira paraleloak!");
            System.exit(1);
        }
        ArrayList<Erlazioa> ebaluatzeko = new ArrayList<>();
        ArrayList<Erlazioa> erroreak = new ArrayList<>();
        for (int i = 0; i < originalak.size(); i++) {
            //Oharra, iragarpenentan:
            //getErlazioa: iragarpena
            //getEntitateak: erlazio originala
            if (iragarpenak.get(i).getErlazioa().trim().equals(ARFFEraldatuKonstanteak.RELATION)) {
                Erlazioa originala = originalak.get(i);
                this.logger.debug("IRAGARPEN ERLAZIOA: " + iragarpenak.get(i));
                this.logger.debug("ERLAZIO ORIGINALA: " + originala);

                if (originala.getErlazioa().equals(ARFFEraldatuKonstanteak.NO_RELATION)) {
                    //Erroredunak iragarpen formatuan gorde erroreak era bisualean konparatzeko
                    erroreak.add(new Erlazioa(originala.getLerroa(), originala.getErlazioa() + ", " + originala.getEntitateak(), iragarpenak.get(i).getErlazioa()));
                } else
                    //Ebaluatzeko datu sorta Joint AB-LSTM formatuan gorde behar da
                    ebaluatzeko.add(originala);
            }

        }
        ABLSTMOperazioak.gorde("_dev_no_relation_gabe.txt", ebaluatzeko);
        ABLSTMOperazioak.gorde("_erroreak_iragarpenak.txt", erroreak);

    }

    public void noRelationEzabatu() throws IOException {
        this.erlazioakKargatu(ArgumentParser.getArgumentParser().getArffIrakurtzekoPath());
        this.sarrera.removeIf(oraingoa -> oraingoa.getErlazioa().trim().equalsIgnoreCase("NO_RELATION"));
        ABLSTMOperazioak.gorde("_NO_RELATION_gabe.txt", this.sarrera);

    }
}
