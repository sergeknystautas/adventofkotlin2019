package aoc2019.day7;

import java.io.File;
import kotlin.system.exitProcess;
import kotlin.math.max;

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

fun ProcessOps(ops: MutableList<Int>, inputs: MutableList<Int>, outputs: MutableList<Int>, pos: Int): Int {
    var opcodeAndParam: Int = ops[pos];
    var opcode = GetDigit(opcodeAndParam, 1) + 10 * GetDigit(opcodeAndParam,10);
    // println("Opcode $opcode");
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
        var var1 = inputs.removeAt(0);
        var arg1 = ops[pos + 1];
        // println("Saving input $var1 to position $arg1");
        ops[arg1] = var1;
        return pos + 2;
    } else if (opcode == 4) {
        // output
        var var1 = GetValue(ops, ops[pos + 1], GetDigit(opcodeAndParam, 100));
        outputs.add(var1);
        // println("** Output $var1");
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

fun ProcessAmplifier(sourceCodes: List<Int>, phase:Int, input: Int): Int {
    var inputs = mutableListOf<Int>();
    inputs.add(phase);
    inputs.add(input);

    var outputs = mutableListOf<Int>();

    var codes: MutableList<Int> = arrayListOf<Int>();
    codes.addAll(sourceCodes);

    var position = 0;
    do {
        position = ProcessOps(codes, inputs, outputs, position);
    } while (position != -1)

    return outputs.removeAt(0);
}

fun <Int> permute(list: List <Int>): List<List<Int>> {
    if (list.size == 1) {
        return listOf(list);
    }
    val perms = mutableListOf<List <Int>>();
    val sub = list[0];
    for (perm in permute(list.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList();
            newPerm.add(i,sub)
            perms.add(newPerm)
        }
    }
    return perms
}

fun main(args: Array<String>) {
    if (args.size == 1) {
        var ops = args[0];
        var sourceCodes = Loadcodes(ops);

        var sourcePhases: List<List<Int>> = permute(listOf(0,1,2,3,4));
        // I need to vary all of the phase combinations
        var highest = 0;
        for (phaseCombo in sourcePhases) {
            // println(phaseCombo);
            var input = 0;
            for (phase in phaseCombo) {
                input = ProcessAmplifier(sourceCodes, phase, input);
            }
            highest = max(highest, input);
            println("$phaseCombo gives output $input");
        }
        println("Highest signal is $highest");

    } else if (args.size == 2) {
        // Load the data
        var phases = args[0];
        var sourcePhases = Loadcodes(phases);

        var ops = args[1];
        var sourceCodes = Loadcodes(ops);

        var input = 0;
        for (phase in sourcePhases) {
            input = ProcessAmplifier(sourceCodes, phase, input);
        }
        println(input);
    } else {
        println("You done messed up a-a-ron");
        println("Usage: cmd phases opcodes");
        println(" ..  or .. ");
        println("Usage: cmd opcodes");
        exitProcess(-1);
    }

    // println(codes);
    // println(codes);
}
