package zed.rainxch.githubstore.core.presentation.utils

import java.awt.Desktop
import java.net.URI

actual fun openBrowser(url: String) {
    try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI(url))
        } else {
            // Fallback for Linux/WSL
            Runtime.getRuntime().exec(arrayOf("xdg-open", url))
        }
    } catch (e: Exception) {
        // Final fallback - try common browsers directly
        val browsers = listOf("xdg-open", "firefox", "google-chrome", "chromium")
        var success = false

        for (browser in browsers) {
            try {
                Runtime.getRuntime().exec(arrayOf(browser, url))
                success = true
                break
            } catch (e: Exception) {
                continue
            }
        }

        if (!success) {
            println("Could not open browser. Please visit: $url")
        }
    }
}