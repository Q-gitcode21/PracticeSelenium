package ostype;

import org.openqa.selenium.Keys;

public enum OS {
    WINDOWS,
    LINUX,
    MAC,
    OTHER;

    public static OS getOSName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return WINDOWS;
        } else if (osName.contains("linux") || osName.contains("unix")) {
            return LINUX;
        } else if (osName.contains("mac")) {
            return MAC;
        } else {
            return OTHER;
        }
    }

    public static Keys getShortcutModifierKey() {
        return switch (getOSName()) {
            case WINDOWS, LINUX -> Keys.CONTROL;
            case MAC -> Keys.COMMAND;
            default -> Keys.CONTROL;
        };
    }
}
