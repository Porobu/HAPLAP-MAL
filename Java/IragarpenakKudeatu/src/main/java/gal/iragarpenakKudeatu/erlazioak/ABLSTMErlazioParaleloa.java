package main.java.gal.iragarpenakKudeatu.erlazioak;

import main.java.gal.iragarpenakKudeatu.IragarpenakKudeatuKonstanteak;

public class ABLSTMErlazioParaleloa {
    //Esaldia IDekin
    private final String testuaIDaDuena;
    //Entitateak
    private final String entitateakIDaDuena;
    //Erlazioa
    private final String erlazioaIDaDuena;
    //Esaldia ID gabe
    private final String testuaIDBarik;
    //Entitateak ID gabekoa kargatzen bada edo erlazio orgininala iragarpenak kargatzean
    private final String benetakoErlazioaIDBarik;
    //Erlazio originala ID gabekoak kargatzean edo iragarritako erlazioa iragarpenak kargatzean
    private final String iragarritakoErlazioaIDBarik;
    private String eHealthKDBihurtua;
    private String standoffBihurtua;

    public ABLSTMErlazioParaleloa(String testuaIDaDuena, String entitateakIDaDuena, String erlazioaIDaDuena,
                                  String testuaIDBarik, String benetakoErlazioaIDBarik, String iragarritakoErlazioaIDBarik) {
        this.testuaIDaDuena = testuaIDaDuena;
        this.entitateakIDaDuena = entitateakIDaDuena;
        this.erlazioaIDaDuena = erlazioaIDaDuena;
        this.testuaIDBarik = testuaIDBarik;
        this.benetakoErlazioaIDBarik = benetakoErlazioaIDBarik;
        this.iragarritakoErlazioaIDBarik = iragarritakoErlazioaIDBarik;
    }

    public void eHealthKDBihurtu() {
        this.eHealthKDBihurtua = this.iragarritakoErlazioaIDBarik + '\t';
        String[] id = this.erlazioanParteHartzenDutenEntitateenIDakLortu();
        this.eHealthKDBihurtua += id[0] + '\t' + id[1];
    }

    public String[] erlazioanParteHartzenDutenEntitateenIDakLortu() {
        String[] id = new String[2];
        for (String hitzBakarra : this.testuaIDaDuena.split(" ")) {
            if (hitzBakarra.contains(IragarpenakKudeatuKonstanteak.DRUG_A)) {
                String[] banatua = hitzBakarra.split(":");
                id[0] = banatua[1];
            }
            if (hitzBakarra.contains(IragarpenakKudeatuKonstanteak.DRUG_B)) {
                String[] banatua = hitzBakarra.split(":");
                id[1] = banatua[1];
            }
        }
        return id;
    }

    public void standoffErlazioaBihurtu() {
        //FORMATUA
        //RID	ERLAZIOA Arg1:TENTITATE1 Arg2:TENTITATE2
        //OHARRA: EZIN ZAIE ORAIN RID IPINI, EZ DAKIGU ZEIN ERLAZIO DEN HAU!
        this.standoffBihurtua = this.iragarritakoErlazioaIDBarik + ' ';
        String[] id = this.erlazioanParteHartzenDutenEntitateenIDakLortu();
        this.standoffBihurtua += "Arg1:T" + id[0] + " Arg2:T" + id[1];
    }

    public String geteHealthKDBihurtua() {
        return this.eHealthKDBihurtua;
    }

    public String getStandoffBihurtua() {
        return this.standoffBihurtua;
    }

    public String getIragarritakoErlazioaIDBarik() {
        return this.iragarritakoErlazioaIDBarik;
    }

    public String getEntitateakIDaDuena() {
        return this.entitateakIDaDuena;
    }

    public String getErlazioaIDaDuena() {
        return this.erlazioaIDaDuena;
    }

    public String getTestuaIDBarik() {
        return this.testuaIDBarik;
    }

    public String getBenetakoErlazioaIDBarik() {
        return this.benetakoErlazioaIDBarik;
    }

    public String getTestuaIDaDuena() {
        return this.testuaIDaDuena;
    }
}
