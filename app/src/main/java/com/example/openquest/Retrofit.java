package com.example.openquest;// En tu clase Retrofit.java (o RetrofitClient.java)
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Date;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    private static final String BASE_URL = "http://192.168.47.54/";
    private static APIService api;

    public Retrofit() {
        if (api == null) {
            JsonDeserializer<Date> dateDeserializer = new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(com.google.gson.JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) throws JsonParseException {
                    if (json.isJsonPrimitive()) {
                        long timestamp = json.getAsJsonPrimitive().getAsLong();
                        return new Date(timestamp);
                    }
                    throw new JsonParseException("Fecha JSON inesperada: " + json.toString());
                }
            };

            JsonSerializer<Date> dateSerializer = new JsonSerializer<Date>() {
                @Override
                public com.google.gson.JsonElement serialize(Date src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                    return new JsonPrimitive(src.getTime());
                }
            };

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, dateDeserializer)
                    .registerTypeAdapter(Date.class, dateSerializer)
                    .setLenient()
                    .create();

            api = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(APIService.class);
        }
    }

    public APIService getApi() {
        return api;
    }
}