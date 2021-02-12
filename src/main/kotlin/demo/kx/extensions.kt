package demo.kx

import java.util.*


// Read https://kotlinlang.org/docs/extensions.html
// Read https://kotlinlang.org/docs/lambdas.html#passing-trailing-lambdas
fun main() {

    applyAction("hello", "bye") { s: String ->
        println(s.uuid())
    }

}

fun applyAction(vararg s: String, action: (String) -> Unit) {
    s.forEach(action)
}

fun String.uuid(): String =
    UUID.nameUUIDFromBytes(this.encodeToByteArray()).toString()
