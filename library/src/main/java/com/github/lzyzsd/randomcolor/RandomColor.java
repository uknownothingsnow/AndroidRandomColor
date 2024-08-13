package com.github.lzyzsd.randomcolor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by bruce on 15/2/5.
 */
public class RandomColor {
    private static final String TAG = "RandomColor";
    private Random random;

    public static enum SaturationType {
        RANDOM, MONOCHROME
    }

    public static enum Luminosity {
        BRIGHT, LIGHT, DARK, RANDOM
    }

    public static class Options {
        int hue;
        SaturationType saturationType;
        Luminosity luminosity;

        public int getHue() {
            return hue;
        }

        public void setHue(int hue) {
            this.hue = hue;
        }

        public SaturationType getSaturationType() {
            return saturationType;
        }

        public void setSaturationType(SaturationType saturationType) {
            this.saturationType = saturationType;
        }

        public Luminosity getLuminosity() {
            return luminosity;
        }

        public void setLuminosity(Luminosity luminosity) {
            this.luminosity = luminosity;
        }
    }

    private HashMap<String, ColorInfo> colors = new HashMap<>();

    public RandomColor() {
        loadColorBounds();
        random = new Random();
    }
    
    public RandomColor(long seed){
        loadColorBounds();
        random = new Random();
        random.setSeed(seed);
    }

    private int getColor(int hue, int saturation, int brightness) {
        return android.graphics.Color.HSVToColor(new float[] {hue, saturation, brightness});
    }

    public int randomColor() {
        return randomColor(0, null, null);
    }

    public int randomColor(int value, SaturationType saturationType, Luminosity luminosity) {
        int hue = value;
        hue = pickHue(hue);
        int saturation = pickSaturation(hue, saturationType, luminosity);
        int brightness = pickBrightness(hue, saturation, luminosity);

        int color = getColor(hue, saturation, brightness);
        return color;
    }

