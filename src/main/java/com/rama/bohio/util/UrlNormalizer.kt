package com.rama.bohio.util

object UrlNormalizer {
    fun normalize(value: String): String {
        val destination = value.trim()

        return when {
            destination.startsWith("http://", ignoreCase = true) ||
                    destination.startsWith("https://", ignoreCase = true) -> destination

            else -> "https://$destination"
        }
    }
}