package men.brakh.digitalSignatureFrame.contoller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import men.brakh.cryptohash.CryptoHash;
import men.brakh.cryptohash.impl.SHA1;
import men.brakh.digitalSignature.DigitalSignatureMath;
import men.brakh.digitalSignature.SignatureAlgorithm;
import men.brakh.digitalSignature.rsa.RSAPrivateKey;
import men.brakh.digitalSignature.rsa.RSAPublicKey;
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
    private String errorStyle = "-fx-background-color: red; -fx-text-fill: #fff";


    void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText("An error occurred during execution.");

        alert.showAndWait();
    }

    @FXML
    private TextArea taChechSignMessage;

    @FXML
    private Button btnCheckSignSelectFile;

    @FXML
    private TextField tfSignP;

    @FXML
    private TextArea taSignedMessage;

    @FXML
    private TextField tfSignQ;

    @FXML
    private TextField tfSignR;

    @FXML
    private TextField tfSignSignature;

    @FXML
    private TextField tfSignD;

    @FXML
    private Button btnSignSelectFile;

    @FXML
    private TextField tfSignE;

    @FXML
    private TextField tfSignHash;

    @FXML
    private Button btnSign;

    @FXML
    private ComboBox<String> cbCheckSignSelectHashFunction;

    @FXML
    private ComboBox<String> cbSignSelectHashFunction;



    @FXML
    void tfSignPrimeKeyReleased(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        try {
            BigInteger q = new BigInteger(textField.getText());
            if(DigitalSignatureMath.isPrime(q)) {
                textField.setStyle("");
            } else {
                textField.setStyle(errorStyle);
            }
        } catch (Exception e) {
            textField.setStyle(errorStyle);
        }
        chechD();
    }

    void chechD() {
        TextField textField = tfSignD;
        try {

            BigInteger d = new BigInteger(textField.getText());

            BigInteger p = new BigInteger(tfSignP.getText());
            BigInteger q = new BigInteger(tfSignP.getText());
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            if ((d.compareTo(BigInteger.ONE) <= 0) || (d.compareTo(phi) >= 0))
                throw new ArithmeticException("D should be from 2 to phi(p*q) - 1");
            if (!d.gcd(phi).equals(BigInteger.ONE))
                throw new ArithmeticException("D should be mutually simple with phi(p*q)");
            textField.setStyle("");
        } catch (Exception e) {
            textField.setStyle(errorStyle);
        }
    }

    @FXML
    void tfSignDKeyReleased(KeyEvent event) {
        chechD();
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



            BigInteger p = new BigInteger(tfSignP.getText());
            BigInteger q = new BigInteger(tfSignQ.getText());
            BigInteger d = new BigInteger(tfSignD.getText());

            SignatureAlgorithm signatureAlgorithm = new RSASignature(p,q,d,cryptoHash);
            BigInteger signature = signatureAlgorithm.sign(message);

            RSAPublicKey rsaPublicKey = ((RSASignature) signatureAlgorithm).getPublicKey();

            tfSignE.setText(rsaPublicKey.getE().toString());
            tfSignR.setText(rsaPublicKey.getR().toString());


            tfSignSignature.setText(signature.toString());

            BigInteger hash = cryptoHash.getIntHash(message).mod(rsaPublicKey.getR());
            tfSignHash.setText(hash.toString());
            saveSignature(pathToSign, signature);
        } catch (ArithmeticException e) {
            errorAlert(e.getMessage());
        } catch (IOException e) {
            errorAlert("Invalid file");
        } catch (Exception e) {
            errorAlert(e.getMessage());
        }
    }

    @FXML
    void btnSelectFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        String path;
        if (selectedFile != null) {
            path = selectedFile.getAbsolutePath();
            try {
                byte[] plain = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
                BytesConverter bytesConverter = new BytesConverter();

                if(event.getSource() == btnSignSelectFile) {
                    tfSignSignature.clear();
                    tfSignHash.clear();
                    taSignedMessage.setText(bytesConverter.byteArray2String(plain));
                    pathToSign = path;
                }
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
