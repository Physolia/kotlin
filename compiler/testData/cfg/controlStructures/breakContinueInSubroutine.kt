inline fun foo(x: () -> Unit): Unit = TODO()

fun test(): String {
    var result = "OK"
    loop@ while (true) {
        foo { break@loop }
        foo { continue@loop }
        result = "..."
    }
    return result
}
