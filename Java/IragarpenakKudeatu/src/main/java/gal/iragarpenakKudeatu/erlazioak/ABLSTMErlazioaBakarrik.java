package main.java.gal.iragarpenakKudeatu.erlazioak;

import main.java.gal.iragarpenakKudeatu.IragarpenakKudeatuKonstanteak;

public class ABLSTMErlazioaBakarrik {
    private final String entitateLerroa;
    private final String erlazioLerroa;
    private final String testuLerroa;
    private String testuLerroaIDGabe;

    public ABLSTMErlazioaBakarrik(String testuLerroa, String entitateLerroa, String erlazioLerroa) {
        this.testuLerroa = testuLerroa;
        this.erlazioLerroa = erlazioLerroa;
        this.entitateLerroa = entitateLerroa;
        this.testuLerroaIDGabe = "";
    }

    public void testuLerroanIDakKendu() {
        String[] banatua = this.testuLerroa.split(" ");
        //TODO HAU FIJO REGEX BATEKIN EGIN DAITEKEELA
        for (String hitza : banatua) {
            if (hitza.contains(IragarpenakKudeatuKonstanteak.DRUG_A))
                hitza = IragarpenakKudeatuKonstanteak.DRUG_A;
            if (hitza.contains(IragarpenakKudeatuKonstanteak.DRUG_B))
                hitza = IragarpenakKudeatuKonstanteak.DRUG_B;
            this.testuLerroaIDGabe += hitza + ' ';
        }
    }

    public String getEntitateLerroa() {
        return this.entitateLerroa;
    }

    public String getErlazioLerroa() {
        return this.erlazioLerroa;
    }

    public String getTestuLerroaIDGabe() {
        return this.testuLerroaIDGabe;
    }
}
