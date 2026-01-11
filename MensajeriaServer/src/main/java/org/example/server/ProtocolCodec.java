package org.example.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

public class ProtocolCodec {
    private static final Gson gson = new Gson();

    private ProtocolCodec() {};

    public static JsonObject decode(byte[] jsonBytes) {
        String s_val = new String(jsonBytes, StandardCharsets.UTF_8);
        return JsonParser.parseString(s_val).getAsJsonObject();
    }

    public static byte[] encode(JsonObject object) {
        String s_val = gson.toJson(object);
        return s_val.getBytes(StandardCharsets.UTF_8);
    }
}
