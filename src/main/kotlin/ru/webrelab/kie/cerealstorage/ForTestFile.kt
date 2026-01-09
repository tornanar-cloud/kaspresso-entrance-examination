package ru.webrelab.kie.cerealstorage

fun main() {
    val c = CerealStorageImpl(1f, 2f).apply {
        println(addCereal(Cereal.RICE,1.1f))
        println(addCereal(Cereal.BUCKWHEAT,1.1f))
        println(toString())
    }




    val c1 = CerealStorageImpl(10f, 55f)
    println(c.toString())
    //println(c1.toString())
}
