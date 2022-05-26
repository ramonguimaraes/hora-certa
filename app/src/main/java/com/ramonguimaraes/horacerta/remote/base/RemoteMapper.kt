package com.ramonguimaraes.horacerta.remote.base

interface RemoteMapper<R, E> {
    fun fromRemote(remote: R): E
    fun toRemote(entity: E): R
    fun fromRemote(remote: List<R>?): List<E> {
        return remote?.map { fromRemote(it) } ?: arrayListOf()
    }
}
