# laugarren scripta

import os

dataset = "REL"
partiketa = "Dev"
osoa = "Joint_AB-LSTM_" + dataset + "_" + partiketa + "_osoa.txt"
erlazio_path = '../../DDI-extraction-through-LSTM/Irteera/' + dataset + '_Irteera/' + partiketa + '/'

fitxategiak = os.listdir(erlazio_path)

batzeko = []
for fitxategi_bat in fitxategiak:
    if "Joint-ABLSTM_" + dataset + "_" + partiketa + "_IDarekin-" in fitxategi_bat:
        batzeko.append(fitxategi_bat)

testua = ""

for testu_bat in batzeko:
    testu_osoa = open(erlazio_path + "/" + testu_bat, "r")
    testua = testua + testu_osoa.read()

irteera = open(erlazio_path + "/" + osoa, "w")
irteera.write(testua)
irteera.close()
