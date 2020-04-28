# hirugarren scripta
import os

# TODO HAU EXEKUTATU!!!!!!!!!!!!!!!!!!!!!!!!!!!!
# Eginda: Epi: Dev200, Train600; GE Train908, Dev259; ID: Dev46 Train152;REL Train800 Dev150
dataset = "REL"
partiketa = "Dev"
jar_path = "../../../IdeaProjects/gal/out/artifacts/ARFFEraiki_jar/ARFFEraiki.jar"
erlazio_path = '../../DDI-extraction-through-LSTM/Irteera/' + dataset + '_Irteera/' + partiketa + '/'

izena_esaldiak_hasiera = erlazio_path + "irteera_" + dataset + "-" + partiketa + "-"
izena_a_hasiera = erlazio_path + "output_a_" + dataset + "-" + partiketa + "-"
izena_b_hasiera = erlazio_path + "output_b_" + dataset + "-" + partiketa + "-"

amaiera = ".txt"

irteera = erlazio_path + "Joint-ABLSTM_" + dataset + "_" + partiketa + "_IDarekin-"

for i in range(1, 151):
    print("\nFitxategia: " + str(i))
    os.system("java -jar " + jar_path + " -e " + izena_esaldiak_hasiera + str(i) + amaiera +
              " -ag " + izena_a_hasiera + str(i) + amaiera +
              " -bg " + izena_b_hasiera + str(i) + amaiera +
              " -i " + irteera + str(i) + ".txt")
