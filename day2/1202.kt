package day2;

import java.io.File;

fun Loadcodes(opcodes: String): List<Int> {

    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(",").map{ it.toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

fun ProcessOps(ops: MutableList<Int>, pos: Int): Int {
    var opcode: Int = ops[pos];
    if (opcode == 1) {
        // Add
        var arg1 = ops[pos + 1];
        var arg2 = ops[pos + 2];
        var arg3 = ops[pos + 3];
        var var1 = ops[arg1];
        var var2 = ops[arg2];
        var sum = var1 + var2;
        ops[arg3] = sum;
        return pos + 4;
    } else if (opcode == 2) {
        // Multiply
        var arg1 = ops[pos + 1];
        var arg2 = ops[pos + 2];
        var arg3 = ops[pos + 3];
        var var1 = ops[arg1];
        var var2 = ops[arg2];
        var sum = var1 * var2;
        ops[arg3] = sum;
        return pos + 4;
    } else if (opcode == 99) {
        // Halt
        return -1;
    } else {
        // Crash and burn
        println("Something is wrong... hit opcode $opcode");
        return -1
    }
}

fun TryNounVerb(sourceCodes: List<Int>, noun:Int, verb:Int): Int {
    var ops: MutableList<Int> = arrayListOf<Int>();
    ops.addAll(sourceCodes);

    ops[1] = noun;
    ops[2] = verb;

    var position = 0;
    do {
        position = ProcessOps(ops, position);
    } while (position != -1)
    // var result = ops.joinToString(",");
    // println(result);
    return ops[0];
}

fun main(args: Array<String>) {
    if (args.size == 0) {
        println("You done messed up a-a-ron");
    } else {
        var ops = args[0];
        var codes = Loadcodes(ops);
        for (noun in 0..99) {
            for (verb in 0..99) {
                var result = TryNounVerb(codes, noun, verb);
                // println(result);
                if (result == 19690720) {
                    println("***** $noun $verb");
                }
            }
        }
        // var result = TryNounVerb(codes, 12, 2);
        // println(result);
    }
}
