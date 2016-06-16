package latcyrconverter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class LatCyrConverterController implements Initializable {
    public static final String FONT = "/resources/font/arial.ttf";
    @FXML
    private TextArea txtInput;
    @FXML
    private TextArea txtOutput;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnChoose;
    @FXML
    private MenuItem miToTxt;
    @FXML
    private MenuItem miToDocx;
    @FXML
    private MenuItem miToPdf;
    @FXML
    private MenuItem cyrillic;
    @FXML
    private MenuItem latin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void convertToHandle(ActionEvent event) {
        String text = this.txtInput.getText();
        MenuItem source = (MenuItem)event.getSource();
        switch (source.getId()) {
            case "cyrillic": {
                Pattern patternLatin = Pattern.compile("[\\p{InBasicLatin}\\s+\\p{Punct}]");
                Matcher matcherLatin = patternLatin.matcher(text);
                if (!matcherLatin.find()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("The are some character in text which aren't Serbian Latin try again!");
                    alert.showAndWait();
                    break;
                }
                String textCyr = this.convertToCyrillic(text);
                this.txtOutput.setText(textCyr);
                break;
            }
            case "latin": {
                Pattern patternCyrillic = Pattern.compile("[\\p{InCyrillic}\\s+\\p{Punct}]");
                Matcher matcherCyrillic = patternCyrillic.matcher(text);
                if (!matcherCyrillic.find()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("The are some character in text which aren't Serbian Cyrillic try again!!");
                    alert.showAndWait();
                    break;
                }
                String textLat = this.convertToLatin(text);
                this.txtOutput.setText(textLat);
            }
        }
    }

    @FXML
    private void deleteHandle(ActionEvent event) {
        this.txtOutput.clear();
        this.txtInput.clear();
    }

    private String convertToLatin(String text) {
        String textLat = text.replaceAll("\u0430", "a");
        textLat = textLat.replaceAll("\u0431", "b");
        textLat = textLat.replaceAll("\u0432", "v");
        textLat = textLat.replaceAll("\u0433", "g");
        textLat = textLat.replaceAll("\u0434", "d");
        textLat = textLat.replaceAll("\u0452", "dj");
        textLat = textLat.replaceAll("e", "e");
        textLat = textLat.replaceAll("\u0436", "\u017e");
        textLat = textLat.replaceAll("\u0437", "z");
        textLat = textLat.replaceAll("\u0438", "i");
        textLat = textLat.replaceAll("\u0458", "j");
        textLat = textLat.replaceAll("k", "dj");
        textLat = textLat.replaceAll("\u043b", "l");
        textLat = textLat.replaceAll("\u0459", "lj");
        textLat = textLat.replaceAll("\u043c", "m");
        textLat = textLat.replaceAll("\u043d", "n");
        textLat = textLat.replaceAll("\u045a", "nj");
        textLat = textLat.replaceAll("\u043e", "o");
        textLat = textLat.replaceAll("\u043f", "p");
        textLat = textLat.replaceAll("\u0440", "r");
        textLat = textLat.replaceAll("\u0441", "s");
        textLat = textLat.replaceAll("\u0442", "t");
        textLat = textLat.replaceAll("\u045b", "\u0107");
        textLat = textLat.replaceAll("\u0443", "u");
        textLat = textLat.replaceAll("\u0444", "f");
        textLat = textLat.replaceAll("\u0445", "h");
        textLat = textLat.replaceAll("\u0446", "c");
        textLat = textLat.replaceAll("\u0447", "\u010d");
        textLat = textLat.replaceAll("\u045f", "d\u017e");
        textLat = textLat.replaceAll("\u0448", "\u0161");
        textLat = textLat.replaceAll("\u0410", "A");
        textLat = textLat.replaceAll("\u0411", "B");
        textLat = textLat.replaceAll("\u0412", "V");
        textLat = textLat.replaceAll("\u0413", "G");
        textLat = textLat.replaceAll("\u0414", "D");
        textLat = textLat.replaceAll("\u0402", "\u0110");
        textLat = textLat.replaceAll("\u0415", "E");
        textLat = textLat.replaceAll("\u0416", "\u017d");
        textLat = textLat.replaceAll("\u0417", "Z");
        textLat = textLat.replaceAll("\u0418", "I");
        textLat = textLat.replaceAll("\u0408", "J");
        textLat = textLat.replaceAll("\u041a", "K");
        textLat = textLat.replaceAll("\u041b", "L");
        textLat = textLat.replaceAll("\u0409", "Lj");
        textLat = textLat.replaceAll("\u041c", "M");
        textLat = textLat.replaceAll("\u041d", "N");
        textLat = textLat.replaceAll("\u040a", "Nj");
        textLat = textLat.replaceAll("\u041e", "O");
        textLat = textLat.replaceAll("\u041f", "P");
        textLat = textLat.replaceAll("\u0420", "R");
        textLat = textLat.replaceAll("\u0421", "S");
        textLat = textLat.replaceAll("\u0422", "T");
        textLat = textLat.replaceAll("\u040b", "\u0106");
        textLat = textLat.replaceAll("\u0423", "U");
        textLat = textLat.replaceAll("\u0424", "F");
        textLat = textLat.replaceAll("\u0425", "H");
        textLat = textLat.replaceAll("\u0426", "C");
        textLat = textLat.replaceAll("\u0427", "\u010c");
        textLat = textLat.replaceAll("\u040f", "D\u017e");
        textLat = textLat.replaceAll("\u0428", "\u0160");
        return textLat;
    }

    private String convertToCyrillic(String text) {
        String textLat = text.replaceAll("\u0111", "\u0452");
        textLat = textLat.replaceAll("nj", "\u045a");
        textLat = textLat.replaceAll("lj", "\u0459");
        textLat = textLat.replaceAll("d\u017e", "\u045f");
        textLat = textLat.replaceAll("a", "\u0430");
        textLat = textLat.replaceAll("b", "\u0431");
        textLat = textLat.replaceAll("v", "\u0432");
        textLat = textLat.replaceAll("g", "\u0433");
        textLat = textLat.replaceAll("d", "\u0434");
        textLat = textLat.replaceAll("e", "\u0435");
        textLat = textLat.replaceAll("\u017e", "\u0436");
        textLat = textLat.replaceAll("z", "\u0437");
        textLat = textLat.replaceAll("i", "\u0438");
        textLat = textLat.replaceAll("j", "\u0458");
        textLat = textLat.replaceAll("k", "\u043a");
        textLat = textLat.replaceAll("l", "\u043b");
        textLat = textLat.replaceAll("m", "\u043c");
        textLat = textLat.replaceAll("n", "\u043d");
        textLat = textLat.replaceAll("o", "\u043e");
        textLat = textLat.replaceAll("p", "\u043f");
        textLat = textLat.replaceAll("r", "\u0440");
        textLat = textLat.replaceAll("s", "\u0441");
        textLat = textLat.replaceAll("t", "\u0442");
        textLat = textLat.replaceAll("\u0107", "\u045b");
        textLat = textLat.replaceAll("u", "\u0443");
        textLat = textLat.replaceAll("f", "\u0444");
        textLat = textLat.replaceAll("h", "\u0445");
        textLat = textLat.replaceAll("c", "\u0446");
        textLat = textLat.replaceAll("\u010d", "\u0447");
        textLat = textLat.replaceAll("\u0161", "\u0448");
        textLat = textLat.replaceAll("D\u017e", "\u040f");
        textLat = textLat.replaceAll("\u0110", "\u0402");
        textLat = textLat.replaceAll("Lj", "\u0409");
        textLat = textLat.replaceAll("Nj", "\u040a");
        textLat = textLat.replaceAll("A", "\u0410");
        textLat = textLat.replaceAll("B", "\u0411");
        textLat = textLat.replaceAll("V", "\u0412");
        textLat = textLat.replaceAll("G", "\u0413");
        textLat = textLat.replaceAll("D", "\u0414");
        textLat = textLat.replaceAll("E", "\u0415");
        textLat = textLat.replaceAll("\u017d", "\u0416");
        textLat = textLat.replaceAll("Z", "\u0417");
        textLat = textLat.replaceAll("I", "\u0418");
        textLat = textLat.replaceAll("J", "\u0408");
        textLat = textLat.replaceAll("K", "\u041a");
        textLat = textLat.replaceAll("L", "\u041b");
        textLat = textLat.replaceAll("M", "\u041c");
        textLat = textLat.replaceAll("N", "\u041d");
        textLat = textLat.replaceAll("P", "\u041f");
        textLat = textLat.replaceAll("R", "\u0420");
        textLat = textLat.replaceAll("S", "\u0421");
        textLat = textLat.replaceAll("T", "\u0422");
        textLat = textLat.replaceAll("\u0106", "\u040b");
        textLat = textLat.replaceAll("U", "\u0423");
        textLat = textLat.replaceAll("F", "\u0424");
        textLat = textLat.replaceAll("H", "\u0425");
        textLat = textLat.replaceAll("C", "\u0426");
        textLat = textLat.replaceAll("\u010c", "\u0427");
        textLat = textLat.replaceAll("\u0160", "\u0428");
        return textLat;
    }

    @FXML
    private void chosseHandle(ActionEvent event) throws FileNotFoundException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("TEXT files (*.txt)", new String[]{"*.txt"}), new FileChooser.ExtensionFilter("WORD files (*.docx)", new String[]{"*.docx"}), new FileChooser.ExtensionFilter("PDF files (*.pdf)", new String[]{"*.pdf"})});
        fileChooser.setTitle("Choose file");
        File file = fileChooser.showOpenDialog((Window)stage);
        if (file.exists()) {
            String extension = FilenameUtils.getExtension((String)file.toString());
            String text = null;
            switch (extension) {
                case "txt": {
                    try {
                        text = FileUtils.readFileToString((File)file, (String)"UTF-16");
                    }
                    catch (IOException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.txtInput.setText(text);
                    break;
                }
                case "docx": {
                    try {
                        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open((File)file));
                        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
                        text = extractor.getText();
                    }
                    catch (IOException | InvalidFormatException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.txtInput.setText(text);
                    break;
                }
                case "pdf": {
                    BodyContentHandler handler = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    FileInputStream inputstream = new FileInputStream(file);
                    ParseContext pcontext = new ParseContext();
                    PDFParser pdfparser = new PDFParser();
                    try {
                        pdfparser.parse((InputStream)inputstream, (ContentHandler)handler, metadata, pcontext);
                        text = handler.toString();
                    }
                    catch (IOException | TikaException | SAXException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        inputstream.close();
                    }
                    catch (IOException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.txtInput.setText(text);
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Success!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No file to choose try again!");
            alert.showAndWait();
        }
    }

    @FXML
    private void exportHandle(ActionEvent event) {
        Stage stage = new Stage();
        String text = this.txtOutput.getText();
        if (text != null && !text.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            MenuItem source = (MenuItem)event.getSource();
            switch (source.getId()) {
                case "text": {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TEXT files (*.txt)", new String[]{"*.txt"}));
                    break;
                }
                case "word": {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WORD files (*.docx)", new String[]{"*.docx"}));
                    break;
                }
                case "pdf": {
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", new String[]{"*.pdf"}));
                }
            }
            File file = fileChooser.showSaveDialog((Window)stage);
            switch (source.getId()) {
                case "text": {
                    try {
                        FileUtils.writeStringToFile((File)file, (String)text, (String)"UTF-16");
                    }
                    catch (IOException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "word": {
                    XWPFDocument doc = new XWPFDocument();
                    XWPFParagraph p = doc.createParagraph();
                    XWPFRun r = p.createRun();
                    r.setText(text);
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        doc.write((OutputStream)out);
                        out.close();
                    }
                    catch (FileNotFoundException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (IOException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case "pdf": {
                    Font font = FontFactory.getFont((String)"/resources/font/arial.ttf", (String)"Identity-H", (boolean)true);
                    Document document = new Document();
                    try {
                        PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(file));
                        document.open();
                        Paragraph para = new Paragraph(text, font);
                        para.setAlignment(3);
                        document.add((Element)para);
                        document.close();
                        break;
                    }
                    catch (DocumentException | FileNotFoundException ex) {
                        Logger.getLogger(LatCyrConverterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Success!Text is export to file: " + file.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No text to export!");
            alert.showAndWait();
        }
    }
}

