package net.mornati.epomodoro.util;

import java.util.List;

import net.mornati.epomodoro.Activator;
import net.mornati.epomodoro.communication.TextMessage;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class UIUtil {

	public static Button createStartButton(Composite parent) {
		final Button startButton=new Button(parent, SWT.FLAT | SWT.TOGGLE);
		startButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (Activator.getDefault().getTimer() != null && Activator.getDefault().getTimer().getStatus().equals(PomodoroTimer.STATUS_INITIALIZED)) {
					Activator.getDefault().getTimer().start();
				} else {
					Activator.getDefault().getTimer().pause();
				}
				List<Button> startButtons=Activator.getDefault().getStartButtons();
				for (final Button startButton : startButtons) {
					if (!startButton.isDisposed()) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								addDetailsToButton(startButton);
							}
						});
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		addDetailsToButton(startButton);
		return startButton;
	}

	public static Button createResetButton(Composite parent) {
		final Button resetButton=new Button(parent, SWT.NULL);
		resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Activator.getDefault().resetTimer(Activator.getDefault().getTimer().getConfigWorkTime(), PomodoroTimer.TYPE_WORK);
				List<Button> startButtons=Activator.getDefault().getStartButtons();
				for (final Button startButton : startButtons) {
					if (!startButton.isDisposed()) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								addDetailsToButton(startButton);
							}
						});
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		resetButton.setImage(Activator.getImageDescriptor(PluginImages.ICONS_RESET).createImage());
		resetButton.setToolTipText("Reset");
		return resetButton;

	}

	public static void showReceivedMessages() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (Activator.getDefault().getCommunication().getReceivedMessages().size() > 0) {
					String messages="";
					for (TextMessage textMessage : Activator.getDefault().getCommunication().getReceivedMessages()) {
						messages+=textMessage.getSender() + " - " + textMessage.getMessage() + "\n";
					}
					MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Messages Received", messages);
					Activator.getDefault().getCommunication().resetReceivedMessages();
				}
			}
		});
	}

	private static void addDetailsToButton(Button startButton) {
		String imagePath=Activator.getDefault().getTimer() != null && Activator.getDefault().getTimer().getStatus().equals(PomodoroTimer.STATUS_WORKING_TIME) ? PluginImages.ICONS_PAUSE
				: PluginImages.ICONS_PLAY;
		startButton.setImage(Activator.getImageDescriptor(imagePath).createImage());
		String tooltipText=Activator.getDefault().getTimer() != null && Activator.getDefault().getTimer().getStatus().equals(PomodoroTimer.STATUS_WORKING_TIME) ? "Pause"
				: "Play";
		startButton.setToolTipText(tooltipText);
	}

}
