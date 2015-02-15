package com.github.lzyzsd.randomcolor;

import java.util.List;

/**
 * Created by bruce on 15/2/9.
 */
public class ColorInfo {
    Range hueRange;
    Range saturationRange;
    Range brightnessRange;
    List<Range> lowerBounds;

    public ColorInfo(Range hueRange, Range saturationRange, Range brightnessRange, List<Range> lowerBounds) {
        this.hueRange = hueRange;
        this.saturationRange = saturationRange;
        this.brightnessRange = brightnessRange;
        this.lowerBounds = lowerBounds;
    }

    public Range getHueRange() {
        return hueRange;
    }

    public void setHueRange(Range hueRange) {
        this.hueRange = hueRange;
    }

    public Range getSaturationRange() {
        return saturationRange;
    }

    public void setSaturationRange(Range saturationRange) {
        this.saturationRange = saturationRange;
    }

    public Range getBrightnessRange() {
        return brightnessRange;
    }

    public void setBrightnessRange(Range brightnessRange) {
        this.brightnessRange = brightnessRange;
    }

    public List<Range> getLowerBounds() {
        return lowerBounds;
    }

    public void setLowerBounds(List<Range> lowerBounds) {
        this.lowerBounds = lowerBounds;
    }
}