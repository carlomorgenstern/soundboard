package omega.soundboard;

import java.io.Serializable;
import java.net.URI;

public class Sound implements Serializable {
	private static final long serialVersionUID = -8144522334751265368L;

	private String title;
	private Shortcut shortcut;
	private URI soundFile;

	public Sound(String title, Shortcut shortcut, URI soundFile) {
		this.title = title;
		this.shortcut = shortcut;
		this.soundFile = soundFile;
	}

	public Sound() {
		this.title = "Unbelegt";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Shortcut getShortcut() {
		return shortcut;
	}

	public void setShortcut(Shortcut shortcut) {
		this.shortcut = shortcut;
	}

	public URI getSoundFile() {
		return soundFile;
	}

	public void setSoundFile(URI soundFile) {
		this.soundFile = soundFile;
	}
}