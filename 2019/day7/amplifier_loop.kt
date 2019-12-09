package aoc2019.day7;

import java.io.File;
import kotlin.system.exitProcess;
import kotlin.math.max;

/*
General strategy...
x 1. create 5 instances of the amplifier
x   a. create ended0-4 Int to track if they've exited.  -1 means dead, 0 means good.
x 2. create 5 input/output mutable lists.  list0 is input from amplifier0 (and output from amplifier4),
x   a. easier to understand... list1 is output from amplifier0 and input for amplifier1.
x 3. put the phases into list0-4.
x 4. put the seed input into list0.
5. now while any amplifier is awake and has input, go process those ops and continue until it creates an output.
*/

class Amplifier constructor (sourceCodes: List<Int>, _inputs: MutableList<Int>, _outputs: MutableList<Int>) {
    val ops = mutableListOf<Int>();
    val inputs = _inputs;
    val outputs = _outputs;
    var pos: Int = 0;
    init {
        ops.addAll(sourceCodes);
    }

    fun isExited() : Boolean {
        return pos == -1;
    }

    fun ops(): Int {
        pos = ProcessOps();
        return pos;
    }

    fun opsUntilOutput() {
        println("until output on $this");
        while (outputs.isEmpty() && !isExited() ) {
            ops();
        }
    }

    /** Do not call this directly... should be thru ops() */
    private fun ProcessOps(): Int {
        var opcodeAndParam: Int = ops[pos];
        var opcode = GetDigit(opcodeAndParam, 1) + 10 * GetDigit(opcodeAndParam,10);
        println("Processing $opcode on $this");
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
                println("Position $pos opcode $opcodeAndParam suggests writing to a value ");
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

    /* Take a number and give the digit for example 1, 10, 100, etc... */
    fun GetDigit(number: Int, digit: Int): Int {
        return number.div(digit).rem(10);
    }

    /* Use positional or immediate mode */
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
}

fun Loadcodes(opcodes: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(",").map{ it.toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

fun SetupAmplifiers(sourceCodes: List<Int>, phases: List<Int>): List<Amplifier> {
    var queues = mutableListOf<MutableList<Int>>();
    for (i in 0..4) {
        var queue = mutableListOf<Int>();
        // put the phases as the first input in the queue
        queue.add(phases[i]);
        queues.add(queue);
    }
    // we should put the first queue as the last one to create the loop.
    queues.add(queues[0]);
    // Add 0 to the first queue to seed input
    queues[0].add(0);

    var amplifiers = mutableListOf<Amplifier>();
    for (i in 0..4) {
        var inputs = queues[i];
        var outputs = queues[i+1];

        var amplifier = Amplifier(sourceCodes, inputs, outputs);
        amplifiers.add(amplifier);

        println("$amplifier is $i");
    }
    return amplifiers;
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

var counter = 5;

/* Figure out if any amplifier is still running */
fun MoreProcessing(amplifiers: List<Amplifier>): Boolean {
    if (counter++ <= 0) {
        println("Counter is dead");
        return false;
    }
    for (amplifier in amplifiers) {
        if (!amplifier.isExited() && amplifier.inputs.size > 0) {
            // Process the inputs
            amplifier.opsUntilOutput();
            return true;
        }
    }
    return false;
}

fun ProcessAmplifiers(sourceCodes: List<Int>, phases: List<Int>): Int {
    var amplifiers = SetupAmplifiers(sourceCodes, phases);

    for (amplifier in amplifiers) {
        // Run once to process the phase values
        amplifier.ops();
    }

    while (MoreProcessing(amplifiers)) {
        println("Thinking...");
    }

    // now while any amplifier is awake and has input, go process those ops and continue until it creates an output.
    for (amplifier in amplifiers) {
        println("$amplifier has output on ${amplifier.outputs}");
    }

    return amplifiers[4].outputs[0];
}

fun main(args: Array<String>) {
    if (args.size == 1) {
        var ops = args[0];
        var sourceCodes = Loadcodes(ops);

        var sourcePhases: List<List<Int>> = permute(listOf(5,6,7,8,9));
        // I need to vary all of the phase combinations
        var highest = 0;
        for (phaseCombo in sourcePhases) {
            var input = ProcessAmplifiers(sourceCodes, phaseCombo);
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

        println(ProcessAmplifiers(sourceCodes, sourcePhases));

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
