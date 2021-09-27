// IGNORE_BACKEND: JVM, JS
// IGNORE_BACKEND_FIR: JVM_IR
// IGNORE_BACKEND_MULTI_MODULE: JVM, JVM_MULTI_MODULE_OLD_AGAINST_IR
// FILE: 1.kt
package test

public inline fun <R> doCall(block: () -> R): R {
    return block()
}

// FILE: 2.kt
import test.*

fun box(): String {
    var result = ""
    loop@ while (true) {
        when (result.length) {
            1 -> doCall { result += "K"; continue@loop }
            2 -> doCall { break@loop }
        }
        result += "O"
    }
    return result
}
