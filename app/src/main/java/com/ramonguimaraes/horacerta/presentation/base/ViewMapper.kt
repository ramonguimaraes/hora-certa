package com.ramonguimaraes.horacerta.presentation.base

interface ViewMapper<V, D> {
    fun toView(domain: D): V
    fun fromView(view: V): D
    fun toView(domain: List<D>?): List<V> {
        return domain?.map { toView(it) } ?: arrayListOf()
    }
    fun fromView(view: List<V>?): List<D> {
        return view?.map { fromView(it) } ?: arrayListOf()
    }
}
