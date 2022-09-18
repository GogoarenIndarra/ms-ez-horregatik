package com.jablonski.msezhorregatik.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

@Component
@RequiredArgsConstructor
public class EncryptedValueConverter implements AttributeConverter<String, String> {

    private final TextEncryptor encryptor;

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptor.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptor.decrypt(s);
    }
}
