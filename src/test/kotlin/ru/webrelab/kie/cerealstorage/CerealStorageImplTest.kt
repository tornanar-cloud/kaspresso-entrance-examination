package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CerealStorageImplTest {
    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should ThrowException When StorageCapacity Is Less Than ContainerCapacity`(){
        assertThrows(IllegalArgumentException::class.java){
            CerealStorageImpl(4f, 3f)
        }
    }

    @ParameterizedTest
    @MethodSource ("provideValidValuesForAddCerealWhenCreatingNewContainer")
    fun `shouldAddCerealWhenStorageIsEmptyAndContainerHasEnoughSpace добавление нового контейнера и крупы, когда в хранилище достаточно места для нового контейреа, значение крупы среднее валидное`(cer: Cereal , am: Float, exOv: Float){
        val actualOverflow = storage.addCereal(cer, am )
        //Проверяю остаток
        assertEquals(exOv, actualOverflow, 0.00038f, "Остаток не совпадает с ожидаемым. Ожидаем $exOv , фактический $actualOverflow ")
        println(exOv)
        //Проверяю, что контейнер добавлен и количество крупы в контейнере. Максимум добавлено может быть containerCpacity, остальное возвращается return
        assertEquals(if(am>=storage.containerCapacity) storage.containerCapacity else am, storage.getAmount(cer))
        println(storage.getAmount(cer))

    }


    @Test
    fun `should Return Amount When Cereal Exists`() {

    }

    @Test
    fun getCereal() {
    }

    @Test
    fun removeContainer() {
    }

    @Test
    fun getAmount() {
    }

    @Test
    fun getSpace() {
    }



companion object{
    @JvmStatic
    fun provideValidValuesForAddCerealWhenCreatingNewContainer() =
        listOf(
            arrayOf(Cereal.RICE, 3.2f,0f),
            arrayOf(Cereal.RICE, 10f,0f),
            arrayOf(Cereal.BUCKWHEAT,12f,2f),
            arrayOf(Cereal.BUCKWHEAT,22f,12f)
        )

}



}