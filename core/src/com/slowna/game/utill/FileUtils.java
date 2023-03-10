package com.slowna.game.utill;

import com.badlogic.gdx.Gdx;
import com.slowna.game.pojo.ScrabbleCharInit;
import com.slowna.game.pojo.StartChars;
import lombok.experimental.UtilityClass;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@UtilityClass
public class FileUtils {

    public StartChars loadCharsFromFile(String resourceName) {
        try {
            StartChars startChars = new StartChars();
            String jsonObj = Gdx.files.internal("language/" + resourceName).readString();
            JSONObject object = new JSONObject(jsonObj);
            JSONArray chars = object.getJSONArray("chars");
            for (int i = 0; i < chars.length(); i++) {
                JSONObject startChar = chars.getJSONObject(i);
                ScrabbleCharInit init = new ScrabbleCharInit();
                init.setPoints(startChar.getInt("points"));
                init.setQuantity(startChar.getInt("quantity"));
                init.setLetter(startChar.getString("letter").charAt(0));
                startChars.getChars().add(init);
            }
            return startChars;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //todo apply to gzip file
    public void saveNewLine(String resourceName, String word) {
        try (FileWriter w = new FileWriter(FileUtils.class.getClassLoader().getResource(resourceName).getFile(), true);
             BufferedWriter writer = new BufferedWriter(w)) {
            writer.newLine();
            writer.write(word);
        } catch (IOException e) {
            throw new RuntimeException("Problem to write file");
        }
    }

    public List<String> loadGzipFile(String resourcesName) {
        try (InputStream fileStream = Gdx.files.internal("language/" + resourcesName).read();
             InputStream gzipStream = new GZIPInputStream(Objects.requireNonNull(fileStream));
             Reader decoder = new InputStreamReader(gzipStream, StandardCharsets.UTF_8);
             BufferedReader buffered = new BufferedReader(decoder)) {
            return buffered.lines().collect(Collectors.toList());
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }
}
