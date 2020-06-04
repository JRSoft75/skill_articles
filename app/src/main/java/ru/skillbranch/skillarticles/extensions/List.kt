package ru.skillbranch.skillarticles.extensions

fun List<Pair<Int, Int>>.groupByBounds(list: List<Pair<Int, Int>> ): List<MutableList<Pair<Int, Int>>>{
    val result: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
    var currentList : MutableList<Pair<Int, Int>>

    var lastIndex = 0
    list.forEach{
        var (boundStart, boundEnd) = it  //60-100
        currentList = mutableListOf()

        var (listStart, listEnd ) = this[lastIndex] // 70-100
        while(listStart in boundStart..boundEnd || listEnd in boundStart..boundEnd){ //если начало диапазона или конец входят в Bounds
            if(boundEnd in listStart..listEnd){ //последний элемент группы
                currentList.add(listStart to boundEnd) //
                result.add(currentList)
                if(boundEnd == listEnd){
                    lastIndex++ //переход на следующую пару 3
                }
                //иначе остаемся на этой паре и берем следующий диапазон
                break
            }else if(boundStart in listStart..listEnd){
                currentList.add(boundStart to listEnd) //
                lastIndex++ //переход на следующую пару 2
            }else{
                currentList.add(it) //добавляем пару как есть
                lastIndex++ //переход на следующую пару 2
            }

            listStart= this[lastIndex].first //22
            listEnd= this[lastIndex].second //30
        }
/**
 * (2,5)  (8,10)
 * (10,20) 22-30
 * 45-50
 *
 *
* */
    }
    return result
}