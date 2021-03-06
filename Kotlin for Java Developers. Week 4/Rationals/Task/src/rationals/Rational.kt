package rationals

import java.math.BigInteger

infix fun Int.divBy(denominator: Int): Rational = Rational(this.toBigInteger(), denominator.toBigInteger())
infix fun Long.divBy(denominator: Long): Rational = Rational(this.toBigInteger(), denominator.toBigInteger())
infix fun BigInteger.divBy(denominator: BigInteger): Rational = Rational(this, denominator)
fun String.toRational(): Rational {
    require(this.isNotEmpty())
    val split = this.split("/", limit = 2)
    if(split.size > 1) {
        return Rational(split[0].toBigInteger(), split[1].toBigInteger())
    } else {
        return Rational(split[0].toBigInteger(), BigInteger.ONE)
    }
}

class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {

    init {
        require(denominator != BigInteger.ZERO)
    }

    override fun compareTo(other: Rational): Int =
            (numerator * other.denominator).compareTo(other.numerator * denominator)

    operator fun plus(other: Rational): Rational {
        return Rational(this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator).normalize()
    }

    operator fun minus(other: Rational): Rational {
        return this + -other
    }

    operator fun times(other: Rational): Rational {
        return Rational(this.numerator * other.numerator,
                this.denominator * other.denominator).normalize()
    }

    operator fun div(other: Rational): Rational {
        return Rational(this.numerator * other.denominator,
                this.denominator * other.numerator).normalize()
    }

    operator fun unaryMinus(): Rational {
        return Rational(-this.numerator, this.denominator).normalize()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        val first = this.normalize()
        val second = other.normalize()

        if (first.numerator != second.numerator) return false
        if (first.denominator != second.denominator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    override fun toString(): String {
        val normalized = this.normalize()
        if(normalized.denominator == BigInteger.ONE) {
            return "${normalized.numerator}"
        } else {
            return "${normalized.numerator}/${normalized.denominator}"
        }
    }

    private fun normalize(): Rational {
        val commonDivisor = this.numerator.gcd(this.denominator)
        if (this.denominator < BigInteger.ZERO) {
            return Rational(-this.numerator / commonDivisor, -this.denominator / commonDivisor)
        } else {
            return Rational(this.numerator / commonDivisor, this.denominator / commonDivisor)
        }
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

