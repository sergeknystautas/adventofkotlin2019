package aoc2018;

import java.io.File;
import kotlin.system.exitProcess;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
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

    println(lines);
}
