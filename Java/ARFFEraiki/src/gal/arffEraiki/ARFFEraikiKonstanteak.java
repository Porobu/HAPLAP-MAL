package gal.arffEraiki;

import java.util.regex.Pattern;

public interface ARFFEraikiKonstanteak {
    String ENTITATEAK_EZ_DU_TESTURIK = "TESTURIK_EZ";

    //FIXME partxea Joint AB-LSTMan lerroak ondo banatzeko!
    String LERRO_BANATZAILEA = "\n";

    String ZURIUNEA = " ";

    //Honek aldatu daitezke, baina python main_train_val.py fitxategian ere aldatu behar dira!
    String DRUG_A = "DRUGA";
    String DRUG_B = "DRUGB";
    String ERLAZIORIK_EZ = "NO_RELATION";

    //Lerroak banatzeko pattern
    Pattern LERROAK_BANATU = Pattern.compile("\\. A-Z0-9");
}
