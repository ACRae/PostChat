package isel.acrae.com.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KFunction


/**
 * Create a logger for the given class.
 */
inline fun <reified T> logger(): Logger =
    LoggerFactory.getLogger(T::class.java.simpleName)

/**
 * Run a block of code with a logger.
 * @param [identifier] Function identifier
 */
fun <T> Logger.runLogging(identifier: KFunction<*>, block: () -> T) : T {
    this.info("Entering ==> ${identifier.name} ${identifier.returnType}")
    return try {
        block().also {
            this.info(
                "Returning: $it <== ${identifier.name} ${identifier.returnType}  "
            )
        }
    }
    catch (e : Exception) {
        this.error("Error in ${identifier.name} ${identifier.returnType}")
        throw e
    }
}