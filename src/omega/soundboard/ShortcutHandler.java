package omega.soundboard;

import java.util.ArrayList;
import java.util.HashMap;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class ShortcutHandler implements HotkeyListener {

	private JIntellitype intelli = JIntellitype.getInstance();
	private HashMap<Integer, ShortcutHolder> keyboardShortcuts = new HashMap<Integer, ShortcutHolder>();
	private static int keyboardShortcutID = 0;

	private ControllerRecordThread controllerThread = new ControllerRecordThread();

	public ShortcutHandler() {
		intelli.addHotKeyListener(this);
		controllerThread.start();
	}

	public ShortcutHandler(Shortcut shortcut, ShortcutListener listener) {
		this();
		registerShortcutListener(shortcut, listener);
	}

	public boolean registerShortcutListener(Shortcut shortcut, ShortcutListener listener) {
		if (shortcut.isKeyboardShortcut()) {
			int addIndex = ++keyboardShortcutID;
			int[] intelliShortcut = KeyCodeMapper.toIntelliShortcut(shortcut.getKeyboardShortcut());
			keyboardShortcuts.put(addIndex, new ShortcutHolder(shortcut, listener));
			intelli.registerHotKey(addIndex, intelliShortcut[0], intelliShortcut[1]);
			return true;
		} else {
			return controllerThread.addControllerShortcut(new ShortcutHolder(shortcut, listener));
		}
	}

	public boolean removeShortcutListener(Shortcut shortcut) {
		if (shortcut.isKeyboardShortcut()) {
			for (HashMap.Entry<Integer, ShortcutHolder> entry : keyboardShortcuts.entrySet()) {
				if (entry.getValue().shortcut.equals(shortcut)) {
					intelli.unregisterHotKey(entry.getKey());
					keyboardShortcuts.remove(entry.getKey());
					return true;
				}
			}
			return false;
		} else {
			return controllerThread.removeControllerShortcut(shortcut);
		}
	}

	public void clearShortcutListeners() {
		for (Integer key : keyboardShortcuts.keySet()) {
			intelli.unregisterHotKey(key);
		}
		keyboardShortcuts.clear();

		controllerThread.clearControllerShortcuts();
	}

	public void stop() {
		intelli.cleanUp();
		controllerThread.interrupt();
	}

	@Override
	public void onHotKey(int key) {
		keyboardShortcuts.get(key).shortcutListener.onShortcutPressed();
	}

	public void requestControllerShortcut(ShortcutRequestListener listener) {
		controllerThread.requestControllerShortcut(listener);
	}

	public void interruptControllerShortcutRequest() {
		controllerThread.interuptControllerShortcutRequest();
	}

	private class ControllerRecordThread extends Thread {
		private static final int minLoopMillis = 30;

		private boolean shortcutRequested = false;
		private ShortcutRequestListener shortcutRequestListener;
		private ArrayList<ShortcutHolder> controllerShortcuts = new ArrayList<ShortcutHolder>();

		private ArrayList<Controller> controllers = new ArrayList<Controller>();

		public ControllerRecordThread() {
			searchForControllers();
		}

		@Override
		public void run() {
			long loopTime = 0;
			while (!isInterrupted()) {
				loopTime = System.currentTimeMillis();

				for (int i = 0; i < controllers.size(); i++) {
					Controller controller = controllers.get(i);
					controller.poll();

					EventQueue queue = controller.getEventQueue();
					Event event = new Event();
					while (queue.getNextEvent(event)) {
						Component comp = event.getComponent();
						String compName = comp.getName();
						if (comp.isAnalog() && Math.abs(event.getValue()) >= 0.5) {
							if (event.getValue() < 0) {
								compName += " -";
							} else {
								compName += " +";
							}

							if (shortcutRequested) {
								shortcutRequestListener.controllerShortcutDetermined(compName);
								shortcutRequested = false;
								shortcutRequestListener = null;
							} else {
								for (int j = 0; j < controllerShortcuts.size(); j++) {
									if (controllerShortcuts.get(j).shortcut.getControllerShortcutName()
											.equals(compName)) {
										controllerShortcuts.get(j).shortcutListener.onShortcutPressed();
									}
								}
							}
						} else if (!comp.isAnalog() && event.getValue() == 1.0f) {

							if (shortcutRequested) {
								shortcutRequestListener.controllerShortcutDetermined(comp.getName());
								shortcutRequested = false;
								shortcutRequestListener = null;
							} else {
								for (int j = 0; j < controllerShortcuts.size(); j++) {
									if (controllerShortcuts.get(j).shortcut.getControllerShortcutName()
											.equals(compName)) {
										controllerShortcuts.get(j).shortcutListener.onShortcutPressed();
									}
								}
							}
						} else if (comp.getIdentifier() == Component.Identifier.Axis.POV && event.getValue() != 0.0f) {
							compName += " " + event.getValue();

							if (shortcutRequested) {
								shortcutRequestListener.controllerShortcutDetermined(compName);
								shortcutRequested = false;
								shortcutRequestListener = null;
							} else {
								for (int j = 0; j < controllerShortcuts.size(); j++) {
									if (controllerShortcuts.get(j).shortcut.getControllerShortcutName()
											.equals(compName)) {
										controllerShortcuts.get(j).shortcutListener.onShortcutPressed();
									}
								}
							}
						}
					}
				}

				if ((loopTime = System.currentTimeMillis() - loopTime) < minLoopMillis) {
					try {
						sleep(minLoopMillis - loopTime);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}

		private void searchForControllers() {
			Controller[] foundControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

			for (int i = 0; i < foundControllers.length; i++) {
				if (foundControllers[i].getType() == Controller.Type.STICK
						|| foundControllers[i].getType() == Controller.Type.GAMEPAD
						|| foundControllers[i].getType() == Controller.Type.WHEEL
						|| foundControllers[i].getType() == Controller.Type.FINGERSTICK) {
					controllers.add(foundControllers[i]);
				}
			}
		}

		public synchronized boolean addControllerShortcut(ShortcutHolder shortcutHolder) {
			return controllerShortcuts.add(shortcutHolder);
		}

		public synchronized boolean removeControllerShortcut(Shortcut shortcut) {
			for (int i = 0; i < controllerShortcuts.size(); i++) {
				if (controllerShortcuts.get(i).shortcut.equals(shortcut)) {
					controllerShortcuts.remove(i);
					return true;
				}
			}
			return false;
		}

		public synchronized void clearControllerShortcuts() {
			controllerShortcuts.clear();
		}

		public synchronized void requestControllerShortcut(ShortcutRequestListener listener) {
			shortcutRequested = true;
			shortcutRequestListener = listener;
		}

		public synchronized void interuptControllerShortcutRequest() {
			shortcutRequested = false;
			shortcutRequestListener = null;
		}
	}

	private class ShortcutHolder {
		Shortcut shortcut;
		ShortcutListener shortcutListener;

		ShortcutHolder(Shortcut shortcut, ShortcutListener shortcutListener) {
			this.shortcut = shortcut;
			this.shortcutListener = shortcutListener;
		}
	}

	public interface ShortcutListener {
		public void onShortcutPressed();
	}

	public interface ShortcutRequestListener {
		public void controllerShortcutDetermined(String componentName);
	}
}
