package integration

import io.cucumber.junit.platform.engine.Constants
import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Tag("integration")
@Suite
@IncludeEngines("cucumber")
@SelectPackages("cucumberFeatures")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
class RunCucumberTest