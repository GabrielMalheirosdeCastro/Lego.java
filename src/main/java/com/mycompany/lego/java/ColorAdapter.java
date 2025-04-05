package com.mycompany.lego.java;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.awt.Color;
import java.io.IOException;

/**
 * Adaptador Gson para serializar e desserializar objetos Color
 * @author Gabriel.Malheiros
 */
public class ColorAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        
        out.beginObject();
        out.name("r").value(value.getRed());
        out.name("g").value(value.getGreen());
        out.name("b").value(value.getBlue());
        out.name("a").value(value.getAlpha());
        out.endObject();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        int r = 0, g = 0, b = 0, a = 255;
        
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "r" -> r = in.nextInt();
                case "g" -> g = in.nextInt();
                case "b" -> b = in.nextInt();
                case "a" -> a = in.nextInt();
                default -> in.skipValue();
            }
        }
        in.endObject();
        
        return new Color(r, g, b, a);
    }
}