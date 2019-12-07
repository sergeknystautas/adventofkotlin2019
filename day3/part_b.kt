import kotlin.math.abs;

/*
 Takes a sequence like U7,R6,D4,L4 and creates a sequence of spots this goes thru like...
 []"1,0","2,0","3,0", etc...]
 */
fun followWire(instructions: String): List<String> {
    // Assume we start at 0,0
    var pos = Pair(0,0);
    var path = mutableListOf<String>();

    var commands = instructions.split(",");
    for (command in commands) {
        // println(command);
        var delta = Direction(command.substring(0, 1).toUpperCase());
        var counter:Int = command.substring(1).toInt();
        while (counter > 0) {
            pos = Pair(pos.first + delta.first, pos.second + delta.second);
            var label = "${pos.first},${pos.second}";
            // println(label);
            path.add(label);
            counter--;
        }
    }
    // path.remove("0,0");
    // println(path.size);
    return path;
}

fun Direction(letter: String): Pair<Int,Int> {
    when (letter) {
        "R" -> return Pair(1,0);
        "L" -> return Pair(-1,0);
        "U" -> return Pair(0,1);
        "D" -> return Pair(0,-1);
    }
    return Pair(0,0);
}

fun Distance(pair: String): Int {
    var coords: List<Int> = pair.split(",").map{ abs(it.toInt()) };
    return coords[0] + coords[1];
}

fun Steps(pair: String, wire1: List<String>, wire2: List<String>): Int {
    // Find the indices of both and add them up.
    var index1 = wire1.indexOf(pair) + 1;
    var index2 = wire2.indexOf(pair) + 1;
    return index1 + index2;
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("You done messed up a-a-ron");
        return;
    }
    var red = args[0];
    var blue = args[1];

    var redPath = followWire(red);
    var bluePath = followWire(blue);

    var crosses = redPath.intersect(bluePath);
    // println(crosses);
    var distances = crosses.map{ Steps(it, redPath, bluePath); }
    // println(distances);
    println(distances.min());
}
