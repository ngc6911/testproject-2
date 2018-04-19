package org.vktest.vktestapp.data.remote.api;

import android.content.Context;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {

    private Context context;

    public DateDeserializer(Context context) {
        this.context = context;
    }

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                context.getResources().getConfiguration().locale);


        try {
            return format.parse(date);
        } catch (ParseException exp) {
            return null;
        }
    }
}
