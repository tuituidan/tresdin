package com.tuituidan.tresdin.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Objects;

/**
 * SensitiveSerializer.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025-10-17
 */
public class SensitiveSerializer extends JsonSerializer<Object> {

    public final SensitiveType type;

    /**
     * SensitiveSerializer.
     *
     * @param type SensitiveType
     */
    public SensitiveSerializer(SensitiveType type) {
        this.type = type;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        switch (type) {
            case EMAIL:
                gen.writeString(Objects.toString(value).replaceAll("(^.).*(@.*$)", "$1****$2"));
                break;
            case ID_CARD:
                gen.writeString(Objects.toString(value).replaceAll("(\\d{6})\\d{8}(\\w{4})", "$1********$2"));
                break;
            case PHONE:
                gen.writeString(Objects.toString(value).replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                break;
            default:
                gen.writeString(Objects.toString(value));
                break;
        }
    }

}
