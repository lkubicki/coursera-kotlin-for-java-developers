package nicestring

fun String.isNice(): Boolean {
    fun String.containsTabuSubstring(): Boolean {
        return this.contains(Regex("b[uae]+"))
    }

    fun String.containsThreeVowels(): Boolean {
        val vowels = listOf('a', 'e', 'i', 'o', 'u')
        return this.filter { vowels.contains(it) }.count() >= 3
    }

    fun String.containsDuplicates(): Boolean {
        if (this.length > 1) {
            for (idx in 1 until this.length) {
                if (this[idx - 1] == this[idx])
                    return true
            }
        }
        return false
    }

    return listOf(!this.containsTabuSubstring(),
            this.containsThreeVowels(),
            this.containsDuplicates())
            .count({ it }) >= 2
}