package day5;

import java.io.File;
import kotlin.system.exitProcess;

var input = mutableListOf<Int>();

fun Loadcodes(opcodes: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(",").map{ it.toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

/*
 Take a number and give the digit for example 1, 10, 100, etc...
 */
fun GetDigit(number: Int, digit: Int): Int {
    return number.div(digit).rem(10);
}

fun GetValue(ops: MutableList<Int>, parameter: Int, mode: Int): Int {
    // println("Value of $parameter using mode $mode");
    if (mode == 0) {
        // Look up
        return ops[parameter];
    } else if (mode == 1) {
        // Use this parameter as the value
        return parameter;
    }
    println("Something went wrong with $parameter in mode $mode");
    exitProcess(-1);
}

fun ProcessOps(ops: MutableList<Int>, pos: Int): Int {
    var opcodeAndParam: Int = ops[pos];
    var opcode = GetDigit(opcodeAndParam, 1) + 10 * GetDigit(opcodeAndParam,10);
    println("Opcode $opcode");
    if (opcode == 1) {
        // Add
        var arg1 = ops[pos + 1];
        var arg2 = ops[pos + 2];
        var arg3 = ops[pos + 3];
        var var1 = GetValue(ops, arg1, GetDigit(opcodeAndParam, 100));
        var var2 = GetValue(ops, arg2, GetDigit(opcodeAndParam, 1000));
        if (GetDigit(opcodeAndParam, 10000) == 1) {
            println("Position $pos opcode $opcodeAndParam suggests writing to a value");
            exitProcess(-1);
        }
        var sum = var1 + var2;
        ops[arg3] = sum;
        return pos + 4;
    } else if (opcode == 2) {
        // Multiply
        var arg1 = ops[pos + 1];
        var arg2 = ops[pos + 2];
        var arg3 = ops[pos + 3];
        var var1 = GetValue(ops, arg1, GetDigit(opcodeAndParam, 100));
        var var2 = GetValue(ops, arg2, GetDigit(opcodeAndParam, 1000));
        if (GetDigit(opcodeAndParam, 10000) == 1) {
            println("Position $pos opcode $opcodeAndParam suggests writing to a value");
            exitProcess(-1);
        }
        var sum = var1 * var2;
        ops[arg3] = sum;
        return pos + 4;
    } else if (opcode == 3) {
        // input
        if (GetDigit(opcodeAndParam, 100) == 1) {
            println("Position $pos opcode $opcodeAndParam suggests writing to a value");
            exitProcess(-1);
        }
        var var1 = input.removeAt(0);
        var arg1 = ops[pos + 1];
        println("Saving input $var1 to position $arg1");
        ops[arg1] = var1;
        return pos + 2;
    } else if (opcode == 4) {
        // output
        var var1 = GetValue(ops, ops[pos + 1], GetDigit(opcodeAndParam, 100));
        println("** Output $var1");
        return pos + 2;
    } else if (opcode == 5) {
        // jump if true
        var var1 = GetValue(ops, ops[pos + 1], GetDigit(opcodeAndParam, 100));
        var var2 = GetValue(ops, ops[pos + 2], GetDigit(opcodeAndParam, 1000));
        if (var1 != 0) {
            // Jump pointer to this position
            return var2;
        } else {
            return pos + 3;
        }
    } else if (opcode == 6) {
        // jump if false
        var var1 = GetValue(ops, ops[pos + 1], GetDigit(opcodeAndParam, 100));
        var var2 = GetValue(ops, ops[pos + 2], GetDigit(opcodeAndParam, 1000));
        if (var1 == 0) {
            // Jump pointer to this position
            return var2;
        } else {
            return pos + 3;
        }
    } else if (opcode == 7) {
        // less than
        if (GetDigit(opcodeAndParam, 10000) == 1) {
            println("Position $pos opcode $opcodeAndParam suggests writing to a value");
            exitProcess(-1);
        }
        var arg1 = ops[pos + 1];
        var arg2 = ops[pos + 2];
        var arg3 = ops[pos + 3];
        var var1 = GetValue(ops, arg1, GetDigit(opcodeAndParam, 100));
        var var2 = GetValue(ops, arg2, GetDigit(opcodeAndParam, 1000));
        if (var1 < var2) {
            ops[arg3] = 1;
        } else {
            ops[arg3] = 0;
        }
        return pos + 4;
    } else if (opcode == 8) {
        // equals
        if (GetDigit(opcodeAndParam, 10000) == 1) {
            println("Position $pos opcode $opcodeAndParam suggests writing to a value");
            exitProcess(-1);
        }
        var arg1 = ops[pos + 1];
        var arg2 = ops[pos + 2];
        var arg3 = ops[pos + 3];
        var var1 = GetValue(ops, arg1, GetDigit(opcodeAndParam, 100));
        var var2 = GetValue(ops, arg2, GetDigit(opcodeAndParam, 1000));
        if (var1 == var2) {
            ops[arg3] = 1;
        } else {
            ops[arg3] = 0;
        }
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

fun main(args: Array<String>) {
    if (args.size == 0) {
        println("You done messed up a-a-ron");
    } else {
        var rawInputs = Loadcodes(args[0])
        input.addAll(rawInputs);

        var ops = args[1];
        var sourceCodes = Loadcodes(ops);
        var codes: MutableList<Int> = arrayListOf<Int>();
        codes.addAll(sourceCodes);

        // println(codes);

        var position = 0;
        do {
            position = ProcessOps(codes, position);
        } while (position != -1)
        // println(codes);
    }
}
