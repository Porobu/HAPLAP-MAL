# Lehenengo scripta
# Orain hau bakarrik erabili, besteak brat programan datozen scripteik egin dira:
# FIXME CATANN TXARTO!!!!!!!!!!!!!!!!!!!!!!
# 1: Merge.py
# 2: catann.py
# 3: LerroJauziakKonpondu.py


import os
import re
dataset = "REL"
partiketa = "Train"
path = '/home/sergio/Dropbox/PycharmProjects/ehealthkd-2019/data/BioNLP-ST_2011/' \
       + dataset + "/" + partiketa + "/"
path_irteera = path + 'LerroJauziekin/'

fitxategiak = os.listdir(path)

for fitxategi_bat in fitxategiak:
    if ".txt" in fitxategi_bat:
        fitxategia = open(path + fitxategi_bat, "r")
        testua = fitxategia.read()
        testua = re.sub("\\. \n", ". !", testua)
        testua_lerro_jauziekin = re.sub("\\. ", ".\n", testua)
        print(len(testua))
        print(len(testua_lerro_jauziekin))
        irteera = open(path_irteera + fitxategi_bat, "w")
        irteera.write(testua_lerro_jauziekin)
        irteera.close()
