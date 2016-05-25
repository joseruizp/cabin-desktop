package com.cabin.desktop;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class WindowsSecurity implements Runnable {
	private Window frame;
	private boolean running;

	private static HHOOK hhk;
	private static LowLevelKeyboardProc keyboardHook;
	private static User32 lib;

	public WindowsSecurity(Window window) {
		this.frame = window;
		this.running = true;
		new Thread(this).start();
	}

	public void stop() {
		this.running = false;
		this.unblockWindowsKey();
	}

	public void run() {
		this.frame.setAlwaysOnTop(true);
		if (this.frame instanceof JFrame)
			((JFrame) this.frame).setDefaultCloseOperation(0);
		if (this.frame instanceof JDialog)
			((JDialog) this.frame).setDefaultCloseOperation(0);
		System.out.println("Terminating explorer");
		System.out.println("Waiting for lock release");

		blockWindowsKey();
		while (running) {
			sleep(30L);
			try {
				Robot robot = new Robot();
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_TAB);
			} catch (AWTException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

	private void blockWindowsKey() {
		lib = User32.INSTANCE;
		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		keyboardHook = new LowLevelKeyboardProc() {
			public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT info) {
				if (nCode >= 0) {
					switch (info.vkCode) {
					case 0x5B:
					case 0x5C:
						return new LRESULT(1);
					default: // do nothing
					}
				}
				return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
			}
		};
		hhk = lib.SetWindowsHookEx(13, keyboardHook, hMod, 0);

		// This bit never returns from GetMessage
		int result;
		MSG msg = new MSG();
		while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
			if (result == -1) {
				break;
			} else {
				lib.TranslateMessage(msg);
				lib.DispatchMessage(msg);
			}
		}
		lib.UnhookWindowsHookEx(hhk);
	}

	private void unblockWindowsKey() {
		if (isWindows() && lib != null) {
			lib.UnhookWindowsHookEx(hhk);
		}
	}

	public boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

}