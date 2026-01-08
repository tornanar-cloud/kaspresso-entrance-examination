package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
    @Test
    fun addCereal() {
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







}