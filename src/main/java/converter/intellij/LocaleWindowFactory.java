package converter.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import converter.plugin.LocalizationConverterV2;
import org.jetbrains.annotations.NotNull;

public class LocaleWindowFactory implements ToolWindowFactory {

	public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow) {
		LocalizationConverterV2 converterV2 = new LocalizationConverterV2();
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		Content content = contentFactory.createContent(converterV2.getContent(), "", false);
		toolWindow.getContentManager().addContent(content);
	}
}
