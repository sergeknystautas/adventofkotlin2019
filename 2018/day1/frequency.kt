package aoc2018.day1;

import java.io.File;
import kotlin.system.exitProcess;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun Splitcodes(opcodes: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(",").map{ it.trim().toInt() };
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
    var changes = Splitcodes(lines.joinToString().replace("+", ""));
    println(changes);
    println("Sum is ${changes.sum()}");

    // Now iterate
    var frequencies = mutableSetOf(0);
    var firstRepeat: Int? = null;
    var now = 0;
    do {
        for (change in changes) {
            now += change;
            if (frequencies.contains(now)) {
                firstRepeat = now;
                break;
            }
            frequencies.add(now);
        }
    } while (firstRepeat == null);
    println("First repeated is $firstRepeat");
}
