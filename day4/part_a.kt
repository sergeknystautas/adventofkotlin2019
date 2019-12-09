package day4;

// import kotlin.int.rem;
/*
 Scans a range between two 6 digit numbers and counting how many combinations meet a set of rules.
 */

fun Descending(digits: List<Int>) : Boolean {
    var prev = digits[0];
    for (i in 1..digits.size - 1) {
        if (prev > digits[i]) {
            return true;
        }
        prev = digits[i];
    }
    return false;
}

fun Repeating(digits: List<Int>) : Boolean {
    var frequencies = digits.groupingBy { it }.eachCount();
    var pairs = frequencies.filter{ it -> it.value > 1};
    if (pairs.size == 0) {
        // If no pairs, this fails.
        return false;
    }
    /* This logic gives 1044 which is too low
    var odd = frequencies.filter{ it -> it.value == 3 || it.value == 5};
    if (odd.size > 0) {
        // If there are triples or quintuples, then fails
        return false;
    }
    */
    pairs = frequencies.filter{ it -> it.value == 2};
    if (pairs.size == 0) {
        // If no pairs, this fails.
        return false;
    }
    var text = digits.joinToString("");
    println("$text groups into $frequencies");
    return true;
}

// Scan from the start to the end.
fun Scan(start: Int, end: Int): Int {
    var matches : Int = 0;
    for (i in start..end) {
        var num = ToArray(i);
        if (Descending(num)) {
            continue;
        }
        if (!Repeating(num)) {
            continue;
        }
        // println(i);
        matches++;
    }
    return matches;
}

// Break down the digits into an array for easier recursion
fun ToArray(raw: Int) : List<Int> {
    var digits = mutableListOf<Int>();
    var value = raw;
    // println(value);
    while (value > 0) {
        var digit = value.rem(10);
        digits.add(digit);
        value = value.div(10);
    }
    digits.reverse();
    // println(digits);
    return digits;
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("You done messed up a-a-ron");
        return;
    }
    var start: Int = args[0].toInt();
    var end: Int = args[1].toInt();

    var possibilities = Scan(start, end);
    println("Possibilities $possibilities");
}
