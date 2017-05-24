package fr.cnes.sonar.plugins.analysis.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;

/**
 * Define web pages of the plugin
 * @author garconb
 */
public class AnalysisPluginPageDefinition implements PageDefinition {

  @Override
  public void define(Context context) {
    context
      .addPage(Page.builder("cnes/analysis")
        .setName("Analysis")
        .setScope(Scope.GLOBAL).build());
  }
}
