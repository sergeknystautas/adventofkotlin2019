package aoc2019.dday11;

import java.io.File;
import kotlin.system.exitProcess;
import kotlin.math.min;
import kotlin.math.max;

val UP = Pair(0, -1);
val RIGHT = Pair(1, 0);
val DOWN = Pair(0, 1);
val LEFT = Pair(-1, 0);
val TURN_LEFT = mapOf(UP to LEFT, LEFT to DOWN, DOWN to RIGHT, RIGHT to UP);
val TURN_RIGHT = mapOf(UP to RIGHT, RIGHT to DOWN, DOWN to LEFT, LEFT to UP);

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun Splitcodes(opcodes: String, delim: String): List<Long> {
    // println("you gave ops of $opcodes");
    var ops: List<Long> = opcodes.split(delim).map{ it.trim().toLong() };
    // println("This is ${ops.size} instructions");
    return ops;
}

class Painter constructor (_board: MutableMap<Pair<Int, Int>, Long>, sourceCodes: List<Long>) {
    var board = _board;
    var direction = UP;
    var pos = Pair(0, 0);
    var inputs = mutableListOf<Long>();
    var outputs = mutableListOf<Long>();
    var painter: PaintComputer;
    init {
        painter = PaintComputer(sourceCodes, inputs, outputs);
    }

    fun Run() {
        while (!painter.isExited()) {
            Read();
        }
    }

    fun Read() {
        var color = ReadColor(pos);
        // We pass this to the computer
        inputs.add(color);
        // Now we read this, but we'll use our hardcoded data for now
        painter.opsUntilOutput();
        if (outputs.size == 0 || painter.isExited()) {
            return;
        }
        var paint = outputs.removeAt(0);
        WriteColor(pos, paint);
        painter.opsUntilOutput();
        if (outputs.size == 0 || painter.isExited()) {
            return;
        }
        var move = outputs.removeAt(0);
        when (move) {
            0L -> direction = TURN_LEFT.getValue(direction);
            1L -> direction = TURN_RIGHT.getValue(direction);
        }
        // move forward
        pos = Pair(pos.first + direction.first, pos.second + direction.second);
    }

    fun ReadColor(spot: Pair<Int, Int>): Long {
        if (board.contains(spot)) {
            return board.getValue(spot);
        } else {
            return 0;
        }
    }

    fun WriteColor(spot: Pair<Int, Int>, color: Long) {
        board.put(spot, color);
    }
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

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("You done messed up a-a-ron");
        println("Usage: cmd filename");
        exitProcess(-1);
    }
    var lines = Loadcodes(args[0]);
    var sourceCodes = Splitcodes(lines[0], ",");

    // println(lines);
    var board = mutableMapOf<Pair<Int, Int>, Long>();
    board.put(Pair(0, 0), 1L);
    var painter = Painter(board, sourceCodes);
    painter.Run();
    var minX = 0;
    var minY = 0;
    var maxX = 0;
    var maxY = 0;
    for (spot in board.keys) {
        minX = min(minX, spot.second);
        minY = min(minY, spot.first);
        maxX = max(maxX, spot.second);
        maxY = max(maxY, spot.first);
    }
    var printout = Array(maxX - minX + 1) {Array(maxY - minY + 1) {" "} };
    for (spot in board) {
        var x = spot.key.second - minX;
        var y = spot.key.first - minY;
        if (spot.value != 0L) {
            printout[x][y] = "0";
        }
    }
    for (row in printout) {
        println(row.joinToString(""));
    }
    // println(board.filter{ it.value != 0L});
    println(board.size);

}
