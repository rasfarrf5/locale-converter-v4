package converter.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

public class LocaleActions extends AnAction {
	public LocaleActions() {
		super("Locale Converter");
	}

	public void actionPerformed(AnActionEvent event) {
		Project project = event.getData(PlatformDataKeys.PROJECT);

		if (project != null) {
			new LocaleDialogWrapper().showAndGet();
		}
	}
}
