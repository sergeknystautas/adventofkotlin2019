package day1;

import java.util.Date;
import java.io.File;
/* First post! */

fun FuelCounter(mass: Int): Int {
    return (mass / 3) - 2;
}

fun FuelCounterWithFuel(mass: Int): Int {
    var sum: Int = FuelCounter(mass);
    var toWeigh: Int = sum
    do {
        var extraFuel:Int = FuelCounter(toWeigh);
        if (extraFuel > 0) {
            sum += extraFuel;
            // println("Carrying $toWeigh needs extra fuel of $extraFuel bringing us up to $sum");
        } else {
            // println("Carrying $toWeigh takes no more fuel");
        }
        toWeigh = extraFuel;
    } while (extraFuel > 0)
    return sum;
}


fun main(args: Array<String>) {
    if (args.size == 0) {
        var sum: Int = 0;
        File("masses.txt").forEachLine {
            var mass = it.toInt();
            var fuel = FuelCounterWithFuel(mass);
            // println("mass $mass needs fuel $fuel");
            println(fuel);
            sum+= fuel;
        }
        println(sum);
        // println("Please specify the mass");
    } else {
        var sum: Int = 0;
        for (arg in args) {
            var mass = arg.toInt();
            var fuel = FuelCounterWithFuel(mass);
            println("mass $mass needs fuel $fuel");
            sum += fuel;
        }
        println(sum);
    }
    // var now = Date();
    // println("Now is $now");
}
