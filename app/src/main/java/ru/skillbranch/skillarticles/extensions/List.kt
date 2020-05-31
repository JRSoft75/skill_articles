package ru.skillbranch.skillarticles.extensions

fun List<Pair<Int, Int>>.groupByBounds(list: List<Pair<Int, Int>> ): List<MutableList<Pair<Int, Int>>>{
    val result: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
    var currentList : MutableList<Pair<Int, Int>>

    var lastIndex = 0
    list.forEach{
        val (boundStart, boundEnd) = it
        val (a, b ) = this[lastIndex]
        while(boundStart < a || boundEnd > b){ //если начало Bound меньше начала диапазона или конец Bound больше конца диапазона, значит диапазон входит в Bounds
            lastIndex++ //переход на следующую пару
            currentList = mutableListOf()
            currentList.add(it)
            if(boundEnd in a..b){ //последний элемент группы
                result.add(currentList)
                break
            }
        }

    }
    return result
}