package gal.arffEraiki.erlazioak;

import java.util.Objects;

public class EHealthKDErlazioa {
    //    FORMATUA:
    //    LABEL           SOURCE-ID       DEST-ID
    //    subject         3               1
    //    target          3               4
    //    in-context      5               6
    private final String label;
    private final EHealthKDEntitatea hasierakoEntitatea;
    private final EHealthKDEntitatea amaierakoEntitatea;
    private final String errepresentazioa;

    public EHealthKDErlazioa(String label, EHealthKDEntitatea hasierakoEntitatea, EHealthKDEntitatea amaierakoEntitatea) {
        this.label = label;
        this.hasierakoEntitatea = hasierakoEntitatea;
        this.amaierakoEntitatea = amaierakoEntitatea;
        this.errepresentazioa = this.errepresentazioaLortu();
    }

    public String getLabel() {
        return this.label;
    }

    private String errepresentazioaLortu() {
        return "ETIKETA: " + this.label + " ENTITATE1: " + this.hasierakoEntitatea.getId() + ' ' + this.hasierakoEntitatea.getTestua() +
                " ENTITATE2: " + this.amaierakoEntitatea.getId() + ' ' + this.amaierakoEntitatea.getTestua();
    }

    @Override
    public String toString() {
        return this.errepresentazioa;
    }

    public EHealthKDEntitatea getHasierakoEntitatea() {
        return this.hasierakoEntitatea;
    }

    public EHealthKDEntitatea getAmaierakoEntitatea() {
        return this.amaierakoEntitatea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EHealthKDErlazioa)) return false;
        EHealthKDErlazioa erlazioa = (EHealthKDErlazioa) o;
        return this.label.equals(erlazioa.label) &&
                this.hasierakoEntitatea.getId() == erlazioa.hasierakoEntitatea.getId() &&
                this.amaierakoEntitatea.getId() == erlazioa.amaierakoEntitatea.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.label, this.hasierakoEntitatea, this.amaierakoEntitatea);
    }
}
