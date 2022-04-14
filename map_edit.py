import xml.etree.ElementTree as ET
import PySimpleGUI as SG
FilePath = 'B:/SteamLibrary/steamapps/common/Tasty Planet/assets/levels/bacteria.xml'
tree = ET.parse(FilePath)
root = tree.getroot()
window = SG.Window(title="Edible Editor", layout=[ [SG.Text('Rows of tiles to add (height)'), SG.Input()],
                                                  [SG.Text('Rows of tiles to add (width)'), SG.Input()],
                                                  [SG.Button('Set Size')]])
while True:
    event, values = window.read()
    if event == SG.WIN_CLOSED or event == 'Set Size':
        break
    print('you entered ', values[2])
window.close()
height = float(root.get('height')) + (int(values[0]) * 256)
width = float(root.get('width')) + (int(values[1]) * 256)

print(values[0])
print('height was: ' + root.get('height'))
root.set('height', str(height))
print('height is: ' + root.get('height'))
print('')

print(values[1])
print('width was: ' + root.get('width'))
root.set('width', str(width))
print('width is: ' + root.get('width'))

tree.write(FilePath)

