package ru.webrelab.kie.cerealstorage

import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.min

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }



    private val storage = mutableMapOf<Cereal, Float>()
    /**
     * Добавляет крупу к существующему контейнеру соответствующего типа, либо добавляет новый контейнер
     * если его ещё не было в хранилище и добавляет в него предоставленную крупу.
     * Для одного вида крупы только один контейнер.
     * @param cereal крупа для добавления в контейнер
     * @param amount количество добавляемой крупы
     * @return количество оставшейся крупы если контейнер заполнился
     * @throws IllegalArgumentException если передано отрицательное значение
     * @throws IllegalStateException если хранилище не позволяет разместить ещё один контейнер для новой крупы
     */
    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Значение не может быть отрицательным" }
        //  Рассматриваю кейс, когда контейнера с крупой нет + проверяю - могу ли я добавить контейнер с крупой
        var tmp = -0.1f
        if (!storage.contains(cereal)) {
            // Проверяю можно ли добавить еще один контейнер
            check(storageCapacity - storage.size.toFloat() * containerCapacity >= containerCapacity) {
                "Хранилище не позволяет разместить ещё один контейнер для новой крупы. В хранилище нет места для размещения контейнера"
            }
            val dif = containerCapacity - amount
            storage[cereal] = min(containerCapacity, amount)
            tmp = amount - getAmount(cereal)
            /* tmp = if ((dif) < 0 ) {
                //storage[cereal] = containerCapacity
                abs(dif)
            } else {
                //storage[cereal] = amount
                0.0f
            }
            */
        } else{
            val dif = min(getSpace(cereal), amount) // е 5 х 15
            storage[cereal] = getAmount(cereal) + dif
            tmp = amount - dif
        }
        check(tmp > 0.0f){"Что-то не так написал в методе"}
        return tmp
    }


    /**
     * Вынимает крупу из контейнера (после этого в контейнере крупы должно стать меньше)
     * @param cereal крупа, которую нужно взять из контейнера
     * @param amount количество крупы
     * @return количество полученной крупы или остаток содержимого контейнера, если крупы в нём было меньше
     * @throws IllegalArgumentException если передано отрицательное значение
     */
    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(storage.contains(cereal)) { "Такого контейнера с крупой нет в хранилище" }
        require(amount >= 0.0f) { "Количество не может быть отрицательным" }
        val currentAmount = getAmount(cereal)
        /*if (currentAmount >= amount) {
            storage[cereal] = currentAmount - amount
            return amount
        } else {
            storage[cereal] = 0.0f
            return currentAmountэ
        }
        */
        val tmp = min(currentAmount, amount)
        storage[cereal] = currentAmount - tmp
        return tmp
    }


    /**
     * @param cereal уничтожает пустой контейнер
     * @return true если контейнер уничтожен и false если контейнер не пуст или отсутствует
     */
    override fun removeContainer(cereal: Cereal): Boolean {
        return (storage[cereal] == 0.0f) && storage.remove(cereal) != null
        //return if(storage[cereal] == 0.0f){ storage.remove(cereal); true} else false
    }


    /**
     * @param cereal крупа, количество которой нужно узнать
     * @return количество крупы, которое хранится в контейнере или 0 если контейнера нет
     */
    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0.0f)
    }


    /**
     * @param cereal крупа, для которой нужно проверить доступное место в контейнере
     * @return количество крупы, которое может вместить контейнер с учётом его текущей заполненности
     * @throws IllegalStateException если проверяемого контейнера нет
     */
    override fun getSpace(cereal: Cereal): Float {
        check(storage.contains(cereal)) { "Проверяемого контейнера нет" }
        //return containerCapacity - storage.getOrElse(cereal){ throw IllegalStateException("проверяемого контейнера нет")}
        //return containerCapacity - (storage[cereal] ?: throw IllegalStateException("проверяемого контейнера нет"))
        return containerCapacity - getAmount(cereal)
    }


    /**
     * @return текстовое представление
     */
    override fun toString(): String {
        return if (storage.isEmpty()) {
            "Хранилище пусто"
        } else {
            "Содержание:\n" +
                    storage.map { it }
                        .joinToString("\n") { "Контейнер - \"${it.key}\" : Количество крупы - ${it.value}" }
        }
    }

}
