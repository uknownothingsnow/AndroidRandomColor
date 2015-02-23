# Android Random Color

Inspired by David Merfield's [randomColor.js](https://github.com/davidmerfield/randomColor).
And onevcat's [RandomColorSwift](https://github.com/onevcat/RandomColorSwift)
It is a ported version to Android. You can use the library to generate attractive random colors on Android.

See the [demo and site](http://llllll.li/randomColor/) to know why does this exist.

[![Demo](http://llllll.li/randomColor/repo_demo.gif)](http://llllll.li/randomColor)

## Install

gradle

```groovy
dependencies {
    compile 'com.github.lzyzsd.randomcolor:library:1.0.0'
}
```

## Example

```java

// Returns a random int color value

RandomColor randomColor = new RandomColor();
int color = randomColor.randomColor();

// Returns an array of 10 random color values

RandomColor randomColor = new RandomColor();
int[] color = randomColor.randomColor(10);

//This lib also predefine some colors, so than you can random color by these predefined colors

// Returns an array of ten green colors

randomColor.random(RandomColor.Color.GREEN, 10);

// Returns a random color use hue, saturation, luminosity
// saturation has two kinds of value: RANDOM, MONOCHROME
// luminosity has for kinds of value: BRIGHT, LIGHT, DARK, RANDOM

randomColor(int value, SaturationType saturationType, Luminosity luminosity)

```

There is also a demo project in this repo.

## Acknowledgements

Thanks for David Merfield bringing us randomColor.js, which is a great utility.

The demo project is using Chirag Mehta's [Name the Color](http://chir.ag/projects/name-that-color) JavaScript library to extract name of color.

## License

This project is licensed under the terms of the MIT license.
