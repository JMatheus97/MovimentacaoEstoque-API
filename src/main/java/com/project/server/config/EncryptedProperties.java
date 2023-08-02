package com.project.server.config;

import com.project.server.model.Usuario;
import javafx.util.Pair;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EncryptedProperties {

    private final String PASSWORD = "*************";
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final BasicTextEncryptor encryptor;

    public EncryptedProperties() {
        encryptor = new BasicTextEncryptor();
        encryptor.setPassword(PASSWORD);
    }

    public String encriptToken(Usuario usuario, Date date) {
        String original = usuario.getUsername() + ";" + DATE_FORMAT.format(date);
        return encryptor.encrypt(original);
    }

    public Pair<String, Date> decriptToken(String token) {
        String decrypted = encryptor.decrypt(token);
        String[] values = decrypted.split(";");
        try {
            return new Pair<>(values[0], DATE_FORMAT.parse(values[1]));
        } catch (ParseException e) {
            return null;
        }
    }

}
