package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        this.allDrivers.subtract(this.trips.map({ it.driver }))

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if(minTrips > 0) {
            this.trips
                    .flatMap { trip ->
                        trip.passengers
                                .map({ passenger -> passenger to trip })
                    }.groupBy { passengersTrips -> passengersTrips.first }
                    .filter { group -> group.value.size >= minTrips }
                    .keys
        } else allPassengers

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.trips
                .filter { trip -> trip.driver == driver }
                .flatMap { trip ->
                    trip.passengers
                            .map({ passenger -> passenger to trip })
                }.groupBy { passengersTrips -> passengersTrips.first }
                .filter { group -> group.value.size > 1 }
                .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        this.trips
                .flatMap { trip -> trip.passengers.map { passenger -> passenger to trip } }
                .groupBy { passengersTrips -> passengersTrips.first }
                .mapValues { (_, trips) -> trips.map { trip -> trip.second } }
                .mapValues { (_, trips) -> trips.partition { trip -> (trip.discount ?: 0.0) > 0.0 } }
                .filter { (_, partitionedTrips) -> partitionedTrips.first.size > partitionedTrips.second.size }
                .keys


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val rangeStart = this.trips
            .groupBy { trip -> trip.duration / 10 }
            .maxBy { (_, durations) -> durations.size }
            ?.key
    if (rangeStart != null) {
        return rangeStart.times(10).rangeTo(rangeStart * 10 + 9)
    }
    return null
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (!trips.isEmpty()) {
        val incomeSortedByDescending = trips.groupBy { trip -> trip.driver }
                .mapValues { (_, driversTrips) -> driversTrips.sumByDouble { trip -> trip.cost } }
                .entries
                .sortedByDescending { (_, driversIncome) -> driversIncome }
        val sumOfAll = incomeSortedByDescending.sumByDouble { it.value }
        val sumOf20Percent = incomeSortedByDescending
                .take(allDrivers.size / 5)
                .sumByDouble { it.value }
        return sumOf20Percent >= sumOfAll * 0.8
    }
    return false
}