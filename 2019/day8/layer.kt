package aoc2019.day8;

import kotlin.system.exitProcess;

fun Loadcodes(opcodes: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(",").map{ it.toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

fun CreateLayers(dimensions: List<Int>, data: String): List<String> {
    var layers = mutableListOf<String>();
    var segmentLength = dimensions[0] * dimensions[1];
    var numLayers = data.length.div(segmentLength) - 1;
    for (i in 0..numLayers) {
        var start = segmentLength * i;
        var end = start + segmentLength;
        var layer = data.substring(start, end);
        layers.add(layer);
    }
    return layers;
}

fun DecodeImage(dimensions: List<Int>, layers: List<String>): String {
    var segmentLength = dimensions[0] * dimensions[1] - 1;
    var decoded = mutableListOf<String>();
    for (i in 0..segmentLength) {
        for (layer in layers) {
            if (layer.get(i) != '2') {
                decoded.add(Character.toString(layer.get(i)));
                break;
            }
        }
    }
    return decoded.joinToString("");
}

fun main(args: Array<String>) {
    if (args.size == 2) {
        // Load the data
        var imageSize = args[0];
        var dimensions = Loadcodes(imageSize);

        var data = args[1];

        var layers = CreateLayers(dimensions, data);
        var decoded = DecodeImage(dimensions, layers);
        println();
        for (i in 0..dimensions[1] - 1) {
            // println(i);
            println(decoded.substring(i * dimensions[0], i * dimensions[0] + dimensions[0]).replace('0', ' '));
        }
        println();
        println(decoded);
        /*
        var layerFewestZeroes = "";
        var fewestZeroCount: Int = dimensions[0] * dimensions[1];
        for (layer in layers) {
            println(layer);
            var zeroCount = layer.count{("0".contains(it))};
            println(zeroCount);
            if (zeroCount < fewestZeroCount) {
                layerFewestZeroes = layer;
                fewestZeroCount = zeroCount;
            }
        }
        println("Out of ${layers.size} layers...");
        println(layerFewestZeroes);
        println(layerFewestZeroes.length);
        println(fewestZeroCount);
        var oneCount = layerFewestZeroes.count{("1".contains(it))};
        var twoCount = layerFewestZeroes.count{("2".contains(it))};
        println("1s... $oneCount");
        println("2s... $twoCount");
        println(oneCount * twoCount);
        */

        // Create layers made up of images

    } else {
        println("You done messed up a-a-ron");
        println("Usage: cmd image_size data");
        exitProcess(-1);
    }

    // println(codes);
    // println(codes);
}
