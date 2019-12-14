package aoc2019.day12;

import java.io.File;
import kotlin.system.exitProcess;
import kotlin.math.abs;
import kotlin.math.max;
import kotlin.math.pow;

fun Loadcodes(filename: String) : List<String> {
    return File(filename).readLines();
}

fun Splitcodes(opcodes: String, delim: String): List<Int> {
    // println("you gave ops of $opcodes");
    var ops: List<Int> = opcodes.split(delim).map{ it.trim().toInt() };
    // println("This is ${ops.size} instructions");
    return ops;
}

class Moon(_pos: Triple<Int, Int, Int>, _moons: List<Moon>) {
    var pos = _pos;
    var originalPos = _pos;
    var velocity = Triple(0, 0, 0);
    var moons = _moons;
    init {

    }

    fun StepGravity() {
        // apply gravity to velocity
        var deltaX = 0;
        var deltaY = 0;
        var deltaZ = 0;
        // println("Checking against ${moons.size}");
        for (moon in moons) {
            deltaX = deltaX + Delta(pos.first, moon.pos.first);
            deltaY = deltaY + Delta(pos.second, moon.pos.second);
            deltaZ = deltaZ + Delta(pos.third, moon.pos.third);
            // println("----");
        }
        // println("Changing as follows: $deltaX, $deltaY, $deltaZ");
        velocity = Triple(velocity.first + deltaX, velocity.second + deltaY, velocity.third + deltaZ);
    }

    fun StepMove() {
        // apply velocity to pos
        pos = Triple(pos.first + velocity.first, pos.second + velocity.second, pos.third + velocity.third);
    }

    fun Delta(axis: Int, otherAxis: Int): Int {
        var result = 0;
        if (axis < otherAxis) {
            result = 1;
        } else if (axis > otherAxis) {
            result = -1;
        }
        return result;
    }

    fun Energy(): Int {
        return PotentialEnergy() * KineticEnergy();
    }

    fun PotentialEnergy(): Int {
        return abs(pos.first) + abs(pos.second) + abs(pos.third);
    }

    fun KineticEnergy(): Int {
        return abs(velocity.first) + abs(velocity.second) + abs(velocity.third);
    }

    override fun toString(): String {
        return "pos=<x=${pos.first}, y=${pos.second}, z=${pos.third}>, vel=<x=${velocity.first}, y=${velocity.second}, z=${velocity.third}>";
    }
}

fun stripChars(s: String, r: String) = s.replace(Regex("[$r]"), "")

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("You done messed up a-a-ron");
        println("Usage: cmd filename");
        exitProcess(-1);
    }
    var lines = Loadcodes(args[0]);
    // <x=5, y=4, z=4>
    // <x=-11, y=-11, z=-3>
    var moons = mutableListOf<Moon>();
    for (line in lines) {
        var positions = Splitcodes(stripChars(line,"<>xyz="), ",");
        // println(positions);
        var position = Triple(positions[0], positions[1], positions[2]);
        moons.add(Moon(position, moons));
    }
    /*
    println("After 0 steps:");
    for (moon in moons) {
        println(moon);
    }
    */
    for (i in 1..10) {
        for (moon in moons) {
            moon.StepGravity();
        }
        for (moon in moons) {
            moon.StepMove();
        }
        /*
        println("After $i step:");
        for (moon in moons) {
            println(moon);
        }
        */
    }
    var energy = 0;
    for (moon in moons) {
        println("Moon energy: ${moon.Energy()}");
        energy += moon.Energy();
    }
    println("Total: $energy");
    // println(lines);
    // Now find the repeats X

    var matches = mutableListOf<Long>();
    matches.add(FindLoop(lines, 0).toLong());
    matches.add(FindLoop(lines, 1).toLong());
    matches.add(FindLoop(lines, 2).toLong());
    println("$matches");
    var primes = Primes(matches.max<Long>()?:1L);
    println(primes);
    var divisors = mutableMapOf<Long, Int>();
    AddDivisors(matches[0], primes, divisors);
    AddDivisors(matches[1], primes, divisors);
    AddDivisors(matches[2], primes, divisors);
    println("Total is ${divisors}");
    var lcm = 1.0;
    divisors.forEach{ (divisor, power) -> lcm *= divisor.toDouble().pow(power)};
    println(lcm.toLong());
}
//
fun AddDivisors(_number: Long, primes: List<Long>, divisors: MutableMap<Long, Int>) {
    var number = _number;
    var thisDivisors = mutableMapOf<Long, Int>();
    // var numDivisors = mutableListOf<Long<();
    var count = 0;
    while (count < primes.size) {
        var prime = primes[count];
        if (number % prime == 0L) {
            // println("  ${prime} is a divisor");
            number /= prime;
            var divisorCount = thisDivisors.getOrDefault(prime, 0);
            thisDivisors.put(prime, ++divisorCount);
        } else {
            count++;
        }
    }
    thisDivisors.forEach {
        (prime, divisor) ->
            var divisorCount = max(divisors.getOrDefault(prime, 0), divisor);
            divisors.put(prime, divisorCount);
    }
    // println("${_number} divides into ${thisDivisors}");
}

// List primes up to a number
fun Primes(limit: Long): ArrayList<Long>{
    var nums: ArrayList<Long> = ArrayList();
    for(i in 2..limit){
        var j: Boolean = true
        for (i2 in 2..(i-1)) {
            if (i % i2 == 0L) {
                j=false
                break
            }
        }
        if (j) {
            nums.add(i.toLong());
        }
    }
    return nums;
}

fun FindLoop(lines: List<String>, axis: Int): Int {
    var moons = mutableListOf<Moon>();
    for (line in lines) {
        var positions = Splitcodes(stripChars(line,"<>xyz="), ",");
        // println(positions);
        var position = Triple(positions[0], positions[1], positions[2]);
        moons.add(Moon(position, moons));
    }
    var matched = -1;
    var count = 0;
    loop@ while (matched == -1) {
        count++;
        for (moon in moons) {
            moon.StepGravity();
        }
        for (moon in moons) {
            moon.StepMove();
        }
        for (moon in moons) {
            if (moon.pos.toList()[axis] != moon.originalPos.toList()[axis] || moon.velocity.toList()[axis] != 0) {
                continue@loop;
            }
        }
        matched = count;
    }
    return matched;
}
