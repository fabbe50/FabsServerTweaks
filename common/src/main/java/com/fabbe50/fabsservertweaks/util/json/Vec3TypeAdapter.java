package com.fabbe50.fabsservertweaks.util.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

public class Vec3TypeAdapter extends TypeAdapter<Vec3> {
    @Override
    public void write(JsonWriter out, Vec3 value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("x").value(value.x);
        out.name("y").value(value.y);
        out.name("z").value(value.z);
        out.endObject();
    }

    @Override
    public Vec3 read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        double x = 0;
        double y = 0;
        double z = 0;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "x" -> x = in.nextDouble();
                case "y" -> y = in.nextDouble();
                case "z" -> z = in.nextDouble();
                default -> in.skipValue();
            }
        }
        in.endObject();
        return new Vec3(x, y, z);
    }
}
