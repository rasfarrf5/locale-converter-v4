package converter.plugin.support;

import converter.plugin.process.ConvertAndroidLocale;
import converter.plugin.process.ConvertIosLocale;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class LocaleTask extends SwingWorker<Void, Void> {

	private final boolean isAndroid;
	private final File selectedFile;
	private final List<Integer> languageToConvert;
	private final LocaleTaskListener listener;

	private boolean taskStatus = false;

	public LocaleTask(boolean isAndroid,
						 File selectedFile,
						 List<Integer> languageToConvert,
						 LocaleTaskListener listener) {
		this.isAndroid = isAndroid;
		this.selectedFile = selectedFile;
		this.languageToConvert = languageToConvert;
		this.listener = listener;
	}

	@Override
	protected Void doInBackground() throws Exception {
		if (isAndroid) {
			taskStatus = new ConvertAndroidLocale()
					.loadSheet(
							languageToConvert,
							selectedFile.getAbsolutePath(),
							selectedFile.getPath().replace("/" + selectedFile.getName(), "/android"));
		} else {
			taskStatus = new ConvertIosLocale()
					.loadSheet(
							languageToConvert,
							selectedFile.getAbsolutePath(),
							selectedFile.getPath().replace("/" + selectedFile.getName(), "/ios"));
		}
		return null;
	}

	@Override
	protected void done() {
		super.done();
		if (taskStatus) {
			listener.onTaskSuccess();
		} else {
			listener.onTaskFailed();
		}
	}
}
