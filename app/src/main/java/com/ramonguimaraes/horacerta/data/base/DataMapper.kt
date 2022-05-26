package com.ramonguimaraes.horacerta.data.base

interface DataMapper<E, D> {
    fun fromEntity(entity: E): D
    fun toEntity(data: D): E
    fun fromEntity(entity: List<E>): List<D> {
        return entity.map { fromEntity(it) }
    }
    fun toEntity(data: List<D>): List<E> {
        return data.map { toEntity(it) }
    }
}
