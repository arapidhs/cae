package com.dungeoncode.cae.terminal.utils;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Utility class responsible for periodically capturing screenshots of a
 * {@link TerminalScreen} backed by a {@link SwingTerminalFrame}.
 * <p>
 * Screenshots are taken at a fixed interval and saved as PNG files under the
 * user's home directory in:
 * <pre>
 * ~/.cae/screenshots
 * </pre>
 * Each screenshot filename is generated using the provided filename prefix and
 * a millisecond-precision timestamp.
 * <p>
 * This class supports starting, stopping, and resuming screenshot capture.
 * Screenshot capture runs on a dedicated single-threaded scheduled executor.
 */
public final class ScreenshotRecorder {

    /**
     * Logger instance for recording lifecycle and error events.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotRecorder.class);

    /**
     * The interval in milliseconds between screenshots.
     */
    private final long intervalMillis;

    /**
     * The terminal screen to capture.
     */
    private final TerminalScreen screen;

    /**
     * Executor service responsible for scheduling screenshot tasks.
     */
    private ScheduledExecutorService executor;

    /**
     * Indicates whether screenshot recording is currently active.
     */
    private volatile boolean isRunning;

    /**
     * Constructs a new {@code ScreenshotRecorder}.
     *
     * @param intervalMillis the interval in milliseconds between screenshots
     * @param screen         the terminal screen to capture
     */
    public ScreenshotRecorder(final long intervalMillis,
                              final TerminalScreen screen) {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.intervalMillis = intervalMillis;
        this.screen = screen;
    }

    /**
     * Starts taking screenshots immediately at the configured interval.
     * <p>
     * This is equivalent to calling {@link #resume(String)}.
     *
     * @param filenamePrefix the prefix to use for generated screenshot filenames
     */
    public void start(final String filenamePrefix) {
        resume(filenamePrefix);
    }

    /**
     * Resumes periodic screenshot capture if it is not already running.
     * <p>
     * Screenshots are taken immediately and then repeatedly at the configured interval.
     *
     * @param filenamePrefix the prefix to use for generated screenshot filenames
     */
    public void resume(final String filenamePrefix) {
        if (!isRunning) {
            isRunning = true;
            LOGGER.info("Automaton resumed with interval: {}ms", intervalMillis);
            executor.scheduleAtFixedRate(() -> {
                try {
                    if (isRunning) {
                        takeScreenshot(filenamePrefix);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error taking screenshot: {}", e.getMessage(), e);
                    isRunning = false;
                }
            }, 0, intervalMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Stops screenshot capture and shuts down the executor service.
     * <p>
     * If the executor does not terminate within one second, it is forcibly shut down.
     * A new executor instance is created so recording can be restarted later.
     */
    public void stop() {
        if (executor != null) {
            isRunning = false;
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            LOGGER.info("Screenshot taking stopped");
            this.executor = Executors.newSingleThreadScheduledExecutor();
        }
    }

    /**
     * Returns whether screenshot recording is currently active.
     *
     * @return {@code true} if recording is active; {@code false} otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Captures the current contents of the terminal screen and saves it as a PNG file.
     * <p>
     * The screenshot is stored in the {@code ~/.cae/screenshots} directory using
     * the provided filename prefix and a timestamp.
     *
     * @param filenamePrefix the prefix to use for the screenshot filename
     * @throws IOException if the screenshot cannot be saved or the output directory
     *                     cannot be created
     * @throws IllegalStateException if the screen is not backed by a
     *                               {@link SwingTerminalFrame}
     */
    private void takeScreenshot(final String filenamePrefix) throws IOException {
        if (screen != null && screen.getTerminal() instanceof SwingTerminalFrame swingTerminalFrame) {
            Component component = swingTerminalFrame.getContentPane().getComponent(0);

            // Create a BufferedImage to hold the component's content
            BufferedImage image = new BufferedImage(
                    component.getWidth(),
                    component.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            // Paint the component to the image
            Graphics2D g2d = image.createGraphics();
            component.paint(g2d);
            g2d.dispose();

            // Create the screenshots directory in the user's home directory
            String userHome = System.getProperty("user.home");
            File screenshotDir = new File(userHome, ".cae/screenshots");
            if (!screenshotDir.exists() && !screenshotDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + screenshotDir.getAbsolutePath());
            }

            // Generate filename with filename prefix and millisecond-precision timestamp
            String timestamp = String.valueOf( System.currentTimeMillis() );
            String fileName = filenamePrefix + "_" + timestamp + ".png";
            File outputFile = new File(screenshotDir, fileName);

            // Save the image
            ImageIO.write(image, "png", outputFile);
            LOGGER.info("Screen saved to {}", outputFile.getAbsolutePath());
        } else {
            throw new IllegalStateException("Screen capture is only supported with SwingTerminalFrame");
        }
    }

}