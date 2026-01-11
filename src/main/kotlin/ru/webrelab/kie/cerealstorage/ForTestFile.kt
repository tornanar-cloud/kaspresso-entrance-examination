package ru.webrelab.kie.cerealstorage

fun main() {
    val c = CerealStorageImpl(20f, 60f).apply {
        //println(addCereal(Cereal.RICE,1.1f))
        addCereal(Cereal.BUCKWHEAT,5f)
        println(toString())
    }
    println(c.addCereal(Cereal.BUCKWHEAT, 15f))
    println(c.toString())




    //val c1 = CerealStorageImpl(10f, 55f)
    //println(c.toString())
    //println(c1.toString())
}
