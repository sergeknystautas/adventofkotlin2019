import java.io.File;

fun LoadOrbits(filename: String) : List<String> {
    return File(filename).readLines();
}

fun MapOrbits(lines: List<String>): Map<String, String> {
    var orbits = lines.associateBy (
        { it.split(")")[1]},
        { it.split(")")[0]}
        );
    return orbits;
}

fun CountOrbit(orbitsCount: MutableMap<String, Int>, orbitsMap: Map<String, String>, body: String): Int {
    if (body.equals("COM")) {
        return 0;
    }
    if (orbitsCount.containsKey(body)) {
        return orbitsCount.getValue(body);
    } else {
        var around: String = orbitsMap.getValue(body);
        var count = CountOrbit(orbitsCount, orbitsMap, around) + 1;
        orbitsCount.put(body, count);
        return count;
    }
}

fun StepsToCom(orbitsMap: Map<String, String>, body: String): List<String> {
    var steps = mutableListOf<String>();
    if (!body.equals("COM")) {
        steps.addAll(StepsToCom(orbitsMap, orbitsMap.getValue(body)));
    }
    steps.add(body);
    return steps;
}

fun main(args: Array<String>) {
    if (args.size == 1) {
        var orbitsLines = LoadOrbits(args[0]);
        var orbitsMap = MapOrbits(orbitsLines);
        var orbitsCount = mutableMapOf<String, Int>();
        println(orbitsMap);
        for (orbit in orbitsMap) {
            var count = CountOrbit(orbitsCount, orbitsMap, orbit.key);
            println("${orbit.key} to ${orbit.value} with count ${count}");
        }

        var sum = orbitsCount.values.sumBy{ it };
        println(sum);

        var you = StepsToCom(orbitsMap, "YOU");
        var san = StepsToCom(orbitsMap, "SAN");
        var position = 0;
        // Shouldn't hit an exception because you'll reach YOU or SAN as the end state
        while (you[position].equals(san[position])) {
            position++;
        }
        var youToSanta = orbitsCount.getValue("YOU") + orbitsCount.getValue("SAN") - position * 2;
        println(you);
        println(san);
        println(position);
        println(youToSanta);
    } else {
        println("Ya done messed up a-a-ron!");
    }
}
