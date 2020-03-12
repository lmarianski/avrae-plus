package io.github.lmarianski.avraeplus.avrae.homebrew.spells;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.Locale;

@JsonAdapter(School.Serializer.class)
public enum School {

    ABJURATION,
    CONJURATION,
    DIVINATION,
    ENCHANTMENT,
    EVOCATION,
    ILLUSION,
    NECROMANCY,
    TRANSMUTATION;

    public static School get(String string) {
        return valueOf(string.toUpperCase(Locale.ROOT));
    }

    public static School getByLetter(String string) {
        for (School s : values())
            if (s.name().substring(0, 1).equals(string)) return s;
        return null;
    }


    public class Serializer implements JsonSerializer<School>, JsonDeserializer<School> {
        @Override
        public School deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return School.getByLetter(json.getAsString());
        }

        @Override
        public JsonElement serialize(School src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.name().substring(0, 1));
        }
    }
}
