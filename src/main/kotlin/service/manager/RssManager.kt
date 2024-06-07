package prod.prog.service.manager

import prod.prog.actionProperties.contextFactory.print.PrintError
import prod.prog.actionProperties.contextFactory.print.PrintInfo
import prod.prog.dataTypes.Company
import prod.prog.dataTypes.rss.AvailableRssSources
import prod.prog.dataTypes.rss.NewsPiecesByCompanyRssSource
import prod.prog.request.Request
import prod.prog.request.RequestContext
import prod.prog.request.resultHandler.PrintErrorHandler
import prod.prog.request.resultHandler.database.AddNewsSummaryDBHandler
import prod.prog.request.resultHandler.ignoreOutput
import prod.prog.request.source.ConstantSource
import prod.prog.request.source.database.CompaniesSourceDB
import prod.prog.request.source.ignoreInput
import prod.prog.request.transformer.IdTransformer
import prod.prog.request.transformer.LanguageModelTransformer
import prod.prog.request.transformer.Transformer.Companion.forEach
import prod.prog.service.database.DatabaseService
import prod.prog.service.languageModel.LanguageModelService
import prod.prog.service.logger.LoggerService
import prod.prog.service.rss.RssService
import prod.prog.service.supervisor.Supervisor

fun rssManager(
    supervisor: Supervisor,
    logger: LoggerService,
    database: DatabaseService,
    rssService: RssService,
    languageModel: LanguageModelService,
    periodInMillis: Long,
) = TimerManager(
    supervisor, logger, "RssManager", periodInMillis, Request(
        CompaniesSourceDB(database)
            .apply { addContext(PrintInfo { "rssRequestSource" }) },
        IdTransformer<Company>()
            .forEach()
            .withPair(ConstantSource(AvailableRssSources.entries.map { s -> s.rssNewsLink }).ignoreInput())
            .andThen(NewsPiecesByCompanyRssSource(rssService))
            .andThen(LanguageModelTransformer(languageModel).forEach())
            .apply { addContext(PrintInfo { "rssRequestTransform" }) },
        AddNewsSummaryDBHandler(database).forEach().ignoreOutput()
            .apply { addContext(PrintInfo { "rssRequestHandler" }) },
        PrintErrorHandler(logger)
            .apply {
                addContext(
                    PrintInfo { "rssRequestErrorHandler" },
                    PrintError { "rssRequestErrorHandler" })
            },
        RequestContext()
    )
)