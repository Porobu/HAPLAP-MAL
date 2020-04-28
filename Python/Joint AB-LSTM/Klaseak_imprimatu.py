import re

path = "../Datuak/irteera_Joint_AB-LSTM_Train_IDGabe.txt"

# eHealthKD2020:
# {'in-context': ' ', 'domain': ' ', 'part-of': ' ', 'target': ' ', 'NO_RELATION': ' ',
# 'in-time': ' ', 'has-property': ' ', 'arg': ' ', 'is-a': ' ', 'in-place': ' ', 'same-as':
# ' ', 'entails': ' ', 'causes': ' ', 'subject': ' '}


f = open(path)

klaseak = {}
for lerro_bat in f:
    lerro_garbia = re.sub("[^A-Za-z0-9_\-]+", " ", lerro_bat.strip())
    if len(lerro_garbia.split(" ")) == 1 and len(lerro_garbia) >= 2:
        klaseak[lerro_garbia] = " "
print(klaseak)
