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

public class mapEdit {

    public static void main(String[] args) {
        JPanel contentPanel = new JPanel();
        try {
            // Open file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
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

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton setSizeButton = new JButton("Set Size");

            JFrame frame = new JFrame("Map Size Editor");
            setSizeButton.addActionListener(e -> transformResult (heightPanel, widthPanel, root, doc, selectedFile, frame, contentPanel));
            buttonPanel.add(setSizeButton);
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.add(heightPanel);
            contentPanel.add(widthPanel);
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
    private static void transformResult(JPanel heightPanel, JPanel widthPanel, Element root, Document doc, File selectedFile, JFrame frame, JPanel contentPanel) {

        try {
            System.out.println("you entered " + ((JTextField) heightPanel.getComponent(1)).getText());
            float height = Float.parseFloat(root.getAttribute("height")) + (Integer.parseInt(((JTextField) heightPanel.getComponent(1)).getText()) * 256);
            float width = Float.parseFloat(root.getAttribute("width")) + (Integer.parseInt(((JTextField) widthPanel.getComponent(1)).getText()) * 256);
            System.out.println("height was: " + root.getAttribute("height"));
            root.setAttribute("height", String.valueOf(height));
            System.out.println("height is: " + root.getAttribute("height"));

            System.out.println("width was: " + root.getAttribute("width"));
            root.setAttribute("width", String.valueOf(width));
            System.out.println("width is: " + root.getAttribute("width"));


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result2 = new StreamResult(selectedFile);
            transformer.transform(source, result2);
            JOptionPane.showMessageDialog(frame, "Changes were successful.", "Changes Successful", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(contentPanel, "Something went wrong, sorry :" + ex.getMessage());
        }

    }
}