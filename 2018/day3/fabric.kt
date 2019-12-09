package aoc2018.day3;

import java.io.File;
import kotlin.system.exitProcess;
import kotlin.math.max;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun Splitline(opcodes: String): List<String> {
    // println("you gave ops of $opcodes");
    var ops: List<String> = opcodes.split(" ").map{ it.trim(); };
    // println("This is ${ops.size} instructions");
    return ops;
}

fun Splitcodes(opcodes: String, delim: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(delim).map{ it.trim().toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("You done messed up a-a-ron");
        println("Usage: cmd filename");
        exitProcess(-1);
    }
    var lines = Loadcodes(args[0]);
    var maxWidth = 1;
    var maxHeight = 1;
    var square = mutableMapOf<String, MutableList<Int>>();
    var sizes = mutableMapOf<Int, Int>();
    for (line in lines) {
        var bits = Splitline(line);
        var id = bits[0].replace("#", "").toInt();
        var topLeft = Splitcodes(bits[2].replace(":", ""), ",");
        var dimensions = Splitcodes(bits[3], "x");
        sizes.put(id, dimensions[0] * dimensions[1]);
        println("We got $topLeft as big as $dimensions");
        for (x in topLeft[0]..topLeft[0]+dimensions[0] - 1) {
            for (y in topLeft[1]..topLeft[1]+dimensions[1] - 1) {
                var coord = "${x},${y}";
                var already = square.getOrDefault(coord, mutableListOf<Int>());
                already.add(id);
                square.put(coord, already);
            }
        }
        maxWidth = max(maxWidth, topLeft[0] + dimensions[0]);
        maxHeight = max(maxHeight, topLeft[1] + dimensions[1]);
    }
    // println(square);
    // println(square.size);
    var countPerChar = square.filter{ it.value.size > 1 };
    // println(countPerChar);
    println(countPerChar.size);
    var countIntact = square.filter{ it.value.size == 1 }.values.flatten().groupingBy{ it }.eachCount();
    println(countIntact);
    println(sizes);
    println(countIntact.filter { sizes.get(it.key) == it.value });
}
