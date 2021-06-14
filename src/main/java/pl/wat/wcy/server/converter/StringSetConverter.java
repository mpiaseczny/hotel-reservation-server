package pl.wat.wcy.server.converter;

import com.google.common.collect.Sets;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.Set;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(Set<String> strings) {
        return strings != null ? String.join(SPLIT_CHAR, strings) : "";
    }

    @Override
    public Set<String> convertToEntityAttribute(String string) {
        return string != null ? Sets.newHashSet(string.split(SPLIT_CHAR)) : Collections.emptySet();
    }
}
