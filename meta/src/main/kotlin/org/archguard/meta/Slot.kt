package org.archguard.meta

// just for fun
typealias Coin = List<String>

/**
 * Slot Type is design for plug slot in source code analysis
 * Slot is a data consumer after analyser analysis data.
 * can be use for:
 * - linter
 * - metrics
 * - custom api analysis
 **/
interface Slot {
    /**
     * The required data type for search in Element, for example: WebAPI require ContainResource
     */
    abstract fun ticket(): Coin

    /**
     * pass data then return response
     */
    abstract fun process(items: List<Any>): List<Any>
}
