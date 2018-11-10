package men.brakh.digitalSignatureFrame.contoller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import men.brakh.cryptohash.CryptoHash;
import men.brakh.cryptohash.impl.SHA1;
import men.brakh.digitalSignature.SignatureAlgorithm;
import men.brakh.digitalSignature.rsa.RSASignature;
import men.brakh.digitalSignatureFrame.BytesConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private String pathToSign;
    private Map<String, CryptoHash> cryptoHashAlgorythms;


    void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText("An error occurred during execution.");

        alert.showAndWait();
    }

    @FXML
    private TextField tfSignD;

    @FXML
    private Button btnSelectFileToSignClick;

    @FXML
    private Button btnSignSelectFile;

    @FXML
    private TextField tfSignHash;

    @FXML
    private Button btnSign;

    @FXML
    private ComboBox<String> cbSignSelectHashFunction;

    @FXML
    private TextField tfSignP;

    @FXML
    private TextArea taSignedMessage;

    @FXML
    private TextField tfSignQ;

    @FXML
    private TextField tfSignSignature;

    @FXML
    void tfSignPKeyReleased(KeyEvent event) {

    }

    @FXML
    void tfSignQReleased(KeyEvent event) {
    }

    @FXML
    void tfQSignKeyReleased(KeyEvent event) {

    }

    @FXML
    void tfSignDKeyReleased(KeyEvent event) {

    }

    void saveSignature(String filepath, BigInteger signature) throws IOException {
        FileWriter fileWriter = new FileWriter(filepath + ".signature");
        fileWriter.write(signature.toString());
        fileWriter.close();
    }

    @FXML
    void btnSignClick(ActionEvent event) {
        try {
            String hashAlgorythm = cbSignSelectHashFunction.getValue();
            if(!cryptoHashAlgorythms.containsKey(hashAlgorythm)) throw  new ArithmeticException("Please, select hash algorithm");
            CryptoHash cryptoHash = cryptoHashAlgorythms.get(hashAlgorythm);

            byte[] message = Files.readAllBytes(Paths.get(pathToSign));
            BigInteger hash = cryptoHash.getIntHash(message);

            tfSignHash.setText(hash.toString());

            BigInteger p = new BigInteger(tfSignP.getText());
            BigInteger q = new BigInteger(tfSignQ.getText());
            BigInteger d = new BigInteger(tfSignD.getText());

            SignatureAlgorithm signatureAlgorithm = new RSASignature(p,q,d,cryptoHash);
            BigInteger signature = signatureAlgorithm.sign(message);

            tfSignSignature.setText(signature.toString());
            saveSignature(pathToSign, signature);
        } catch (ArithmeticException e) {
            errorAlert(e.getMessage());
        } catch (IOException e) {
            errorAlert("Invalid file");
        }
    }

    @FXML
    void btnSignSelectFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        String path;
        if (selectedFile != null) {
            path = selectedFile.getAbsolutePath();
            try {
                byte[] plain = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
                BytesConverter bytesConverter = new BytesConverter();
                tfSignSignature.clear();
                tfSignHash.clear();
                taSignedMessage.setText(bytesConverter.byteArray2String(plain));
                pathToSign = path;
            } catch (IOException e) {
            }
        }
    }

    @FXML
    void initialize() {
        cryptoHashAlgorythms = new HashMap<>();
        cryptoHashAlgorythms.put("SHA-1", new SHA1());
        cbSignSelectHashFunction.getItems().addAll(cryptoHashAlgorythms.keySet());
    }

}
