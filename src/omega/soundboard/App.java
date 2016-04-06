package omega.soundboard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class App extends Application {

	private UIController controller;
	private Logic logic;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		logic = new Logic();
		logic.startShortcutHandler();

		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
			final Parent root = (Parent) loader.load();
			controller = loader.getController();
			controller.setLogic(logic);

			loader.setController(controller);
			primaryStage.setScene(new Scene(root, 400, 400));
			primaryStage.show();
			controller.updateButtons();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		logic.stopShortcutHandler();
		logic.saveAll(controller.getButtonSounds());
	}

	public class Logic {
		private ShortcutHandler shortcutHandler;

		private Path dataFolderPath;
		private Path commonPropertiesPath;
		private Path soundPropertiesPath;

		private int buttonCount = 1;
		private int columnCount = 1;

		public Logic() {
			try {
				dataFolderPath = Paths.get(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI())
						.getParent().resolve("data");

				if (!Files.isDirectory(dataFolderPath)) {
					Files.createDirectories(dataFolderPath);
				}

				commonPropertiesPath = dataFolderPath.resolve("common.properties");
				soundPropertiesPath = dataFolderPath.resolve("sounds.ser");

				loadCommonProperties();
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		}

		public void saveAll(List<Sound> sounds) {
			try {
				safeCommonProperties();
				safeSounds(sounds);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public int getButtonCount() {
			return buttonCount;
		}

		public void setButtonCount(int buttonCount) {
			this.buttonCount = buttonCount;
		}

		public ShortcutHandler getShortcutHandler() {
			return shortcutHandler;
		}

		public int getColumnCount() {
			return columnCount;
		}

		public void setColumnCount(int columnCount) {
			this.columnCount = columnCount;
		}

		public void startShortcutHandler() {
			if (shortcutHandler != null) {
				shortcutHandler.stop();
			}
			shortcutHandler = new ShortcutHandler();
		}

		public void stopShortcutHandler() {
			shortcutHandler.stop();
		}

		public Path copyToData(Path fileToCopy) {
			try {
				return Files.copy(fileToCopy, dataFolderPath.resolve(fileToCopy.getFileName()),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		private void loadCommonProperties() throws IOException {
			if (Files.isReadable(commonPropertiesPath)) {
				Properties properties = new Properties();
				properties.loadFromXML(Files.newInputStream(commonPropertiesPath));

				buttonCount = Integer.valueOf(properties.getProperty("buttonCount", "1"));
				columnCount = Integer.valueOf(properties.getProperty("columnCount", "1"));
			}
		}

		@SuppressWarnings("unchecked")
		public List<Sound> loadSounds() throws IOException {
			List<Sound> result = new ArrayList<Sound>();
			if (Files.isReadable(soundPropertiesPath)) {
				ObjectInputStream in = new ObjectInputStream(Files.newInputStream(soundPropertiesPath));
				try {
					result = (List<Sound>) in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				in.close();
			}
			return result;
		}

		private void safeCommonProperties() throws IOException {
			OutputStream os = Files.newOutputStream(commonPropertiesPath);
			Properties defaults = new Properties();
			defaults.setProperty("buttonCount", Integer.toString(buttonCount));
			defaults.setProperty("columnCount", Integer.toString(columnCount));
			defaults.storeToXML(os, null);
			os.close();
		}

		private void safeSounds(List<Sound> sounds) throws IOException {
			ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(soundPropertiesPath));
			os.writeObject(sounds);
			os.flush();
			os.close();
		}
	}
}
