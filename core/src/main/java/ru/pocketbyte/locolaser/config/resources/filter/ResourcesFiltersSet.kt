package ru.pocketbyte.locolaser.config.resources.filter

/**
 * A [ResourcesFilter] that combines multiple filters, including a resource only if all filters accept its key (logical AND).
 */
data class ResourcesFiltersSet(
    /**
     * The array of [ResourcesFilter] instances that are all applied.
     */
    val filters: Array<out ResourcesFilter>
): ResourcesFilter {

    override fun filter(key: String): Boolean {
        filters.forEach {
            if (it.filter(key).not()) {
                return false
            }
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResourcesFiltersSet

        return filters.contentEquals(other.filters)
    }

    override fun hashCode(): Int {
        return filters.contentHashCode()
    }
}