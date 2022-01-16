package com.wh.common.store

abstract class AStoreFactory {
    abstract fun <T : IStore> create(modeClass: Class<T>): T
}