package org.betacraft.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Lang extends JFrame {
	public static List<String> locales = new ArrayList<String>();

	static JList list;
	static DefaultListModel listModel;
	static JScrollPane listScroller;
	JButton OK;

	public Lang() {
		Logger.a("Otwarto okno wyboru jezyka.");
		setSize(282, 386);
		setLayout(null);
		setTitle("Select language");
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		try {
			initLang();
		} catch (IOException e1) {
			e1.printStackTrace();
			Logger.a(e1);
		}

		int i = 0;
		int index = 0;
		listModel = new DefaultListModel();
		String lang = Launcher.getProperty(Launcher.SETTINGS, "language");
		for (String item : locales) {
			listModel.addElement(item);
			if (lang.equals(item)) {
				index = i;
			}
			i++;
		}

		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(10, 30, 262, 290);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(3);
		list.setSelectedIndex(index);

		if (listScroller != null) this.remove(listScroller);

		listScroller = new JScrollPane(list);
		listScroller.setBounds(10, 30, 262, 280);
		listScroller.setWheelScrollingEnabled(true);
		getContentPane().add(listScroller);

		OK = new JButton("OK");
		OK.setBounds(10, 320, 60, 20);
		OK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String lang = (String) list.getSelectedValue();
				if (!downloaded(lang)) {
					download(lang);
				}
				Launcher.setProperty(Launcher.SETTINGS, "language", lang);
				setVisible(false);
				apply();
			}
		});
		add(OK);

		OK.setBackground(Color.LIGHT_GRAY);
	}

	public static void initLang() throws IOException {
		URL url = new URL("https://betacraft.ovh/lang/index.html");

		Scanner scanner = new Scanner(url.openStream());
		String now;
		while (scanner.hasNextLine()) {
			now = scanner.nextLine();
			if (now.equalsIgnoreCase("")) continue;
			locales.add(now);
		}
		scanner.close();
	}

	public static boolean downloaded(String lang) {
		File file = new File(BC.get() + "launcher/lang", lang);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static void download(String lang) {
		Launcher.download("https://betacraft.ovh/lang/" + lang + ".txt", new File(BC.get() + "launcher/lang/", lang + ".txt"));
	}

	public static String get(String property) {
		return Launcher.getProperty(new File(BC.get() + "launcher/lang/", Launcher.getProperty(Launcher.SETTINGS, "language") + ".txt"), property);
	}

	public static void apply() {
		String lang = Launcher.getProperty(Launcher.SETTINGS, "language");
		if (lang.equals("")) return;
		File file = new File(BC.get() + "launcher/lang/", lang + ".txt");

		Window.about.setText(Launcher.getProperty(file, "version_button"));
		Window.play.setText(Launcher.getProperty(file, "play_button"));
		Window.options.setText(Launcher.getProperty(file, "options_button"));
		Window.window.setTitle(Launcher.getProperty(file, "launcher_title") + Launcher.VERSION);
		Window.kazu.setText(Launcher.getProperty(file, "credits"));
		Window.nicktext.setText(Launcher.getProperty(file, "nick") + ":");

		Launcher.update = Launcher.getProperty(file, "new_version_found");

		Window.play_lang = Launcher.getProperty(file, "play_button");
		Window.max_chars = Launcher.getProperty(file, "max_chars");
		Window.min_chars = Launcher.getProperty(file, "min_chars");
		Window.banned_chars = Launcher.getProperty(file, "banned_chars");
		Window.warning = Launcher.getProperty(file, "warning");
		Window.no_connection = Launcher.getProperty(file, "no_connection");
		Window.download_fail = Launcher.getProperty(file, "download_fail");
		Window.downloading = Launcher.getProperty(file, "downloading");

		Opcje opcje = Window.currentOptions;
		Opcje.update = Launcher.getProperty(file, "update_check");
		Opcje.update_not_found = Launcher.getProperty(file, "update_not_found");
		if (opcje != null) {
			Opcje.retrocraft.setText(Launcher.getProperty(file, "use_retrocraft"));
			Opcje.label.setText(Launcher.getProperty(file, "launch_arguments") + ":");
			Opcje.open.setText(Launcher.getProperty(file, "keep_launcher_open"));
			Opcje.checkUpdate.setText(Launcher.getProperty(file, "check_update_button"));
			opcje.setTitle(Launcher.getProperty(file, "options_title"));
		}

		Wersja wersja = Window.currentAbout;
		if (wersja != null) {
			wersja.setTitle(Launcher.getProperty(file, "version_title"));
		}
	}
}