# bigarren scripta
from scripts.ann2txt import main
import os
from pathlib import Path

dataset = "REL"
partiketa = "Dev"
path = '../data/BioNLP-ST_2011/' + dataset + "/" + partiketa + "/LerroJauziekin/"
irteera_path = '../../DDI-extraction-through-LSTM/Irteera/' \
               + '/' + dataset + '_Irteera/' + partiketa + '/' \
               + 'irteera_' + dataset + '-' + partiketa + '-'

fitxategiak = os.listdir(path)
testuak = []
for fitxategi_bat in fitxategiak:
    if ".txt" in fitxategi_bat:
        testuak.append(path + fitxategi_bat)

i = 0
for testu_bat in testuak:
    i += 1
    izenBerria = irteera_path + str(i) + ".txt"
    print(testu_bat)
    main(Path(testu_bat), Path(izenBerria))
