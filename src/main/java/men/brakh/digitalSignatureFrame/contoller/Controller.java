package men.brakh.digitalSignatureFrame.contoller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import men.brakh.cryptohash.CryptoHash;
import men.brakh.cryptohash.impl.SHA1;
import men.brakh.cryptohash.impl.SHA256;
import men.brakh.digitalSignature.DigitalSignatureMath;
import men.brakh.digitalSignature.SignatureAlgorithm;
import men.brakh.digitalSignature.rsa.RSAPublicKey;
import men.brakh.digitalSignature.rsa.RSASignature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private String pathToSign;
    private String pathToCheckSign;
    private Map<String, CryptoHash> cryptoHashAlgorythms;
    private String errorStyle = "-fx-background-color: red; -fx-text-fill: #fff";


    void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText("An error occurred during execution.");

        alert.showAndWait();
    }

    void completeAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Complete!");
        alert.setHeaderText(message);
        alert.setContentText("");

        alert.showAndWait();
    }

    @FXML
    private TextArea taChechSignMessage;

    @FXML
    private TextField tfCheckSignR;

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
    private TextField tfCheckSignE;

    @FXML
    private TextField tfSignD;

    @FXML
    private Button btnSignSelectFile;

    @FXML
    private TextField tfSignE;

    @FXML
    private TextField tfSignHash;
    @FXML
    private TextField tfSignHash16;

    @FXML
    private Button btnSign;

    @FXML
    private ComboBox<String> cbCheckSignSelectHashFunction;

    @FXML
    private Button btnChechSignature;

    @FXML
    private TextField tfExpectedHash;

    @FXML
    private TextField tfActualHash;

    @FXML
    private ComboBox<String> cbSignSelectHashFunction;

    @FXML
    private TextField tfCheckSignSignature;




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

    String removeLastLine(String source) {
        String[] lines = source.split("\n");
        StringBuilder sourceText = new StringBuilder(source);

        char currChar;
        do {
            currChar = sourceText.charAt(sourceText.length() - 1);
            sourceText.deleteCharAt(sourceText.length() - 1);
        } while(currChar != '\n');

        return sourceText.toString();

    }

    void chechD() {
        TextField textField = tfSignD;
        try {

            BigInteger d = new BigInteger(textField.getText());

            BigInteger p = new BigInteger(tfSignP.getText());
            BigInteger q = new BigInteger(tfSignQ.getText());
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));


            if ((d.compareTo(BigInteger.ONE) <= 0) || (d.compareTo(phi) >= 0))
                throw new ArithmeticException("D should be from 2 to phi(p*q) - 1");
            else if (!d.gcd(phi).equals(BigInteger.ONE))
                throw new ArithmeticException("D should be mutually simple with phi(p*q)");
            else
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
        FileWriter fileWriter = new FileWriter(filepath + ".signed");
        fileWriter.write(taSignedMessage.getText() + "\n" + signature.toString());
        fileWriter.close();
    }

    @FXML
    void btnSignClick(ActionEvent event) {
        try {
            String hashAlgorythm = cbSignSelectHashFunction.getValue();
            if(!cryptoHashAlgorythms.containsKey(hashAlgorythm)) throw  new ArithmeticException("Please, select hash algorithm");
            CryptoHash cryptoHash = cryptoHashAlgorythms.get(hashAlgorythm);

            //byte[] message = Files.readAllBytes(Paths.get(pathToSign));
            byte[] message = taSignedMessage.getText().getBytes();


            BigInteger p = new BigInteger(tfSignP.getText());
            BigInteger q = new BigInteger(tfSignQ.getText());
            BigInteger d = new BigInteger(tfSignD.getText());

            RSASignature signatureAlgorithm = new RSASignature(p,q,d,cryptoHash);
            BigInteger signature = signatureAlgorithm.sign(message);

            RSAPublicKey rsaPublicKey = signatureAlgorithm.getPublicKey();

            tfSignE.setText(rsaPublicKey.getE().toString());
            tfSignR.setText(rsaPublicKey.getR().toString());


            tfSignSignature.setText(signature.toString());

            BigInteger hash = cryptoHash.getIntHash(message);
            tfSignHash.setText(hash.toString());
            tfSignHash16.setText(hash.toString(16));
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

                if(event.getSource() == btnSignSelectFile) {
                    tfSignSignature.clear();
                    tfSignHash.clear();
                    taSignedMessage.setText(new String(plain));
                    pathToSign = path;
                } else if(event.getSource() == btnCheckSignSelectFile) {
                    tfActualHash.clear();
                    tfExpectedHash.clear();

                    taChechSignMessage.setText(removeLastLine(new String(plain)));

                    String[] text = new String(plain).split("\n");

                    String signatureFromFile = text[text.length-1];

                    tfCheckSignSignature.setText(signatureFromFile);


                    pathToCheckSign = path;
                }
            } catch (IOException e) {

            }
        }
    }

    @FXML
    void btnCheckSignatureClick(ActionEvent event) {
        try {
            BigInteger e = new BigInteger(tfCheckSignE.getText());
            BigInteger r = new BigInteger(tfCheckSignR.getText());

            String hashAlgorythm = cbCheckSignSelectHashFunction.getValue();
            if(!cryptoHashAlgorythms.containsKey(hashAlgorythm)) throw  new ArithmeticException("Please, select hash algorithm");
            CryptoHash cryptoHash = cryptoHashAlgorythms.get(hashAlgorythm);



            byte[] message = taChechSignMessage.getText().getBytes();


            RSAPublicKey rsaPublicKey = new RSAPublicKey(e,r);

            BigInteger signature = new BigInteger(tfCheckSignSignature.getText());

            boolean isCorrect = RSASignature.checkSignature(rsaPublicKey, signature, message, cryptoHash);
            if(isCorrect) {
                completeAlert("Signature confirmed", Alert.AlertType.CONFIRMATION);
            } else {
                completeAlert("The signature has been forged", Alert.AlertType.WARNING);
            }

            tfExpectedHash.setText(cryptoHash.getIntHash(message).mod(rsaPublicKey.getR()).toString()); // HASH(MESSAGE) MOD R
            tfActualHash.setText(DigitalSignatureMath.power(signature, rsaPublicKey.getE(), rsaPublicKey.getR()).toString());

        } catch (ArithmeticException e) {
            errorAlert(e.getMessage());
        } catch (Exception e) {
            errorAlert(e.getMessage());
        }
    }

    @FXML
    void initialize() {
        cryptoHashAlgorythms = new HashMap<>();
        cryptoHashAlgorythms.put("SHA-1", new SHA1());
        cryptoHashAlgorythms.put("SHA-256", new SHA256());
        cbSignSelectHashFunction.getItems().addAll(cryptoHashAlgorythms.keySet());
        cbCheckSignSelectHashFunction.getItems().addAll(cryptoHashAlgorythms.keySet());
    }

}
