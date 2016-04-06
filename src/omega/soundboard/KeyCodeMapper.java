package omega.soundboard;

import java.util.HashMap;

import com.melloware.jintellitype.JIntellitype;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class KeyCodeMapper {

	private static HashMap<KeyCode, Integer> keyCodeIntelliMap = new HashMap<KeyCode, Integer>();

	static {
		keyCodeIntelliMap.put(KeyCode.BACK_SPACE, 0x08);
		keyCodeIntelliMap.put(KeyCode.TAB, 0x09);
		keyCodeIntelliMap.put(KeyCode.CLEAR, 0x0C);
		keyCodeIntelliMap.put(KeyCode.ENTER, 0x0D);
		keyCodeIntelliMap.put(KeyCode.SHIFT, 0x10);
		keyCodeIntelliMap.put(KeyCode.CONTROL, 0x11);
		keyCodeIntelliMap.put(KeyCode.ALT, 0x12);
		keyCodeIntelliMap.put(KeyCode.PAUSE, 0x13);
		keyCodeIntelliMap.put(KeyCode.CAPS, 0x14);
		keyCodeIntelliMap.put(KeyCode.ESCAPE, 0x1B);
		keyCodeIntelliMap.put(KeyCode.CONVERT, 0x1C);
		keyCodeIntelliMap.put(KeyCode.NONCONVERT, 0x1D);
		keyCodeIntelliMap.put(KeyCode.ACCEPT, 0x1E);
		keyCodeIntelliMap.put(KeyCode.MODECHANGE, 0x1F);
		keyCodeIntelliMap.put(KeyCode.SPACE, 0x20);
		keyCodeIntelliMap.put(KeyCode.PAGE_UP, 0x21);
		keyCodeIntelliMap.put(KeyCode.PAGE_DOWN, 0x22);
		keyCodeIntelliMap.put(KeyCode.END, 0x23);
		keyCodeIntelliMap.put(KeyCode.HOME, 0x24);
		keyCodeIntelliMap.put(KeyCode.LEFT, 0x25);
		keyCodeIntelliMap.put(KeyCode.UP, 0x26);
		keyCodeIntelliMap.put(KeyCode.RIGHT, 0x27);
		keyCodeIntelliMap.put(KeyCode.DOWN, 0x28);
		keyCodeIntelliMap.put(KeyCode.PRINTSCREEN, 0x2C);
		keyCodeIntelliMap.put(KeyCode.INSERT, 0x2D);
		keyCodeIntelliMap.put(KeyCode.DELETE, 0x2E);
		keyCodeIntelliMap.put(KeyCode.HELP, 0x2F);
		keyCodeIntelliMap.put(KeyCode.DIGIT0, 0x30);
		keyCodeIntelliMap.put(KeyCode.DIGIT1, 0x31);
		keyCodeIntelliMap.put(KeyCode.DIGIT2, 0x32);
		keyCodeIntelliMap.put(KeyCode.DIGIT3, 0x33);
		keyCodeIntelliMap.put(KeyCode.DIGIT4, 0x34);
		keyCodeIntelliMap.put(KeyCode.DIGIT5, 0x35);
		keyCodeIntelliMap.put(KeyCode.DIGIT6, 0x36);
		keyCodeIntelliMap.put(KeyCode.DIGIT7, 0x37);
		keyCodeIntelliMap.put(KeyCode.DIGIT8, 0x38);
		keyCodeIntelliMap.put(KeyCode.DIGIT9, 0x39);
		keyCodeIntelliMap.put(KeyCode.A, 0x41);
		keyCodeIntelliMap.put(KeyCode.B, 0x42);
		keyCodeIntelliMap.put(KeyCode.C, 0x43);
		keyCodeIntelliMap.put(KeyCode.D, 0x44);
		keyCodeIntelliMap.put(KeyCode.E, 0x45);
		keyCodeIntelliMap.put(KeyCode.F, 0x46);
		keyCodeIntelliMap.put(KeyCode.G, 0x47);
		keyCodeIntelliMap.put(KeyCode.H, 0x48);
		keyCodeIntelliMap.put(KeyCode.I, 0x49);
		keyCodeIntelliMap.put(KeyCode.J, 0x4A);
		keyCodeIntelliMap.put(KeyCode.K, 0x4B);
		keyCodeIntelliMap.put(KeyCode.L, 0x4C);
		keyCodeIntelliMap.put(KeyCode.M, 0x4D);
		keyCodeIntelliMap.put(KeyCode.N, 0x4E);
		keyCodeIntelliMap.put(KeyCode.O, 0x4F);
		keyCodeIntelliMap.put(KeyCode.P, 0x50);
		keyCodeIntelliMap.put(KeyCode.Q, 0x51);
		keyCodeIntelliMap.put(KeyCode.R, 0x52);
		keyCodeIntelliMap.put(KeyCode.S, 0x53);
		keyCodeIntelliMap.put(KeyCode.T, 0x54);
		keyCodeIntelliMap.put(KeyCode.U, 0x55);
		keyCodeIntelliMap.put(KeyCode.V, 0x56);
		keyCodeIntelliMap.put(KeyCode.W, 0x57);
		keyCodeIntelliMap.put(KeyCode.X, 0x58);
		keyCodeIntelliMap.put(KeyCode.Y, 0x59);
		keyCodeIntelliMap.put(KeyCode.Z, 0x5A);
		keyCodeIntelliMap.put(KeyCode.WINDOWS, 0x5B);
		keyCodeIntelliMap.put(KeyCode.NUMPAD0, 0x60);
		keyCodeIntelliMap.put(KeyCode.NUMPAD1, 0x61);
		keyCodeIntelliMap.put(KeyCode.NUMPAD2, 0x62);
		keyCodeIntelliMap.put(KeyCode.NUMPAD3, 0x63);
		keyCodeIntelliMap.put(KeyCode.NUMPAD4, 0x64);
		keyCodeIntelliMap.put(KeyCode.NUMPAD5, 0x65);
		keyCodeIntelliMap.put(KeyCode.NUMPAD6, 0x66);
		keyCodeIntelliMap.put(KeyCode.NUMPAD7, 0x67);
		keyCodeIntelliMap.put(KeyCode.NUMPAD8, 0x68);
		keyCodeIntelliMap.put(KeyCode.NUMPAD9, 0x69);
		keyCodeIntelliMap.put(KeyCode.MULTIPLY, 0x6A);
		keyCodeIntelliMap.put(KeyCode.ADD, 0x6B);
		keyCodeIntelliMap.put(KeyCode.SEPARATOR, 0x6C);
		keyCodeIntelliMap.put(KeyCode.SUBTRACT, 0x6D);
		keyCodeIntelliMap.put(KeyCode.DECIMAL, 0x6E);
		keyCodeIntelliMap.put(KeyCode.DIVIDE, 0x6F);
		keyCodeIntelliMap.put(KeyCode.F1, 0x70);
		keyCodeIntelliMap.put(KeyCode.F2, 0x71);
		keyCodeIntelliMap.put(KeyCode.F3, 0x72);
		keyCodeIntelliMap.put(KeyCode.F4, 0x73);
		keyCodeIntelliMap.put(KeyCode.F5, 0x74);
		keyCodeIntelliMap.put(KeyCode.F6, 0x75);
		keyCodeIntelliMap.put(KeyCode.F7, 0x76);
		keyCodeIntelliMap.put(KeyCode.F8, 0x77);
		keyCodeIntelliMap.put(KeyCode.F9, 0x78);
		keyCodeIntelliMap.put(KeyCode.F10, 0x79);
		keyCodeIntelliMap.put(KeyCode.F11, 0x7A);
		keyCodeIntelliMap.put(KeyCode.F12, 0x7B);
		keyCodeIntelliMap.put(KeyCode.F13, 0x7C);
		keyCodeIntelliMap.put(KeyCode.F14, 0x7D);
		keyCodeIntelliMap.put(KeyCode.F15, 0x7E);
		keyCodeIntelliMap.put(KeyCode.F16, 0x7F);
		keyCodeIntelliMap.put(KeyCode.F17, 0x80);
		keyCodeIntelliMap.put(KeyCode.F18, 0x81);
		keyCodeIntelliMap.put(KeyCode.F19, 0x82);
		keyCodeIntelliMap.put(KeyCode.F20, 0x83);
		keyCodeIntelliMap.put(KeyCode.F21, 0x84);
		keyCodeIntelliMap.put(KeyCode.F22, 0x85);
		keyCodeIntelliMap.put(KeyCode.F23, 0x86);
		keyCodeIntelliMap.put(KeyCode.F24, 0x87);
		keyCodeIntelliMap.put(KeyCode.NUM_LOCK, 0x90);
		keyCodeIntelliMap.put(KeyCode.SCROLL_LOCK, 0x91);
		keyCodeIntelliMap.put(KeyCode.MUTE, 0xAD);
		keyCodeIntelliMap.put(KeyCode.VOLUME_DOWN, 0xAE);
		keyCodeIntelliMap.put(KeyCode.VOLUME_UP, 0xAF);
		keyCodeIntelliMap.put(KeyCode.TRACK_NEXT, 0xB0);
		keyCodeIntelliMap.put(KeyCode.TRACK_PREV, 0xB1);
		keyCodeIntelliMap.put(KeyCode.STOP, 0xB2);
		keyCodeIntelliMap.put(KeyCode.PLAY, 0xB3);
	}

	public static int[] toIntelliShortcut(KeyCodeCombination combination) {
		int[] result = new int[2];
		if (combination.getControl() == KeyCombination.ModifierValue.DOWN) {
			result[0] += JIntellitype.MOD_CONTROL;
		}
		if (combination.getShift() == KeyCombination.ModifierValue.DOWN) {
			result[0] += JIntellitype.MOD_SHIFT;
		}
		if (combination.getAlt() == KeyCombination.ModifierValue.DOWN) {
			result[0] += JIntellitype.MOD_ALT;
		}

		result[1] = keyCodeIntelliMap.get(combination.getCode());
		return result;
	}

}
