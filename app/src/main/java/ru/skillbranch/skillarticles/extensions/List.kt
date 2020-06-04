package ru.skillbranch.skillarticles.extensions

fun List<Pair<Int, Int>>.groupByBounds(list: List<Pair<Int, Int>>): List<MutableList<Pair<Int, Int>>> {
    val result: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
    var currentList: MutableList<Pair<Int, Int>>
    var lastIndex = 0
    list.forEach {
        val (boundStart, boundEnd) = it  //5443, 6130
        currentList = mutableListOf()
        if (lastIndex <= this.size - 1) {
            var (listStart, listEnd) = this[lastIndex] // 5766, 5776
            while (listStart in boundStart..boundEnd || listEnd in boundStart..boundEnd) { //если начало диапазона или конец входят в Bounds
                if (boundEnd in listStart..listEnd) { //последний элемент группы
                    currentList.add(listStart to boundEnd) //
                    if (boundEnd == listEnd) {
                        lastIndex++ //переход на следующую пару 4
                    }
                    //иначе остаемся на этой паре и берем следующий диапазон
                    break
                } else if (boundStart in listStart..listEnd) {
                    currentList.add(boundStart to listEnd) //
                } else {
                    currentList.add(this[lastIndex]) //добавляем пару как есть
                }
                lastIndex++ //переход на следующую пару 1
                if (lastIndex <= this.size - 1) {  //size=5
                    listStart = this[lastIndex].first //22
                    listEnd = this[lastIndex].second //30
                } else {
                    result.add(currentList)
                    return@forEach
                }
            }
        }
        result.add(currentList)
    }
    return result
}