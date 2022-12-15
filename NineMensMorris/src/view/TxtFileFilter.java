package view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TxtFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) return true;
		String fname = file.getName().toLowerCase();
		return fname.endsWith(".txt");
	}

	public String getDescription() {
		return "Normal text file (*.txt)";
	}
}
