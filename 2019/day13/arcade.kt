package aoc2019.day13;

import java.io.File;
import kotlin.system.exitProcess;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun Splitcodes(opcodes: String, delim: String): List<Long> {
    // println("you gave ops of $opcodes");
    var ops: List<Long> = opcodes.split(delim).map{ it.trim().toLong() };
    // println("This is ${ops.size} instructions");
    return ops;
}


class PaintComputer constructor (sourceCodes: List<Long>, _inputs: MutableList<Long>, _outputs: MutableList<Long>) {
    val ops = mutableListOf<Long>();
    val inputs = _inputs;
    val outputs = _outputs;
    var pos: Int = 0;
    var relativeBase: Int = 0;
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
        var opcodeAndParam: Int = ops[pos].toInt();
        var opcode = GetDigit(opcodeAndParam, 1) + 10 * GetDigit(opcodeAndParam,10);
        println("Processing $opcode ($opcodeAndParam) on $this");
        if (opcode == 1) {
            // Add
            var arg1 = ops[pos + 1];
            var arg2 = ops[pos + 2];
            var arg3 = ops[pos + 3];
            var var1 = GetValue(arg1, GetDigit(opcodeAndParam, 100));
            var var2 = GetValue(arg2, GetDigit(opcodeAndParam, 1000));
            var sum = var1 + var2;
            WriteValue(arg3, GetDigit(opcodeAndParam, 10000), sum);
            return pos + 4;
        } else if (opcode == 2) {
            // Multiply
            var arg1 = ops[pos + 1];
            var arg2 = ops[pos + 2];
            var arg3 = ops[pos + 3];
            var var1 = GetValue(arg1, GetDigit(opcodeAndParam, 100));
            var var2 = GetValue(arg2, GetDigit(opcodeAndParam, 1000));
            println("Multiply $var1 x $var2");
            var sum = var1 * var2;
            WriteValue(arg3, GetDigit(opcodeAndParam, 10000), sum);
            return pos + 4;
        } else if (opcode == 3) {
            // input
            var var1 = inputs.removeAt(0);
            var arg1 = ops[pos + 1];
            // println("Saving input $var1 to position $arg1");
            WriteValue(arg1, GetDigit(opcodeAndParam, 100), var1);
            return pos + 2;
        } else if (opcode == 4) {
            // output
            var var1 = GetValue(ops[pos + 1], GetDigit(opcodeAndParam, 100));
            outputs.add(var1);
            // println("** Output $var1");
            return pos + 2;
        } else if (opcode == 5) {
            // jump if true
            var var1 = GetValue(ops[pos + 1], GetDigit(opcodeAndParam, 100)).toInt();
            var var2 = GetValue(ops[pos + 2], GetDigit(opcodeAndParam, 1000)).toInt();
            if (var1 != 0) {
                // Jump pointer to this position
                return var2;
            } else {
                return pos + 3;
            }
        } else if (opcode == 6) {
            // jump if false
            var var1 = GetValue(ops[pos + 1], GetDigit(opcodeAndParam, 100)).toInt();
            var var2 = GetValue(ops[pos + 2], GetDigit(opcodeAndParam, 1000)).toInt();
            if (var1 == 0) {
                // Jump pointer to this position
                return var2;
            } else {
                return pos + 3;
            }
        } else if (opcode == 7) {
            // less than
            var arg1 = ops[pos + 1];
            var arg2 = ops[pos + 2];
            var arg3 = ops[pos + 3];
            var var1 = GetValue(arg1, GetDigit(opcodeAndParam, 100));
            var var2 = GetValue(arg2, GetDigit(opcodeAndParam, 1000));
            if (var1 < var2) {
                WriteValue(arg3, GetDigit(opcodeAndParam, 10000), 1);
            } else {
                WriteValue(arg3, GetDigit(opcodeAndParam, 10000), 0);
            }
            return pos + 4;
        } else if (opcode == 8) {
            // equals
            var arg1 = ops[pos + 1];
            var arg2 = ops[pos + 2];
            var arg3 = ops[pos + 3];
            var var1 = GetValue(arg1, GetDigit(opcodeAndParam, 100));
            var var2 = GetValue(arg2, GetDigit(opcodeAndParam, 1000));
            if (var1 == var2) {
                WriteValue(arg3, GetDigit(opcodeAndParam, 10000), 1);
            } else {
                WriteValue(arg3, GetDigit(opcodeAndParam, 10000), 0);
            }
            return pos + 4;
        } else if (opcode == 9) {
            // adjust relative relativeBase
            var arg1 = ops[pos + 1];
            var var1 = GetValue(arg1, GetDigit(opcodeAndParam, 100));
            relativeBase += var1.toInt();
            println("Adjusted relative base to $relativeBase");
            return pos + 2;
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
    fun GetValue(parameter: Long, mode: Int): Long {
        // println("Value of $parameter using mode $mode");
        if (mode == 0) {
            // Look up
            return GetOpsValue(parameter.toInt());
        } else if (mode == 1) {
            // Use this parameter as the value
            return parameter.toLong();
        } else if (mode == 2) {
            // Use the relative base
            return GetOpsValue(relativeBase + parameter.toInt());
        }
        println("Something went wrong with $parameter in mode $mode");
        exitProcess(-1);
    }

    /* Make sure we safely extend the computer memory */
    fun GetOpsValue(position: Int): Long {
        while (ops.size <= position) {
            ops.add(0);
        }
        return ops[position];
    }

    fun WriteValue(position: Long, mode:Int, value: Long) {
        if (mode == 0) {
            // do it there
            WriteOpsValue(position, value);
        } else if (mode == 2) {
            // Write it at a relative position
            WriteOpsValue(relativeBase + position, value);
        } else {
            println("Mode $mode is not allowed for a write");
            exitProcess(-1);
        }

    }

    /* Make sure we safely extend the computer memory */
    fun WriteOpsValue(position: Long, value: Long) {
        while (ops.size <= position) {
            ops.add(0);
        }
        ops[position.toInt()] = value;
    }
}

class DrawCommand(_x: Long, _y: Long, _tile: Long) {
    var x = _x;
    var y = _y;
    var tile = _tile;

    override fun toString(): String {
        return "[$x, $y]";
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("You done messed up a-a-ron");
        println("Usage: cmd filename");
        exitProcess(-1);
    }
    var lines = Loadcodes(args[0]);

    var sourceCodes = Splitcodes(lines[0], ",");
    var inputs = mutableListOf<Long>(2L);
    var outputs = mutableListOf<Long>();
    var painter = PaintComputer(sourceCodes, inputs, outputs);
    while (!painter.isExited()) {
        painter.ops();
    }
    var blocks = mutableListOf<DrawCommand>();
    var currentScore = 0L;
    while (outputs.size > 0) {
        var draw = DrawCommand(outputs.removeAt(0), outputs.removeAt(0), outputs.removeAt(0));
        if (draw.tile == 2L) {
            blocks.add(draw);
        } else if (draw.x == -1L && draw.y == 0L) {
            currentScore = draw.tile;
        }
    }
    println(blocks);
    println(currentScore);
}
