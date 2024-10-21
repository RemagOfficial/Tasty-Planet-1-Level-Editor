import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class mapEdit {

    /**
     * The main method of the program, this will open a file dialog for the user
     * to select a tasty planet level file, then it will open a new window with
     * input fields for the height and width of the new level size.
     * @param args The command line arguments (not used)
     */
    public static void main(String[] args) {
        JPanel contentPanel = new JPanel();
        try {
            // Open file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            // dev forced file path
            // fileChooser.setCurrentDirectory(new File("B:\\SteamLibrary\\steamapps\\common\\Tasty Planet\\assets\\levels"));
            //File filter
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
            fileChooser.setDialogTitle("Choose Tasty Planet level file (.XML)");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(contentPanel);
            if (result != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(contentPanel, "Something went wrong, sorry", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File selectedFile = fileChooser.getSelectedFile();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(selectedFile);
            Element root = doc.getDocumentElement();


            JPanel heightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            heightPanel.add(new JLabel("Rows of tiles to add (height)"));
            heightPanel.add(new JTextField(10));

            JPanel widthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            widthPanel.add(new JLabel("Rows of tiles to add (width)"));
            widthPanel.add(new JTextField(10));

            JPanel tileOverridePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tileOverridePanel.add(new JLabel("Override tile name (optional)"));
            tileOverridePanel.add(new JTextField(10));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton setSizeButton = new JButton("Set Size");

            JFrame frame = new JFrame("Map Size Editor");
            setSizeButton.addActionListener(e -> transformResult (heightPanel, widthPanel, tileOverridePanel, root, doc, selectedFile, frame, contentPanel));
            buttonPanel.add(setSizeButton);
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.add(heightPanel);
            contentPanel.add(widthPanel);
            contentPanel.add(tileOverridePanel);
            contentPanel.add(buttonPanel);

            frame.setContentPane(contentPanel);
            frame.setMinimumSize(new Dimension(400, 200));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(contentPanel, "Something is went wrong, sorry :" + ex.getMessage());
        }
    }
    /**
     * Called when the user hits the "Set Size" button. This method updates the Map.xml file
     * @param heightPanel The panel that contains the height input field
     * @param widthPanel The panel that contains the width input field
     * @param root The root element of the document
     * @param doc The entire document
     * @param selectedFile The file the user selected to modify
     * @param frame The frame that the content panel is in
     * @param contentPanel The panel that everything is in
     */
    private static void transformResult(JPanel heightPanel, JPanel widthPanel, JPanel tileOverridePanel, Element root, Document doc, File selectedFile, JFrame frame, JPanel contentPanel) {

        try {
            // log the user input
            System.out.println("you entered " + ((JTextField) widthPanel.getComponent(1)).getText() + " as the added width");
            System.out.println("you entered " + ((JTextField) heightPanel.getComponent(1)).getText() + " as the added height");
            // Add the height the user entered to the height attribute of the root element
            float height = Float.parseFloat(root.getAttribute("height")) + (Integer.parseInt(((JTextField) heightPanel.getComponent(1)).getText()) * 256);
            float width = Float.parseFloat(root.getAttribute("width")) + (Integer.parseInt(((JTextField) widthPanel.getComponent(1)).getText()) * 256);

            // Set the new height and width attributes on the root element
            System.out.println("height was: " + root.getAttribute("height"));
            root.setAttribute("height", String.valueOf(height));
            System.out.println("height is: " + root.getAttribute("height"));

            System.out.println("width was: " + root.getAttribute("width"));
            root.setAttribute("width", String.valueOf(width));
            System.out.println("width is: " + root.getAttribute("width"));

            // set the tileshigh and tileswide attributes on the tilelayer element to its current value plus the users input
            Element tilelayer = (Element) root.getElementsByTagName("tilelayer").item(0);
            tilelayer.setAttribute("tileshigh", String.valueOf(Integer.parseInt(tilelayer.getAttribute("tileshigh")) + Integer.parseInt(((JTextField) heightPanel.getComponent(1)).getText())));
            tilelayer.setAttribute("tileswide", String.valueOf(Integer.parseInt(tilelayer.getAttribute("tileswide")) + Integer.parseInt(((JTextField) widthPanel.getComponent(1)).getText())));

            // set the list of tile elements in the tilelayer element to the new tileshigh multiplied by the tileswide
            // use the first tile in the list as a template for the rest

            Element tile = (Element) tilelayer.getElementsByTagName("tile").item(0);
            NodeList tileList = tilelayer.getElementsByTagName("tile");

            // Remove existing tile elements and log when done
            while (tilelayer.hasChildNodes()) {
                Node child = tilelayer.getFirstChild();
                if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                    tilelayer.removeChild(child);
                } else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("tile")) {
                    tilelayer.removeChild(child);
                } else {
                    break;
                }
            }
            System.out.println("removed all tiles");

            for (int i = 0; i < Integer.parseInt(tilelayer.getAttribute("tileshigh")) * Integer.parseInt(tilelayer.getAttribute("tileswide")); i++) {
                Element newTile = doc.createElement("tile");
                // if the user entered an override, use that, otherwise use the name of the first tile
                if (!((JTextField) tileOverridePanel.getComponent(1)).getText().isEmpty()) {
                    System.out.println("overriding tile name: " + ((JTextField) tileOverridePanel.getComponent(1)).getText());
                    newTile.setAttribute("name", ((JTextField) tileOverridePanel.getComponent(1)).getText());
                } else {
                    System.out.println("using original tile name, no override set");
                    newTile.setAttribute("name", tile.getAttribute("name"));
                }
                // Add other attributes from the first element if needed
                tilelayer.appendChild(newTile);
                tilelayer.appendChild(doc.createTextNode("\n"));
                // log the new tile
                System.out.println("added tile: " + i);
            }

            // Create a Transformer object
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Set the output property to indent the XML nicely
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Create a DOM source from the document
            DOMSource source = new DOMSource(doc);
            // Create a StreamResult to write the transformed XML to
            StreamResult result2 = new StreamResult(selectedFile);
            // Transform the document
            transformer.transform(source, result2);
            // Show a message to the user to let them know the changes were successful
            JOptionPane.showMessageDialog(frame, "Changes were successful.", "Changes Successful", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            // Show an error message to the user if something goes wrong
            JOptionPane.showMessageDialog(contentPanel, "Something went wrong, sorry :" + ex.getMessage());
        }

    }
}