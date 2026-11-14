package com.example.p2.data.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public final class ApiErrorParser {
    private ApiErrorParser() {
    }

    public static String extrairDetail(ResponseBody body) {
        if (body == null) {
            return null;
        }
        try {
            JsonElement raiz = JsonParser.parseString(body.string());
            if (!raiz.isJsonObject()) {
                return raiz.toString();
            }
            JsonElement detail = raiz.getAsJsonObject().get("detail");
            return formatarDetail(detail);
        } catch (IOException | JsonParseException erro) {
            return erro.getMessage();
        }
    }

    private static String formatarDetail(JsonElement detail) {
        if (detail == null || detail.isJsonNull()) {
            return null;
        }
        if (detail.isJsonPrimitive()) {
            JsonPrimitive primitivo = detail.getAsJsonPrimitive();
            return primitivo.getAsString();
        }
        if (detail.isJsonArray()) {
            return formatarValidacoes(detail.getAsJsonArray());
        }
        return detail.toString();
    }

    private static String formatarValidacoes(JsonArray validacoes) {
        List<String> mensagens = new ArrayList<>();
        for (JsonElement validacao : validacoes) {
            JsonObject item = validacao.getAsJsonObject();
            if (item.has("msg")) {
                mensagens.add(item.get("msg").getAsString());
            }
        }
        return String.join("; ", mensagens);
    }
}
