package prod.prog.request.resultHandler

import prod.prog.request.transformer.Transformer

typealias ResultHandler<T> = Transformer<T, Unit>
