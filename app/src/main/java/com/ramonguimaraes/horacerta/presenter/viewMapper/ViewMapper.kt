package com.ramonguimaraes.horacerta.presenter.viewMapper

abstract class ViewMapper<V, D> {
    abstract fun mapToView(model: D): V
    abstract fun mapFromView(view: V): D

    open fun mapToView(view: List<D>): List<V> {
        return view.map {
            mapToView(it)
        }
    }
}
