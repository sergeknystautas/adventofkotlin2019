package aoc2018.day2;

import java.io.File;
import kotlin.system.exitProcess;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun CountGroups(letters: String): Map<Int, String> {
    // letters.chunked(1);
    println(letters);
    var countPerChar = letters.chunked(1).groupingBy{ it }.eachCount();
    println(countPerChar);
    val reversed = countPerChar.entries.associateBy({ it.value }) { it.key }
    println(reversed);
    // println(letters.associateWith { char -> letters.count(char) });
    return reversed;
}

fun Levenshtein(strand1: String, strand2: String): Int {
    require(strand1.length == strand2.length) { "left and right strands must be of equal length." }
    return strand1.zip(strand2).count { it.first != it.second }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("You done messed up a-a-ron");
        println("Usage: cmd filename");
        exitProcess(-1);
    }
    var lines = Loadcodes(args[0]);

    var twos = 0;
    var threes = 0;
    for (line in lines) {
        var letters = CountGroups(line);
        if (letters.contains(2)) {
            twos++;
        }
        if (letters.contains(3)) {
            threes++;
        }
    }
    println("Twos: $twos and Threes: $threes");
    var result = twos * threes;
    println("Total: $result");
    for (i in 0..lines.size - 1) {
        for (j in i+1..lines.size - 1) {
            if (Levenshtein(lines[i], lines[j]) <= 1) {
                println("Matching boxes are ${lines[i]} and ${lines[j]}");
            }
        }
    }
}
