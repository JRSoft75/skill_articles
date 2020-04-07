package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf( substr: String?, ignoreCase: Boolean = true): List<Int> {

    var lastIndex = 0
    val result: MutableList<Int> = ArrayList()
    val sourseText: String
    val findStr: String

    this ?: return emptyList()
    if(substr.isNullOrBlank()) { return emptyList() }
    if (ignoreCase) {
        sourseText = this.toLowerCase()
        findStr = substr.toLowerCase()
    } else {
        sourseText = this
        findStr = substr
    }
    while (lastIndex != -1) {
        lastIndex = sourseText.indexOf(findStr, lastIndex)
        if (lastIndex != -1) {
            result.add(lastIndex)
            lastIndex += 1
        }
    }

    return result
    }
