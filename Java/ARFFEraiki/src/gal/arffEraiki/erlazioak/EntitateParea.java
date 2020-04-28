package gal.arffEraiki.erlazioak;

public class EntitateParea {
    private final String ezkerrekoEntitatea;
    private final String eskumakoEntitatea;

    public EntitateParea(String ezkerrekoEntitatea, String eskumakoEntitatea) {
        this.ezkerrekoEntitatea = ezkerrekoEntitatea;
        this.eskumakoEntitatea = eskumakoEntitatea;
    }

    public String getEskumakoEntitatea() {
        return this.eskumakoEntitatea;
    }

    public String getEzkerrekoEntitatea() {
        return this.ezkerrekoEntitatea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        EntitateParea that = (EntitateParea) o;

        if (!this.ezkerrekoEntitatea.equals(that.ezkerrekoEntitatea)) return false;
        return this.eskumakoEntitatea.equals(that.eskumakoEntitatea);

    }

    @Override
    public int hashCode() {
        int result = this.ezkerrekoEntitatea.hashCode();
        result = 31 * result + this.eskumakoEntitatea.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.ezkerrekoEntitatea + " -> " + this.eskumakoEntitatea;
    }
}
