package com.Vaniz.InfoShop.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Locale;

@Converter
public class PerfilConverter implements AttributeConverter<Perfil, String> {

    @Override
    public String convertToDatabaseColumn(Perfil perfil) {
        return perfil == null ? null : perfil.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Perfil convertToEntityAttribute(String valor) {
        return valor == null ? null : Perfil.valueOf(valor.toUpperCase(Locale.ROOT));
    }
}