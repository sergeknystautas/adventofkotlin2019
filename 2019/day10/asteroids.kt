package aoc2019.day10;

import java.io.File;
import kotlin.system.exitProcess;
import kotlin.math.hypot;
import kotlin.math.atan;
import kotlin.math.PI;
// import kotlin.Double.NaN;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun Splitcodes(opcodes: String, delim: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(delim).map{ it.trim().toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

fun ListAsteroids(lines: List<String>): List<Pair<Int, Int>> {
    var asteroids = mutableListOf<Pair<Int, Int>>();
    for (i in 0..lines.size - 1) {
        for (j in 0..lines[i].length - 1) {
            if (lines[i][j] == '#') {
                asteroids.add(Pair(j, i));
            }
        }
    }
    return asteroids;
}

fun GetDistances(asteroids: List<Pair<Int, Int>>, station: Pair<Int, Int>): List<Double> {
    var distances = mutableListOf<Double>();
    for (asteroid in asteroids) {
        if (asteroid == station) {
            distances.add(0.0);
        } else {
            distances.add(hypot((asteroid.first - station.first).toDouble(), (asteroid.second - station.second).toDouble()));
        }
    }
    return distances;
}

fun GetRadians(asteroids: List<Pair<Int, Int>>, station: Pair<Int, Int>): List<Double> {
    var radians = mutableListOf<Double>();
    for (asteroid in asteroids) {
        if (asteroid == station) {
            radians.add(Double.NaN);
        } else {
            radians.add(Radians(asteroid, station));
        }
    }
    return radians;
}

fun ScoreStation(asteroids: List<Pair<Int, Int>>, station: Pair<Int, Int>): Int {
    //
    // println("Scoring $station");
    var distances = GetDistances(asteroids, station);
    var radians = GetRadians(asteroids, station);
    // Now we have all of the distances and radians so we can determine who is in between
    var count = 0;
    for (i in 0..asteroids.size - 1) {
        var visible = true;
        var asteroid = asteroids[i];
        // We iterate thru all to see if anything blocks it
        for (j in 0..asteroids.size - 1) {
            if (i == j) {
                // We skip ourselves
                continue;
            }
            if (asteroid == station) {
                // Skip the station
                visible = false;
                continue;
            }
            // If the angle is the same, and it's further away than this, it's not visible
            if (radians[i] == radians[j] && distances[i] > distances[j]) {
                // println("Cannot see ${asteroids[i]} blocked by ${asteroids[j]}");
                visible = false;
                break;
            }
        }
        if (visible) {
            // println("Can see $asteroid with angle ${radians[i]}");
            count++;
        } else {
            // println("Cannot see $asteroid with angle ${radians[i]}");
        }
    }

    return count;
}

fun ShootAsteroids(asteroids: List<Pair<Int, Int>>, station: Pair<Int, Int>): Int {
    var distances = GetDistances(asteroids, station);
    var radians = GetRadians(asteroids, station);
    var radiansMapped = mutableMapOf<Double, MutableList<Pair<Int, Int>>>();
    var distancesMapped = mutableMapOf<Pair<Int, Int>, Double>();
    asteroids.forEachIndexed { index, it ->
        // Create map of each radian to asteroid pairs
        var mapped = radiansMapped.getOrDefault(radians[index], mutableListOf<Pair<Int, Int>>());
        mapped.add(it);
        radiansMapped.put(radians[index], mapped);
        distancesMapped.put(it, distances[index]);
    };
    radiansMapped.remove(Double.NaN);
    // Order the asteroids by the distances
    for (asteroids in radiansMapped.values) {
        asteroids.sortBy { distancesMapped[it] };
    }
    // Get all of the radians in order
    var radiansSorted = radiansMapped.keys.toMutableList();
    radiansSorted.sortDescending();

    // Now sort by distances
    // radiansMapped.forEach{ println(it)};
    // distancesMapped.forEach{ println(it)};
    // radiansSorted.forEach{ println(it)};

    var next = -PI / 2;
    var shot = 0;
    do {
        var possible = radiansSorted.filter { it < next };
        if (possible.size > 0) {
            next = possible.first();
            var shotAsteroid = radiansMapped.getValue(next).removeAt(0);
            if (radiansMapped.getValue(next).size == 0) {
                radiansMapped.remove(next);
                radiansSorted.remove(next);
            }
            println("Shot ${shot + 1} is $shotAsteroid");
            shot++;
        } else {
            // Start over at the top
            next = PI * 2;
            // Clean out radians mapped
            // println("before cleaning ${radiansMapped.size}");
            // radiansMapped = radiansMapped.filter { it.value.size > 0 }.toMutableMap();
            // println("after cleaning ${radiansMapped.size}");
        }
    } while (shot < 200 && radiansMapped.size > 0);
    return 0;
}

fun Radians(station: Pair<Int, Int>, asteroid: Pair<Int, Int>): Double {
    // println("Comparing $asteroid to $station");
    var x = asteroid.first - station.first;
    var y = -(asteroid.second - station.second);
    // System.out.println("$x $y");
    if (x == 0 && y == 0) {
        println("You calculated the radians of the same point $asteroid");
        exitProcess(-1);
    } else if (x == 0 && y > 0) {
        return PI * 0.50;
    } else if (x == 0 && y < 0) {
        return PI * 1.50;
    } else if (x > 0) {
        return atan(y.toDouble() / x.toDouble());
    } else {
        return atan(y.toDouble() / x.toDouble()) + PI;
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("You done messed up a-a-ron");
        println("Usage: cmd filename");
        exitProcess(-1);
    }
    // Load the data
    var lines = Loadcodes(args[0]);
    // println(lines);

    // Find the asteroids
    var asteroids = ListAsteroids(lines);
    // println(asteroids);

    // Score the site
    var mostCount = 0;
    var most: Pair<Int, Int> = Pair(-1, -1);
    for (station in asteroids) {
        // println("Checking asteroid $station as possible station...");
        var count = ScoreStation(asteroids, station);
        println("Asteroid $station can see $count");
        if (count > mostCount) {
            mostCount = count;
            most = station;
        }
    }
    println("$most can see $mostCount");
    /*
    var center = Pair(0, 0);
    println("Right: ${Radians(center, Pair(1,0))}");
    println("Right-down: ${Radians(center, Pair(1,1))}");
    println("Down: ${Radians(center, Pair(0,1))}");
    println("Down-left: ${Radians(center, Pair(-1,1))}");
    println("Left: ${Radians(center, Pair(-1,0))}");
    println("Left-up: ${Radians(center, Pair(-1,-1))}");
    println("Up: ${Radians(center, Pair(0,-1))}");
    println("Up-Right: ${Radians(center, Pair(1,-1))}");
    */
    ShootAsteroids(asteroids, most);

    // println(atan(1.0));
    // println(atan(-1.0));
}
