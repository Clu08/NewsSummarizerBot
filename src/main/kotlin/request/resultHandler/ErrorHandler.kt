package prod.prog.request.resultHandler

import prod.prog.actionProperties.print.PrintError

interface ErrorHandler : ResultHandler<Throwable>, PrintError