package converter.intellij;

import com.intellij.openapi.ui.DialogWrapper;
import converter.plugin.LocalizationConverterV2;

import javax.annotation.Nullable;
import javax.swing.*;

public class LocaleDialogWrapper extends DialogWrapper {

	private LocalizationConverterV2 converterV2 = new LocalizationConverterV2();

	public LocaleDialogWrapper() {
		super(true);
		init();
		setTitle("Locale Converter");
		setOKButtonText("Start");

		getOKAction().addPropertyChangeListener(listener -> converterV2.startConverter());
	}

	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		return converterV2.getContent();
	}
}
