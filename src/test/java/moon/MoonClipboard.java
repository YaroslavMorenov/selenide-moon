package moon;

import com.codeborne.selenide.Clipboard;
import com.codeborne.selenide.DefaultClipboard;
import com.codeborne.selenide.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MoonClipboard implements Clipboard {
    private static final Logger log = LoggerFactory.getLogger(MoonClipboard.class);

    private final Driver driver;

    MoonClipboard(Driver driver) {
        this.driver = driver;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public Driver driver() {
        return driver;
    }

    @Nonnull
    @CheckReturnValue
    @Override
    public Clipboard object() {
        return this;
    }

    @CheckReturnValue
    @Nonnull
    @Override
    public String getText() {
        if (driver.config().remote() == null) {
            log.debug("Working in local browser. Switching to a default Clipboard implementation.");
            return new DefaultClipboard(driver).getText();
        } else {
            return new MoonClient(driver.config().remote(), driver.getSessionId().toString()).getClipboardText();
        }
    }

    @Override
    public void setText(String text) {
        if (driver.config().remote() == null) {
            log.debug("Working in local browser. Switching to a default Clipboard implementation.");
            new DefaultClipboard(driver).setText(text);
        } else {
            new MoonClient(driver.config().remote(), driver.getSessionId().toString()).setClipboardText(text);
        }
    }
}
