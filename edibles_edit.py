import xml.etree.ElementTree as ET
import PySimpleGUI as SG
FilePath = 'B:/SteamLibrary/steamapps/common/Tasty Planet/assets/levels/bacteria.xml'
tree = ET.parse(FilePath)
root = tree.getroot()
window = SG.Window(title="Edible Editor", layout=[ [SG.Text('Element to edit'), SG.InputText()],
                                                  [SG.Text('Attribute to edit'), SG.InputText()],
                                                  [SG.Text('Value to set attribute to (Float)'), SG.InputText(default_text= ".0")],
                                                  [SG.Button('Set Values')]])
while True:
    event, values = window.read()
    if event == SG.WIN_CLOSED or event == 'Set Values':
        break
    print('you entered ', values[2])
window.close()

element = root[int(values[0])]
print('Attribute changed: ' + values[1])
print('Value before: ' + element.get(str(values[1])))
element.set(str(values[1]), values[2])
print('Value after: ' + element.get(str(values[1])))

tree.write(FilePath)

