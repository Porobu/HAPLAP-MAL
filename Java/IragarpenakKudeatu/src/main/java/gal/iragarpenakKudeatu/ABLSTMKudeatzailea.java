package main.java.gal.iragarpenakKudeatu;

import main.java.gal.iragarpenakKudeatu.erlazioak.ABLSTMErlazioParaleloa;
import main.java.gal.iragarpenakKudeatu.erlazioak.ABLSTMErlazioaBakarrik;
import main.java.gal.iragarpenakKudeatu.salbuespenak.ErlazioParaleloaEzDaParaleloaSalbuespena;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ABLSTMKudeatzailea {
    private final Logger logger;
    private final ArrayList<ABLSTMErlazioaBakarrik> erlazioLista;
    private final ArrayList<ABLSTMErlazioParaleloa> paraleloak;

    public ABLSTMKudeatzailea() {
        this.logger = LogManager.getLogger();
        this.erlazioLista = new ArrayList<>();
        this.paraleloak = new ArrayList<>();
    }

    private static ArrayList<String> lerroakLortu(String path) {
        ArrayList<String> lerroak = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String lerroa;
            while ((lerroa = bufferedReader.readLine()) != null)
                lerroak.add(lerroa);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //FIXME CHAPUZA
        Iterator<String> stringIterator = lerroak.iterator();
        String aurrekoa = stringIterator.next();
        while (stringIterator.hasNext()) {
            String oraingoa = stringIterator.next();
            if (aurrekoa.trim().isEmpty() && oraingoa.trim().isEmpty())
                stringIterator.remove();
            aurrekoa = oraingoa;
        }
        return lerroak;
    }

    public void ablstmFitxategiBatKargatu() {
        String path = ArgumentParser.getArgumentParser().getIdSarrera();
        ArrayList<String> lerroak = ABLSTMKudeatzailea.lerroakLortu(path);
        for (int i = 0; i < lerroak.size(); i++) {
            //Testua
            if (!lerroak.get(i).isEmpty()) {
                String testua = lerroak.get(i);
                this.logger.trace("TESTUA: " + testua);
                i++;
                String entitateak = lerroak.get(i);
                this.logger.trace("ENTITATEAK: " + entitateak);
                i++;
                String klasea = lerroak.get(i);
                this.logger.trace("KLASEA: " + klasea);
                this.erlazioLista.add(new ABLSTMErlazioaBakarrik(testua, entitateak, klasea));
            }
        }
    }

    public void erlazioeiIDaKendu() {
        for (ABLSTMErlazioaBakarrik ablstmErlazioaBakarrik : this.erlazioLista)
            ablstmErlazioaBakarrik.testuLerroanIDakKendu();
    }

    public void ablstmParaleloakKargatu() throws ErlazioParaleloaEzDaParaleloaSalbuespena {
        String pathHasierakoErlazioak = ArgumentParser.getArgumentParser().getIdSarrera();
        String pathIragarpenak = ArgumentParser.getArgumentParser().getIragarpenakSarrera();
        ArrayList<String> lerroakHasierakoak = ABLSTMKudeatzailea.lerroakLortu(pathHasierakoErlazioak);
        ArrayList<String> lerroakIragarpenak = ABLSTMKudeatzailea.lerroakLortu(pathIragarpenak);
        for (int i = 0; i < lerroakHasierakoak.size(); i++) {
            //Testua
            if (!lerroakHasierakoak.get(i).isEmpty()) {
                String testua = lerroakHasierakoak.get(i);
                this.logger.trace("TESTUA IDREKIN: " + testua);
                String testuaIragarpenak = lerroakIragarpenak.get(i);
                this.logger.trace("TESTUA IRAGRPENAK: " + testuaIragarpenak);
                i++;
                //FIXME PARALELIZATU!!!!!!!!!!!!!!!!
                //home/sergio/irteera_ABLSTM_Test.txt
                //9593 lerroak bi lerro jauzi
                String entitateak = lerroakHasierakoak.get(i);
                this.logger.trace("ENTITATEAK: " + entitateak);
                String benetakoErlazioa = lerroakIragarpenak.get(i);
                this.logger.trace("BENETAKO KLASEA: " + benetakoErlazioa);
                i++;

                String klasea = lerroakHasierakoak.get(i);
                this.logger.trace("KLASEA: " + klasea);
                String iragarpena = lerroakIragarpenak.get(i);
                this.logger.trace("IRAGARPENA: " + iragarpena);

                if (!benetakoErlazioa.equals(klasea))
                    throw new ErlazioParaleloaEzDaParaleloaSalbuespena("Erlazio paraleloek ez dute klase berdina!");
                this.paraleloak.add(new ABLSTMErlazioParaleloa(testua, entitateak, klasea,
                        testuaIragarpenak, benetakoErlazioa, iragarpena));
            }
        }
    }

    public void erlazioakBBihurtu() {
        for (ABLSTMErlazioParaleloa ablstmErlazioParaleloa : this.paraleloak) {
            ablstmErlazioParaleloa.eHealthKDBihurtu();
            this.logger.debug("B LERROA: " + ablstmErlazioParaleloa.geteHealthKDBihurtua());
        }
    }

    public void erlazioakBratBihurtu() {
        for (ABLSTMErlazioParaleloa ablstmErlazioParaleloa : this.paraleloak) {
            ablstmErlazioParaleloa.standoffErlazioaBihurtu();
            this.logger.debug("B LERROA: " + ablstmErlazioParaleloa.getStandoffBihurtua());
        }
    }

    public void bLerroakGorde() {
        String path = ArgumentParser.getArgumentParser().getIrteera();
        this.paraleloak.removeIf(oraingoa -> oraingoa.getIragarritakoErlazioaIDBarik().trim().equals(
                IragarpenakKudeatuKonstanteak.ERLAZIORIK_EZ));
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (ABLSTMErlazioParaleloa ablstmErlazioParaleloa : this.paraleloak) {
                bufferedWriter.write(ablstmErlazioParaleloa.geteHealthKDBihurtua());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void standoffErlazioakGorde() {
        String path = ArgumentParser.getArgumentParser().getIrteera();
        this.paraleloak.removeIf(oraingoa -> oraingoa.getIragarritakoErlazioaIDBarik().trim().equals(
                IragarpenakKudeatuKonstanteak.ERLAZIORIK_EZ));
        //FIXME IKUSI EA 0 EDO 1 DEN HASIERA!
        int i = 1;
        //FORMATUA
        //RID	ERLAZIOA Arg1:TENTITATE1 Arg2:TENTITATE2
        //OHARRA:RID HEMEN IPINI BEHAR ZAIE, ERLAZIO KLASEAN EZIN DA JAKIN ZEIN ID DIREN!
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (ABLSTMErlazioParaleloa ablstmErlazioParaleloa : this.paraleloak) {
                if (ablstmErlazioParaleloa.getIragarritakoErlazioaIDBarik().equalsIgnoreCase("same-as")) {
                    String[] idSameAs = ablstmErlazioParaleloa.erlazioanParteHartzenDutenEntitateenIDakLortu();
                    bufferedWriter.write("*\tsame-as T" + idSameAs[0] + " T" + idSameAs[1]);
                } else {
                    bufferedWriter.write("R" + i + '\t' + ablstmErlazioParaleloa.getStandoffBihurtua());
                    i++;
                }
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void idGabekoaGorde() {
        String path = ArgumentParser.getArgumentParser().getIrteera();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (ABLSTMErlazioaBakarrik ablstmErlazioaBakarrik : this.erlazioLista) {
                String testua = ablstmErlazioaBakarrik.getTestuLerroaIDGabe();
                String entitateak = ablstmErlazioaBakarrik.getEntitateLerroa();
                String erlazioa = ablstmErlazioaBakarrik.getErlazioLerroa();
                bufferedWriter.write(testua);
                bufferedWriter.newLine();
                bufferedWriter.write(entitateak);
                bufferedWriter.newLine();
                bufferedWriter.write(erlazioa);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void norelationIragarpenakEzabatu() {
        //Momentuz bakarrik gorde erlazioa dutenak, eta IDak berreskuratu!
        String path = ArgumentParser.getArgumentParser().getIrteera();
        this.paraleloak.removeIf(
                oraingoa -> oraingoa.getIragarritakoErlazioaIDBarik().trim().equalsIgnoreCase(IragarpenakKudeatuKonstanteak.ERLAZIORIK_EZ));
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (ABLSTMErlazioParaleloa erlazioa : this.paraleloak) {
                String testua = erlazioa.getTestuaIDaDuena();
                String entitateak = erlazioa.getEntitateakIDaDuena();
                String erlazioaString = erlazioa.getIragarritakoErlazioaIDBarik();
                bufferedWriter.write(testua);
                bufferedWriter.newLine();
                bufferedWriter.write(entitateak);
                bufferedWriter.newLine();
                bufferedWriter.write(erlazioaString);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
