package prod.prog.request.resultHandler

import prod.prog.request.transformer.Transformer

abstract class ResultHandler<T> : Transformer<T, Unit>()
