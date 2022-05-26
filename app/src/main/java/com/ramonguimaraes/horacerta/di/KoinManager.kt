package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.di.clientProfile.clientProfileModule
import org.koin.core.module.Module

object KoinManager {

    /**
     * Método responsavel por retornar todos os modulos de dependencias do projeto.
     *
     * @return List<Module>
     */
    fun getModules() = listOf<Module>(
        clientProfileModule()
    )
}
