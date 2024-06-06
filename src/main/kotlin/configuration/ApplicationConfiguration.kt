package prod.prog.configuration

import io.github.cdimascio.dotenv.dotenv
import io.sentry.Sentry
import io.sentry.SentryOptions

class ApplicationConfiguration {
    fun initApplication() {
        initSentry()
    }

    // We should set env variable on start
    private fun initSentry() {
        Sentry.init { options: SentryOptions ->
            options.dsn = dotenv()["SENTRY_DSN"]
            options.tracesSampleRate = 1.0
        }
    }
}