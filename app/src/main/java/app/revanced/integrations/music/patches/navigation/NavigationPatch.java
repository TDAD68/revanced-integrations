package app.revanced.integrations.music.patches.navigation;

import static app.revanced.integrations.shared.utils.Utils.hideViewUnderCondition;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import app.revanced.integrations.music.settings.Settings;

@SuppressWarnings("unused")
public class NavigationPatch {
    public static Enum<?> lastPivotTab;

    public static int enableBlackNavigationBar() {
        return Settings.ENABLE_BLACK_NAVIGATION_BAR.get() ? -16777216 : -14869219;
    }

    public static void hideNavigationLabel(TextView textview) {
        hideViewUnderCondition(Settings.HIDE_NAVIGATION_LABEL.get(), textview);
    }

    public static void hideNavigationButton(@NonNull View view) {
        if (Settings.HIDE_NAVIGATION_BAR.get() && view.getParent() != null) {
            hideViewUnderCondition(true, (View) view.getParent());
            return;
        }

        for (NavigationButton button : NavigationButton.values())
            if (lastPivotTab.name().equals(button.name))
                hideViewUnderCondition(button.enabled, view);
    }

    private enum NavigationButton {
        HOME("TAB_HOME", Settings.HIDE_NAVIGATION_HOME_BUTTON.get()),
        SAMPLES("TAB_SAMPLES", Settings.HIDE_NAVIGATION_SAMPLES_BUTTON.get()),
        EXPLORE("TAB_EXPLORE", Settings.HIDE_NAVIGATION_EXPLORE_BUTTON.get()),
        LIBRARY("LIBRARY_MUSIC", Settings.HIDE_NAVIGATION_LIBRARY_BUTTON.get()),
        UPGRADE("TAB_MUSIC_PREMIUM", Settings.HIDE_NAVIGATION_UPGRADE_BUTTON.get());

        private final boolean enabled;
        private final String name;

        NavigationButton(String name, boolean enabled) {
            this.enabled = enabled;
            this.name = name;
        }
    }
}
