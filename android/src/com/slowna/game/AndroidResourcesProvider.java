package com.slowna.game;

import android.content.Context;
import android.content.res.Configuration;
import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.scrabble.extra.ResourcesProvider;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AndroidResourcesProvider implements ResourcesProvider {

    private Context context;
    private final Map<String, Integer> fieldsMap = new HashMap<>();

    public AndroidResourcesProvider(Context context) {
        this.context = context;
        updateMap();
    }

    @Override
    public void setLanguage(AppLanguage language) {
        Locale locale = new Locale(language.name().toLowerCase());
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
    }

    private void updateMap() {
        Field[] fields = R.string.class.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                fieldsMap.put(field.getName(), (int) field.get(field));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    @Override
    public String getString(String resName) {
        return context.getResources().getString(fieldsMap.get(resName));
    }
}