    public int[] randomColor(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }

        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            colors[i] = randomColor();
        }

        return colors;
    }

    public int randomColor(Color color) {
        int hue = pickHue(color.name());
        int saturation = pickSaturation(color, null, null);
        int brightness = pickBrightness(color, saturation, null);

        int colorValue = getColor(hue, saturation, brightness);
        return colorValue;
    }

    public int[] random(Color color, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }

        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            colors[i] = randomColor(color);
        }

        return colors;
    }

    private int pickHue(int hue) {
        Range hueRange = getHueRange(hue);
        return doPickHue(hueRange);
    }

    private int doPickHue(Range hueRange) {
        int hue = randomWithin(hueRange);

        // Instead of storing red as two seperate ranges,
        // we group them, using negative numbers
        if (hue < 0) {
            hue = 360 + hue;
        }

        return hue;
    }

    private int pickHue(String name) {
        Range hueRange = getHueRange(name);
        return doPickHue(hueRange);
    }

    private Range getHueRange(int number) {
        if (number < 360 && number > 0) {
            return new Range(number, number);
        }

        return new Range(0, 360);
    }

    private Range getHueRange(String name) {
        if (colors.containsKey(name)) {
            return colors.get(name).getHueRange();
        }

        return new Range(0, 360);
    }

    private int pickSaturation(int hue, SaturationType saturationType, Luminosity luminosity) {
        return pickSaturation(getColorInfo(hue), saturationType, luminosity);
    }

    private int pickSaturation(Color color, SaturationType saturationType, Luminosity luminosity) {
        ColorInfo colorInfo = colors.get(color.name());
        return pickSaturation(colorInfo, saturationType, luminosity);
    }

    private int pickSaturation(ColorInfo colorInfo, SaturationType saturationType, Luminosity luminosity) {
        if (saturationType != null) {
            switch (saturationType) {
                case RANDOM:
                    return randomWithin(new Range(0, 100));
                case MONOCHROME:
                    return 0;
            }
        }

        if (colorInfo == null) {
            return 0;
        }

        Range saturationRange =  colorInfo.getSaturationRange();

        int min = saturationRange.start;
        int max = saturationRange.end;

        if (luminosity != null) {
            switch (luminosity) {
                case LIGHT:
                    min = 55;
                    break;
                case BRIGHT:
                    min = max - 10;
                    break;
                case DARK:
                    max = 55;
                    break;
            }
        }

        return randomWithin(new Range(min, max));
    }

    private int pickBrightness(int hue, int saturation, Luminosity luminosity) {
        ColorInfo colorInfo = getColorInfo(hue);

        return pickBrightness(colorInfo, saturation, luminosity);
    }

    private int pickBrightness(Color color, int saturation, Luminosity luminosity) {
        ColorInfo colorInfo = colors.get(color.name());

        return pickBrightness(colorInfo, saturation, luminosity);
    }

    private int pickBrightness(ColorInfo colorInfo, int saturation, Luminosity luminosity) {
        int min = getMinimumBrightness(colorInfo, saturation),
            max = 100;

        if (luminosity != null) {
            switch (luminosity) {

                case DARK:
                    max = min + 20;
                    break;

                case LIGHT:
                    min = (max + min) / 2;
                    break;

                case RANDOM:
                    min = 0;
                    max = 100;
                    break;
            }
        }

        return randomWithin(new Range(min, max));
    }

    private int getMinimumBrightness(ColorInfo colorInfo, int saturation) {
        if (colorInfo == null) {
            return 0;
        }

        List<Range> lowerBounds = colorInfo.getLowerBounds();
        for (int i = 0; i < lowerBounds.size() - 1; i++) {

            int s1 = lowerBounds.get(i).start,
                v1 = lowerBounds.get(i).end;

            if (i == lowerBounds.size() - 1) {
                break;
            }
            int s2 = lowerBounds.get(i + 1).start,
                v2 = lowerBounds.get(i + 1).end;

            if (saturation >= s1 && saturation <= s2) {

                float m = (v2 - v1)/(float) (s2 - s1),
                    b = v1 - m*s1;

                return (int) (m*saturation + b);
            }

        }

        return 0;
    }

    private ColorInfo getColorInfo(int hue) {
        // Maps red colors to make picking hue easier
        if (hue >= 334 && hue <= 360) {
            hue-= 360;
        }

        for(String key : colors.keySet()) {
            ColorInfo colorInfo = colors.get(key);
            if (colorInfo.getHueRange() != null && colorInfo.getHueRange().contain(hue)) {
                return colorInfo;
            }
        }

        return null;
    }

    private int randomWithin (Range range) {
        return (int) Math.floor(range.start + random.nextDouble()*(range.end + 1 - range.start));
    }

    public void defineColor(String name, Range hueRange, List<Range> lowerBounds) {
        int sMin = lowerBounds.get(0).start;
        int sMax = lowerBounds.get(lowerBounds.size() - 1).start;
        int bMin = lowerBounds.get(lowerBounds.size() - 1).end;
        int bMax = lowerBounds.get(0).end;

        colors.put(name, new ColorInfo(hueRange, new Range(sMin, sMax), new Range(bMin, bMax), lowerBounds));
    }

    private void loadColorBounds() {
        List<Range> lowerBounds1 = new ArrayList<>();
        lowerBounds1.add(new Range(0, 0));
        lowerBounds1.add(new Range(100, 0));
        defineColor(
            Color.MONOCHROME.name(),
            null,
            lowerBounds1
        );

        List<Range> lowerBounds2 = new ArrayList<>();
        lowerBounds2.add(new Range(20, 100));
        lowerBounds2.add(new Range(30, 92));
        lowerBounds2.add(new Range(40, 89));
        lowerBounds2.add(new Range(50, 85));
        lowerBounds2.add(new Range(60, 78));
        lowerBounds2.add(new Range(70, 70));
        lowerBounds2.add(new Range(80, 60));
        lowerBounds2.add(new Range(90, 55));
        lowerBounds2.add(new Range(100, 50));
        defineColor(
            Color.RED.name(),
            new Range(-26, 18),
            lowerBounds2
        );

        List<Range> lowerBounds3 = new ArrayList<Range>();
        lowerBounds3.add(new Range(20, 100));
        lowerBounds3.add(new Range(30, 93));
        lowerBounds3.add(new Range(40, 88));
        lowerBounds3.add(new Range(50, 86));
        lowerBounds3.add(new Range(60, 85));
        lowerBounds3.add(new Range(70, 70));
        lowerBounds3.add(new Range(100, 70));
        defineColor(
            Color.ORANGE.name(),
            new Range(19, 46),
            lowerBounds3
        );

        List<Range> lowerBounds4 = new ArrayList<>();
        lowerBounds4.add(new Range(25, 100));
        lowerBounds4.add(new Range(40, 94));
        lowerBounds4.add(new Range(50, 89));
        lowerBounds4.add(new Range(60, 86));
        lowerBounds4.add(new Range(70, 84));
        lowerBounds4.add(new Range(80, 82));
        lowerBounds4.add(new Range(90, 80));
        lowerBounds4.add(new Range(100, 75));

        defineColor(
            Color.YELLOW.name(),
            new Range(47, 62),
            lowerBounds4
        );

        List<Range> lowerBounds5 = new ArrayList<>();
        lowerBounds5.add(new Range(30, 100));
        lowerBounds5.add(new Range(40, 90));
        lowerBounds5.add(new Range(50, 85));
        lowerBounds5.add(new Range(60, 81));
        lowerBounds5.add(new Range(70, 74));
        lowerBounds5.add(new Range(80, 64));
        lowerBounds5.add(new Range(90, 50));
        lowerBounds5.add(new Range(100, 40));

        defineColor(
            Color.GREEN.name(),
            new Range(63,178),
            lowerBounds5
        );

        List<Range> lowerBounds6 = new ArrayList<>();
        lowerBounds6.add(new Range(20, 100));
        lowerBounds6.add(new Range(30, 86));
        lowerBounds6.add(new Range(40, 80));
        lowerBounds6.add(new Range(50, 74));
        lowerBounds6.add(new Range(60, 60));
        lowerBounds6.add(new Range(70, 52));
        lowerBounds6.add(new Range(80, 44));
        lowerBounds6.add(new Range(90, 39));
        lowerBounds6.add(new Range(100, 35));

        defineColor(
            Color.BLUE.name(),
            new Range(179, 257),
            lowerBounds6
        );

        List<Range> lowerBounds7 = new ArrayList<>();
        lowerBounds7.add(new Range(20, 100));
        lowerBounds7.add(new Range(30, 87));
        lowerBounds7.add(new Range(40, 79));
        lowerBounds7.add(new Range(50, 70));
        lowerBounds7.add(new Range(60, 65));
        lowerBounds7.add(new Range(70, 59));
        lowerBounds7.add(new Range(80, 52));
        lowerBounds7.add(new Range(90, 45));
        lowerBounds7.add(new Range(100, 42));

        defineColor(
            Color.PURPLE.name(),
            new Range(258, 282),
            lowerBounds7
        );

        List<Range> lowerBounds8 = new ArrayList<>();
        lowerBounds8.add(new Range(20, 100));
        lowerBounds8.add(new Range(30, 90));
        lowerBounds8.add(new Range(40, 86));
        lowerBounds8.add(new Range(60, 84));
        lowerBounds8.add(new Range(80, 80));
        lowerBounds8.add(new Range(90, 75));
        lowerBounds8.add(new Range(100, 73));

        defineColor(
            Color.PINK.name(),
            new Range(283, 334),
            lowerBounds8
        );
    }

    public static enum Color {
        MONOCHROME, RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, PINK
    }
}
