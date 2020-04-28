package gal.arffEraldatu;

public class Erlazioa {
    private final String lerroa;
    private final String entitateak;
    private String erlazioa;


    public Erlazioa(String lerroa, String entitateak, String erlazioa) {
        this.lerroa = lerroa;
        this.entitateak = entitateak;
        this.erlazioa = erlazioa;
    }

    public String toString() {
        return this.lerroa + ARFFEraldatuKonstanteak.LERRO_BANATZAILEA +
                this.entitateak + ARFFEraldatuKonstanteak.LERRO_BANATZAILEA +
                this.erlazioa + ARFFEraldatuKonstanteak.LERRO_BANATZAILEA +
                ARFFEraldatuKonstanteak.LERRO_BANATZAILEA;
    }

    public String getLerroa() {
        return this.lerroa;
    }

    public String getEntitateak() {
        return this.entitateak;
    }

    public String getErlazioa() {
        return this.erlazioa;
    }

    public void setErlazioa(String erlazioa) {
        this.erlazioa = erlazioa;
    }

}
