package com.example.customprinterlist.exceptions

sealed class PrintException : Exception()

data class DiscoveryFailedException(override val message: String) : PrintException()
data class PrintFailedException(override val message: String) : PrintException()