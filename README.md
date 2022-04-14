# Tasty Planet 1 Level Editor
a very basic level editor for the original tasty planet

# How To Use
To use the editor you will need to set the file path variable to the location of the level you want to edit, the only level i have tested with both scripts is the second level (bacteria.xml) so other level files may work but no support will be given by me as im not going the test the editor on every level until i understand the level format and xml editing in python more

The level editor is split into a few programs, the Edibles Editor lets you change the size of stuff that exists in the level and thats about it for now as xml sub-elements are confusing

The Map Editor lets you add rows of tiles to the height or width of the map, the script currently only adds the size of the tiles to the level size not the tiles list so that will have to be done manualy for now.

[How the level format works](https://tastyplanet.fandom.com/wiki/Tasty_Planet_1_Level_Format)

# Incase of file corruption

In the situation that the program messes up and crashes and the levels xml file is missing all its data you will need to redownload the level or replace the broken level
with a backup

# Requirements

To run the python file you will need [PySimpleGUI](https://pypi.org/project/PySimpleGUI/) installed as it is used for the GUI you will also need python but i shouldnt have to say that
