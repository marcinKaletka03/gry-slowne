package com.slowna.game.utill;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.PI;

@UtilityClass
public class ShapeUtil {

    private static final Map<String, Texture> loadedTextures = new HashMap<>();
    private static final Array<Color> rainbowColors = new Array<>(new Color[]{Color.PURPLE, Color.BLUE, Color.NAVY, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED});


    public static List<Vector2> makeCirclesPoints(float radius, int points) {
        double step = 2 * PI / points;
        List<Vector2> results = new ArrayList<>();
        for (double t = 0; t < 2 * PI; t += step) {
            results.add(new Vector2((float) -(radius * Math.cos(t)), (float) (radius * Math.sin(t))));
        }
        return results;
    }

    public static Texture getGradientCircle(int size, Color color) {
        return getGradientCircle(size, color, color, Interpolation.linear);
    }

    public static Texture getGradientCircle(int size, Color color, Interpolation interpolation) {
        return getGradientCircle(size, color, color, interpolation);
    }

    public static Texture getCircle(int size, Color color) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int center = size / 2;
        pixmap.setColor(color);
        pixmap.fillCircle(center, center, size / 2);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public static Texture getGradientCircle(int size, Color startColor, Color finishColor, Interpolation interpolation) {
        String name = size + startColor.toString() + finishColor.toString() + interpolation.toString();
        Texture resource = loadedTextures.get(name);
        if (resource != null) {
            return resource;
        }
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int center = size / 2;
        Color current = new Color(startColor);
        for (int i = size; i > 0; i -= 2) {
            current.a = interpolation.apply(0, 1, (size / (float) i) / 100f);
            pixmap.setColor(current);
            pixmap.fillCircle(center, center, i / 2);
            current.lerp(finishColor, 1f / size);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        loadedTextures.put(name, texture);
        return texture;
    }

    public static Texture createGradientHorizontalRectangle(int width, int height, Color startColor, Color finishColor) {
        String name = width + startColor.toString() + finishColor.toString();
        Texture resource = loadedTextures.get(name);
        if (resource != null) {
            return resource;
        }
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Color current = new Color(startColor);
        for (int i = 0; i < width; i++) {
            pixmap.setColor(current);
            pixmap.fillRectangle(i, 0, 1, height);
            current.lerp(finishColor, 1f / width);
        }
        Texture texture = new Texture(pixmap);
        loadedTextures.put(name, texture);
        pixmap.dispose();
        return texture;
    }

    public static Texture createGradientVerticalRectangle(int width, int height, Color startColor, Color finishColor) {
        String name = width + startColor.toString() + finishColor.toString();
        Texture resource = loadedTextures.get(name);
        if (resource != null) {
            return resource;
        }
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        Color current = new Color(startColor);
        for (int i = 0; i < height; i++) {
            pixmap.setColor(current);
            pixmap.fillRectangle(0, i, width, 1);
            current.lerp(finishColor, 1f / height);
        }
        Texture texture = new Texture(pixmap);
        loadedTextures.put(name, texture);
        pixmap.dispose();
        return texture;
    }

    public static Texture createRectangle(int width, int height, Color color) {
        String name = width + color.toString() + height;
        if (loadedTextures.containsKey(name)) {
            return loadedTextures.get(name);
        }
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        loadedTextures.put(name, texture);
        return texture;
    }

    public static Texture getRainbow() {
        Texture rainbow = loadedTextures.get("rainbow");
        if (rainbow != null) {
            return rainbow;
        }
        int multiplier = 30;
        Pixmap pixmap = new Pixmap(4, rainbowColors.size * multiplier, Pixmap.Format.RGBA8888);
        int counter = 0;
        for (int i = 0; i < rainbowColors.size * multiplier; i++) {
            pixmap.setColor(rainbowColors.get(counter));
            pixmap.fillRectangle(0, rainbowColors.size * multiplier - 1 - i, 4, 1);
            if (i != 0 && i % rainbowColors.size == multiplier) {
                counter++;
            }
        }
        Texture texture = new Texture(pixmap);
        loadedTextures.put("rainbow", texture);
        pixmap.dispose();
        return texture;
    }
}
