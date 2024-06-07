package prod.prog.service.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import prod.prog.request.Request
import prod.prog.service.logger.LoggerService
import prod.prog.service.supervisor.Supervisor
import java.util.concurrent.atomic.AtomicBoolean


class TimerManager(
    supervisor: Supervisor,
    private val logger: LoggerService,
    private val name: String,
    private val periodInMillis: Long,
    private val request: Request<*, *>,
) : RequestManager(supervisor) {
    private val isWorking = AtomicBoolean(false)

    private tailrec suspend fun loop() {
        if (isWorking.get()) {
            makeRequest(request)
            delay(periodInMillis)
            loop()
        }
    }

    override fun start() {
        logger.log(PrintInfo, "${name()} started")
        if (isWorking.compareAndSet(false, true)) {
            CoroutineScope(Dispatchers.IO).launch {
                loop()
            }
        }
    }

    override fun stop() {
        logger.log(PrintInfo, "${name()} stopped")
        isWorking.set(false)
    }

    override fun name() = "${periodInMillis}ms timer $name"
}