package com.jcb.instaapp.network;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jcb.instaapp.model.Datum;

import java.lang.reflect.Type;

/**
 * Created by jacobkoikkara on 9/21/15.
 */
public class InstagramDeserializer implements JsonDeserializer<Datum[]> {


    @Override
    public Datum[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement content = json.getAsJsonObject().get("data");

        return new Gson().fromJson(content, Datum[].class);


    }
}
