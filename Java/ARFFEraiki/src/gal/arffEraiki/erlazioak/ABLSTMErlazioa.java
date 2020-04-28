package gal.arffEraiki.erlazioak;

import gal.arffEraiki.ARFFEraikiKonstanteak;
import gal.arffEraiki.salbuespenak.EHealthKDErlazioaEzDaBaliozkoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ABLSTMErlazioa {
    private final Logger logger;
    private final EHealthKDErlazioa oraingoa;
    private String drugLerroa;
    private String entitateLerroa;
    private String erlazioLerroa;

    public ABLSTMErlazioa(EHealthKDErlazioa erlazioa) {
        this.logger = LogManager.getLogger();
        this.oraingoa = erlazioa;
    }
    //Formatua:
//    La DRUGA de las personas que lo DRUGB ha trabajado en lugares donde quizás inhalaron partículas de asbesto.
//    mayoría    Predicate    padece    Action
//    false

//FIXME ARAZOA
//    La DRUGA de las vitaminas B12 o B 6 puede causar anemia.
//    falta	Action	vitaminas B 6	Conc
//    target

    //Bigarren lerro honetan teorian ez da predicate izan behar, drug baizik.
    public String irteeraLortu() throws EHealthKDErlazioaEzDaBaliozkoa {
        //INPORTANTE BI LERRO BANATZEN!!!!!
        this.testuLerroaEraldatu();
        this.entitateLerroaLortu();
        this.erlazioLerroaLortu();
        return this.drugLerroa + ARFFEraikiKonstanteak.LERRO_BANATZAILEA +
                this.entitateLerroa + ARFFEraikiKonstanteak.LERRO_BANATZAILEA +
                this.erlazioLerroa + ARFFEraikiKonstanteak.LERRO_BANATZAILEA + ARFFEraikiKonstanteak.LERRO_BANATZAILEA;
    }

    private void testuLerroaEraldatu() throws EHealthKDErlazioaEzDaBaliozkoa {
        this.drugLerroa = this.oraingoa.getHasierakoEntitatea().getAgertzenDenEsaldia();
        String besteLerroa = this.oraingoa.getAmaierakoEntitatea().getAgertzenDenEsaldia();

        //TODO Hemen ez dago salbuespenik BioNLP ez apurtzeko
        if (!this.drugLerroa.equals(besteLerroa)) {
            this.logger.error("Erlazioek ez daukate lerro bera, hau ez da gordeko!");
            return;
        }

        this.logger.trace("LORTUTAKO TESTUA: " + this.drugLerroa);

        //TODO TRAMPA APUR BAT...
        this.drugLerroa = this.drugLerroa.replaceAll("&", ARFFEraikiKonstanteak.ZURIUNEA);
        this.drugLerroa = this.drugLerroa.replaceAll("!", ARFFEraikiKonstanteak.ZURIUNEA);

        int lerroHonenHasiera = this.oraingoa.getHasierakoEntitatea().getEsaldiarenHasieranEgonDirenKaraketereKopurua();
        this.logger.debug("AURREKO KARAKTEREAK: " + lerroHonenHasiera);
        char[] lerroa = this.drugLerroa.toCharArray();

        //FIXME EZIN DIRA KUDEATU HITZ ANITZEKOAK BANATUTA DAUDENEAN MOMENTUZ! (Bakarrik lehen hitza anotatu)
        //FIXME EZIN DA KUDEATU ENTITATEAK BERDINAK DIRENAREN KASUA!

        //REPLACE EGIN KARAKTEREZ KARAKTERE!
        //OHARRA! EZ ALDATU ESALDIAREN KARAKTERE KOPURUA!

        char[] drugACharArray = "&                                                                      ".toCharArray();
        char[] drugBCharArray = "!                                                                      ".toCharArray();
        try {
            //Hasierak DRUGArekin aldatu
            int lehenengoEntitatearenHasieraLerroHonetan = this.oraingoa.getHasierakoEntitatea().getHasierak().get(0) - lerroHonenHasiera;
            int lehenengoEntitatearenAmaieraLerroHonetan =
                    this.oraingoa.getHasierakoEntitatea().getAmaierak().get(this.oraingoa.getHasierakoEntitatea().getAmaierak().size() - 1) - lerroHonenHasiera;

            int drugXPos = 0;
            for (int j = lehenengoEntitatearenHasieraLerroHonetan; j < lehenengoEntitatearenAmaieraLerroHonetan; j++) {
                lerroa[j] = drugACharArray[drugXPos];
                drugXPos++;
            }

            //Amaierak DRUGBrekin aldatu
            int bigarrenEntitatearenHasieraLerroHonetan = this.oraingoa.getAmaierakoEntitatea().getHasierak().get(0) - lerroHonenHasiera;
            this.logger.debug("Bigarren entitatearen hasiera ");
            int bigarrenEntitatearenAmaieraLerroHonetan =
                    this.oraingoa.getAmaierakoEntitatea().getAmaierak().get(this.oraingoa.getAmaierakoEntitatea().getAmaierak().size() - 1) - lerroHonenHasiera;
            drugXPos = 0;
            for (int j = bigarrenEntitatearenHasieraLerroHonetan; j < bigarrenEntitatearenAmaieraLerroHonetan; j++) {
                lerroa[j] = drugBCharArray[drugXPos];
                drugXPos++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            this.logger.error("Ezin da erlazioa lortu, hau lerroen jauziak lortzean apurtu baita!");
            return;
        }

        this.drugLerroa = new String(lerroa);
        this.drugLerroa = this.drugLerroa.replace("&", ARFFEraikiKonstanteak.DRUG_A);
        this.drugLerroa = this.drugLerroa.replace("!", ARFFEraikiKonstanteak.DRUG_B);
        this.drugLerroa = this.drugLerroa.trim().replaceAll(" +", " ");
        this.logger.debug("LEHENENGO LERROA DRUGX IPINITA: " + this.drugLerroa);
    }

    private void entitateLerroaLortu() {
        String lehenengoEntitatearenTestua = this.oraingoa.getHasierakoEntitatea().getTestua();
        String lehenegoEntitatearenEtiketa = this.oraingoa.getHasierakoEntitatea().getEtiketa();
        String bigarrenEntitatearenTestua = this.oraingoa.getAmaierakoEntitatea().getTestua();
        String bigarrenEntitatearenEtiketa = this.oraingoa.getAmaierakoEntitatea().getEtiketa();
        this.entitateLerroa = lehenengoEntitatearenTestua + '\t' + lehenegoEntitatearenEtiketa + '\t' +
                bigarrenEntitatearenTestua + '\t' + bigarrenEntitatearenEtiketa;

    }

    private void erlazioLerroaLortu() {
        this.erlazioLerroa = this.oraingoa.getLabel();
    }
}
