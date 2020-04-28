package gal.arffEraiki.erlazioak;

import gal.arffEraiki.ARFFEraikiKonstanteak;
import gal.arffEraiki.salbuespenak.EHealthKDEntitateaEzDaBaliozkoa;

import java.util.*;

public class EHealthKDEntitatea {
    //Honetan lerro bat sartuko da A fitxategitik
    //ID      START/END               LABEL           TEXT (optional)
    //1       3 7                     Concept         asma
    //2       15 25                   Concept         enfermedad
    private final int id;
    private final ArrayList<Integer> hasierak;
    private final ArrayList<Integer> amaierak;
    private final String etiketa;
    private final String testua;
    private String errepresentazioa;
    private String agertzenDenEsaldia;
    private int esaldiarenHasieranEgonDirenKaraketereKopurua;
    private int lerroZenbakia;

    public EHealthKDEntitatea(int id, ArrayList<Integer> hasierak, ArrayList<Integer> amaierak,
                              String etiketa, String testua) throws EHealthKDEntitateaEzDaBaliozkoa {
        if (hasierak.size() != amaierak.size())
            throw new EHealthKDEntitateaEzDaBaliozkoa("Hasiera eta amaiera posizio kopuruak ez dira berdinak!");
        this.id = id;
        this.hasierak = hasierak;
        this.amaierak = amaierak;
        this.etiketa = etiketa;
        this.lerroZenbakia = new Random().nextInt();
        //FIXME Momentuz testutik ez ipini, baina hau hutsik egon daiteke
        if (testua.isEmpty())
            this.testua = ARFFEraikiKonstanteak.ENTITATEAK_EZ_DU_TESTURIK;
        else
            this.testua = testua;
        this.errepresentazioa = this.toStringEraiki();
    }

    public int getLerroZenbakia() {
        return this.lerroZenbakia;
    }

    public void setLerroZenbakia(int lerroZenbakia) {
        this.lerroZenbakia = lerroZenbakia;
    }

    private String toStringEraiki() {
        StringBuilder luzera = new StringBuilder();
        luzera.append(" HASIERA-AMAIERAK: ");
        for (int i = 0; i < this.hasierak.size(); i++) {
            luzera.append(this.hasierak.get(i));
            luzera.append('-');
            luzera.append(this.amaierak.get(i));
            luzera.append(' ');
        }
        return "Entitatea: ID: " + this.id + luzera + "ETIKETA: " + this.etiketa +
                " TESTUA: " + this.testua + " ENTITATEAREN ESALDIA: " + this.agertzenDenEsaldia;
    }

    @Override
    public String toString() {
        return this.errepresentazioa;
    }

    public List<Integer> getAmaierak() {
        return Collections.unmodifiableList(this.amaierak);
    }

    public String getTestua() {
        return this.testua;
    }

    public int getId() {
        return this.id;
    }

    public String getEtiketa() {
        return this.etiketa;
    }

    public String getAgertzenDenEsaldia() {
        return this.agertzenDenEsaldia;
    }

    public int getEsaldiarenHasieranEgonDirenKaraketereKopurua() {
        return this.esaldiarenHasieranEgonDirenKaraketereKopurua;
    }

    public void setAgertzenDenEsaldia(String agertzenDenEsaldia) {
        this.agertzenDenEsaldia = agertzenDenEsaldia;
        this.errepresentazioa = this.toStringEraiki();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        EHealthKDEntitatea that = (EHealthKDEntitatea) o;
        return this.id == that.id &&
                this.hasierak.equals(that.hasierak) &&
                this.amaierak.equals(that.amaierak) &&
                this.etiketa.equals(that.etiketa) &&
                Objects.equals(this.testua, that.testua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.hasierak, this.amaierak, this.etiketa, this.testua);
    }

    public void setEsaldiarenHasieranEgonDirenKaraketereKopurua(int esaldiarenHasieranEgonDirenKaraketereKopurua) {
        this.esaldiarenHasieranEgonDirenKaraketereKopurua = esaldiarenHasieranEgonDirenKaraketereKopurua;
    }

    public ArrayList<Integer> getHasierak() {
        return this.hasierak;
    }



}
