import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    launch {
        logMessage("Pre>>>")
        val time = measureTimeMillis {
            logMessage(logHelloWorld())
        }
        logMessage("Time taken: $time")
    }

    logMessage("<<<Post")
    logMessage("completing main function...")

}


private suspend fun logHelloWorld() = coroutineScope {

    withContext(Dispatchers.Default) {
        logMessage("in logHelloWorld")
        val helloMessage = async { helloMessage() }
        val worldMessage = async { worldMessage() }

        logMessage("leaving logHelloWorld")
        helloMessage.await() + worldMessage.await()
    }
}

private suspend fun helloMessage(): String {
    delay(1000)
    return "Hello "
}


private suspend fun worldMessage(): String {
    delay(1000)
    return "World!!"
}


fun logMessage(msg: String) {
    println("Running on: [${Thread.currentThread().name}] | $msg")
}


fun CoroutineScope.logContext(id: String) {
    coroutineContext.logDetails(id)
}


fun CoroutineContext.logDetails(id: String) {
    sequenceOf(
        Job,
        ContinuationInterceptor,
        CoroutineExceptionHandler,
        CoroutineName
    )
        .mapNotNull { key -> this[key] }
        .forEach { logMessage("id: $id ${it.key} = ${it}") }
}