package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.min

class CerealStorageImplTest {
    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should ThrowException When StorageCapacity Is Less Than ContainerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(4f, 3f)
        }
    }

    @ParameterizedTest
    @MethodSource("provideValidValuesForAddCerealWhenCreatingNewContainer")
    fun `shouldAddCerealWhenStorageIsEmptyAndContainerHasEnoughSpace добавление нового контейнера и крупы, когда в хранилище достаточно места для нового контейреа, значение крупы среднее валидное`(
        cer: Cereal,
        am: Float,
        exOv: Float
    ) {
        val actualOverflow = storage.addCereal(cer, am)
        //Проверяю остаток
        assertEquals(
            exOv,
            actualOverflow,
            0.00038f,
            "Остаток не совпадает с ожидаемым. Ожидаем $exOv , фактический $actualOverflow "
        )
        println(exOv)
        //Проверяю, что контейнер добавлен и количество крупы в контейнере. Максимум добавлено может быть containerCpacity, остальное возвращается return
        assertEquals(min(am, storage.containerCapacity), storage.getAmount(cer))
        println(storage.getAmount(cer))

    }

    @Test
    fun shouldCreateNewContainerWhenStorageIsNotEmptyAndHasFreeSpace() {
        storage.addCereal(Cereal.PEAS, 15f)
        storage.addCereal(Cereal.RICE, 12f)
        assertEquals(2, storage.getStorageSize()) { "storage.size не соответствует количеству добавленных контейнеров" }
    }

    @Test
    fun `should throw IllegalStateException when creating container without space`() {
        val v = CerealStorageImpl(9f, 10f)
        v.addCereal(Cereal.PEAS, 9f)
        assertThrows(IllegalStateException::class.java) {
            v.addCereal(Cereal.BUCKWHEAT, 3f)
        }
    }

    @Test
    fun `should Throw Exception When Adding NegativeAmount`(){
        assertThrows(IllegalArgumentException::class.java){
            storage.addCereal(Cereal.RICE, -10f)
        }
    }

    @ParameterizedTest
    @MethodSource("provideExistingContainerWithCereal")
    fun `should add cereal to existing container`(cereal: Cereal, amount: Float) {
        val b  = CerealStorageImpl(20f, 60f)
        b.addCereal(cereal, 5f)
        val preAmount = b.getAmount(cereal)
        println("Количество до добавления $preAmount")
        val storageSize = b.getStorageSize()
        val forPlus = b.getSpace(cereal)
        val ov = b.addCereal(cereal, amount)

        val postAmount = b.getAmount(cereal)

        println("После добавления $postAmount")
        assertEquals(
            postAmount,
            preAmount + minOf(amount,forPlus ),
            0.00038f

        ){"Количество крупы в контейнере не совпадает со значением прошлого + того, что положили "}
        assertEquals(storageSize, b.getStorageSize())
    }

    @Test
    fun `should Return Remaining Amount When Container Overflows`(){
        val c = CerealStorageImpl(5f, 15f)
        val ov1 = c.addCereal(Cereal.RICE, 2f)
        assertEquals(0.0f, ov1){"Остаток должен быть ноль"}
        val ov2 = c.addCereal(Cereal.RICE, 2f)
        assertEquals(0.0f, ov1){"Остаток должен быть ноль"}
        val ov3 = c.addCereal(Cereal.RICE, 31f)
        assertEquals(30f,ov3){"Остаток должен быть 30"}
        val ov4 = c.addCereal(Cereal.BUCKWHEAT, 31f)
        assertEquals(26f,ov4) {"Остаток должен быть 26"}
        val ov5 = c.addCereal(Cereal.BUCKWHEAT, 0f)
        assertEquals(0.0f, ov5){"Остаток должен быть ноль"}
    }


    @Test
    fun `should ThrowException When Getting Negative Amount`()
    {
        storage.addCereal(Cereal.RICE,5f)
        assertThrows(java.lang.IllegalArgumentException::class.java){
            storage.getCereal(Cereal.RICE,-5f)
        }
    }

    @ParameterizedTest
    @MethodSource("provideValuesForShouldReturnCorrectAmountForVariousValues")
    fun `should Return Correct Amount For Various Values`(amount: Float, takenAmount: Float, remainingAmount: Float){
        storage.addCereal(Cereal.BULGUR,5f)
        val takenAmount1 = storage.getCereal(Cereal.BULGUR, amount)
        val remainingAmount1 = storage.getAmount(Cereal.BULGUR)
        assertEquals(takenAmount, takenAmount1) {"Фактически полученное количество крупы отличается"}
        assertEquals(remainingAmount,remainingAmount1) {"Остаток в контейнере после применения метода "}
    }

    @Test
    fun `should Remove Container Only When It Is Empty`(){
        storage.addCereal(Cereal.BULGUR,2f)
        assertFalse { storage.removeContainer(Cereal.BULGUR)}
        assertEquals(1,storage.getStorageSize()) {"Значение storage.size должно быть 1 "}
        storage.getCereal(Cereal.BULGUR,2f)
        assertTrue { storage.removeContainer(Cereal.BULGUR)}
        assertEquals(0,storage.getStorageSize()){"Значение storage.size должно быть 0 "}
    }

    @Test
    fun `should Return Amount Or Zero When Container Does Not Exist`(){
        storage.addCereal(Cereal.BULGUR ,3.5f )
        assertEquals(3.5f, storage.getAmount(Cereal.BULGUR))
        assertEquals(0.0f, storage.getAmount(Cereal.RICE))
    }

    @Test
    fun `should return correct space when container exists`(){
        val c = CerealStorageImpl(5f,20f).apply {
            addCereal(Cereal.RICE,0f)
            addCereal(Cereal.BULGUR, 5f)
            addCereal(Cereal.BUCKWHEAT,5f)
            getCereal(Cereal.BUCKWHEAT,2f)
            addCereal(Cereal.PEAS,150f)
        }
        assertEquals(5f, c.getSpace(Cereal.RICE))
        assertEquals(0f, c.getSpace(Cereal.BULGUR))
        assertEquals(2f,c.getSpace(Cereal.BUCKWHEAT))
        assertEquals(0f, c.getSpace((Cereal.PEAS)))

    }


    @Test
    fun `should Throw IllegalStateException When Container Does Not Exist`(){
        assertThrows(IllegalStateException::class.java){
            storage.getSpace(Cereal.BULGUR)
        }
        storage.addCereal(Cereal.RICE,5f)
        assertThrows(IllegalStateException::class.java){
            storage.getSpace(Cereal.BULGUR)
        }

    }


    companion object {
        @JvmStatic
        fun provideValidValuesForAddCerealWhenCreatingNewContainer() =
            listOf(
                arrayOf(Cereal.PEAS, 0, 0f),
                arrayOf(Cereal.RICE, 3.2f, 0f),
                arrayOf(Cereal.RICE, 10f, 0f),
                arrayOf(Cereal.BUCKWHEAT, 12f, 2f),
                arrayOf(Cereal.BUCKWHEAT, 22f, 12f)
            )

        @JvmStatic
        fun provideExistingContainerWithCereal() =
            listOf(
                arrayOf(Cereal.RICE, 0.0f),
                arrayOf(Cereal.RICE, 8f),
                arrayOf(Cereal.RICE, 15f),
                arrayOf(Cereal.RICE, 222f)
            )

        @JvmStatic
        fun provideValuesForShouldReturnCorrectAmountForVariousValues() = listOf(
            arrayOf(3f,3f, 2f),
            arrayOf(5f,5f, 0.0f),
            arrayOf(6f, 5f, 0.0f)
        )
    }


}