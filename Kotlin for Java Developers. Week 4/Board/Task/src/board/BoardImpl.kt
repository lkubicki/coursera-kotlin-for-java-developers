package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    private var cells =
            (1..width).map { i -> (1..width).map { j -> Cell(i, j) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i in 1..width) {
            if (j in 1..width) {
                return cells[i - 1][j - 1]
            }
        }
        return null;
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j)!!
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatMap { values -> values }
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return removeRangeOutOfBoard(jRange).map { j -> getCell(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return removeRangeOutOfBoard(iRange).map { i -> getCell(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP -> getCellOrNull(this.i - 1, j)
                DOWN -> getCellOrNull(this.i + 1, j)
                LEFT -> getCellOrNull(this.i, j - 1)
                RIGHT -> getCellOrNull(this.i, j + 1)
            }


    private fun removeRangeOutOfBoard(jRange: IntProgression): IntProgression {
        var lowerBound = jRange.first
        var higherBound = jRange.last
        if (lowerBound < 1) {
            lowerBound = 1
        }
        if (lowerBound > width) {
            lowerBound = width
        }
        if (higherBound < 1) {
            higherBound = 1
        }
        if (higherBound > width) {
            higherBound = width
        }

        if (lowerBound < higherBound) {
            return lowerBound..higherBound
        } else {
            return lowerBound downTo higherBound
        }
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val board = getAllCells()
            .map { Pair(it, null) }
            .toMap<Cell, T?>()
            .toMutableMap()

    override fun get(cell: Cell): T? {
        return board[cell]
    }

    override fun set(cell: Cell, value: T?) {
        board[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return board.entries.filter { predicate(it.value) }.map { it.key }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return board.entries.find { predicate(it.value) }?.key
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return board.entries.any { predicate(it.value) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return board.entries.all { predicate(it.value) }
    }

}