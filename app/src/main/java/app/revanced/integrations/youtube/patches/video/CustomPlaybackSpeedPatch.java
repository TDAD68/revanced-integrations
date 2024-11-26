package app.revanced.integrations.youtube.patches.video;

import static app.revanced.integrations.shared.utils.ResourceUtils.getString;
import static app.revanced.integrations.shared.utils.StringRef.str;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.Arrays;

import app.revanced.integrations.shared.utils.Logger;
import app.revanced.integrations.shared.utils.Utils;
import app.revanced.integrations.youtube.patches.components.PlaybackSpeedMenuFilter;
import app.revanced.integrations.youtube.settings.Settings;
import app.revanced.integrations.youtube.utils.VideoUtils;

@SuppressWarnings("unused")
public class CustomPlaybackSpeedPatch {
    /**
     * Maximum playback speed, exclusive value.  Custom speeds must be less than this value.
     * <p>
     * Going over 8x does not increase the actual playback speed any higher,
     * and the UI selector starts flickering and acting weird.
     * Over 10x and the speeds show up out of order in the UI selector.
     */
    public static final float MAXIMUM_PLAYBACK_SPEED = 8;
    private static final String[] defaultSpeedEntries;
    private static final String[] defaultSpeedEntryValues;
    /**
     * Custom playback speeds.
     */
    private static float[] playbackSpeeds;
    private static String[] customSpeedEntries;
    private static String[] customSpeedEntryValues;

    private static String[] playbackSpeedEntries;
    private static String[] playbackSpeedEntryValues;

    /**
     * The last time the old playback menu was forcefully called.
     */
    private static long lastTimeOldPlaybackMenuInvoked;

    static {
        defaultSpeedEntries = new String[]{getString("quality_auto"), "0.25x", "0.5x", "0.75x", getString("revanced_playback_speed_normal"), "1.25x", "1.5x", "1.75x", "2.0x"};
        defaultSpeedEntryValues = new String[]{"-2.0", "0.25", "0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0"};

        loadCustomSpeeds();
    }

    /**
     * Injection point.
     */
    public static float[] getArray(float[] original) {
        return isCustomPlaybackSpeedEnabled() ? playbackSpeeds : original;
    }

    /**
     * Injection point.
     */
    public static int getLength(int original) {
        return isCustomPlaybackSpeedEnabled() ? playbackSpeeds.length : original;
    }

    /**
     * Injection point.
     */
    public static int getSize(int original) {
        return isCustomPlaybackSpeedEnabled() ? 0 : original;
    }

    public static String[] getListEntries() {
        return isCustomPlaybackSpeedEnabled()
                ? customSpeedEntries
                : defaultSpeedEntries;
    }

    public static String[] getListEntryValues() {
        return isCustomPlaybackSpeedEnabled()
                ? customSpeedEntryValues
                : defaultSpeedEntryValues;
    }

    public static String[] getTrimmedListEntries() {
        if (playbackSpeedEntries == null) {
            final String[] playbackSpeedWithAutoEntries = getListEntries();
            playbackSpeedEntries = Arrays.copyOfRange(playbackSpeedWithAutoEntries, 1, playbackSpeedWithAutoEntries.length);
        }

        return playbackSpeedEntries;
    }

    public static String[] getTrimmedListEntryValues() {
        if (playbackSpeedEntryValues == null) {
            final String[] playbackSpeedWithAutoEntryValues = getListEntryValues();
            playbackSpeedEntryValues = Arrays.copyOfRange(playbackSpeedWithAutoEntryValues, 1, playbackSpeedWithAutoEntryValues.length);
        }

        return playbackSpeedEntryValues;
    }

    private static void resetCustomSpeeds(@NonNull String toastMessage) {
        Utils.showToastLong(toastMessage);
        Utils.showToastShort(str("revanced_extended_reset_to_default_toast"));
        Settings.CUSTOM_PLAYBACK_SPEEDS.resetToDefault();
    }

    private static void loadCustomSpeeds() {
        try {
            if (!Settings.ENABLE_CUSTOM_PLAYBACK_SPEED.get()) {
                return;
            }

            String[] speedStrings = Settings.CUSTOM_PLAYBACK_SPEEDS.get().split("\\s+");
            Arrays.sort(speedStrings);
            if (speedStrings.length == 0) {
                throw new IllegalArgumentException();
            }
            playbackSpeeds = new float[speedStrings.length];
            int i = 0;
            for (String speedString : speedStrings) {
                final float speedFloat = Float.parseFloat(speedString);
                if (speedFloat <= 0 || arrayContains(playbackSpeeds, speedFloat)) {
                    throw new IllegalArgumentException();
                }

                if (speedFloat > MAXIMUM_PLAYBACK_SPEED) {
                    resetCustomSpeeds(str("revanced_custom_playback_speeds_invalid", MAXIMUM_PLAYBACK_SPEED));
                    loadCustomSpeeds();
                    return;
                }

                playbackSpeeds[i] = speedFloat;
                i++;
            }

            if (customSpeedEntries != null) return;

            customSpeedEntries = new String[playbackSpeeds.length + 1];
            customSpeedEntryValues = new String[playbackSpeeds.length + 1];
            customSpeedEntries[0] = getString("quality_auto");
            customSpeedEntryValues[0] = "-2.0";

            i = 1;
            for (float speed : playbackSpeeds) {
                String speedString = String.valueOf(speed);
                customSpeedEntries[i] = speed != 1.0f
                        ? speedString + "x"
                        : getString("revanced_playback_speed_normal");
                customSpeedEntryValues[i] = speedString;
                i++;
            }
        } catch (Exception ex) {
            Logger.printInfo(() -> "parse error", ex);
            resetCustomSpeeds(str("revanced_custom_playback_speeds_parse_exception"));
            loadCustomSpeeds();
        }
    }

    private static boolean arrayContains(float[] array, float value) {
        for (float arrayValue : array) {
            if (arrayValue == value) return true;
        }
        return false;
    }

    private static boolean isCustomPlaybackSpeedEnabled() {
        return Settings.ENABLE_CUSTOM_PLAYBACK_SPEED.get() && playbackSpeeds != null;
    }

    /**
     * Injection point.
     */
    public static void onFlyoutMenuCreate(RecyclerView recyclerView) {
        if (!Settings.ENABLE_CUSTOM_PLAYBACK_SPEED.get()) {
            return;
        }

        recyclerView.getViewTreeObserver().addOnDrawListener(() -> {
            try {
                if (PlaybackSpeedMenuFilter.isOldPlaybackSpeedMenuVisible) {
                    if (hideLithoMenuAndShowOldSpeedMenu(recyclerView, 8)) {
                        PlaybackSpeedMenuFilter.isOldPlaybackSpeedMenuVisible = false;
                    }
                    return;
                }
            } catch (Exception ex) {
                Logger.printException(() -> "isOldPlaybackSpeedMenuVisible failure", ex);
            }

            try {
                if (PlaybackSpeedMenuFilter.isPlaybackRateSelectorMenuVisible) {
                    if (hideLithoMenuAndShowOldSpeedMenu(recyclerView, 5)) {
                        PlaybackSpeedMenuFilter.isPlaybackRateSelectorMenuVisible = false;
                    }
                }
            } catch (Exception ex) {
                Logger.printException(() -> "isPlaybackRateSelectorMenuVisible failure", ex);
            }
        });
    }

    private static boolean hideLithoMenuAndShowOldSpeedMenu(RecyclerView recyclerView, int expectedChildCount) {
        if (recyclerView.getChildCount() == 0) {
            return false;
        }

        if (!(recyclerView.getChildAt(0) instanceof ViewGroup PlaybackSpeedParentView)) {
            return false;
        }

        if (PlaybackSpeedParentView.getChildCount() != expectedChildCount) {
            return false;
        }

        if (!(Utils.getParentView(recyclerView, 3) instanceof ViewGroup parentView3rd)) {
            return false;
        }

        if (!(parentView3rd.getParent() instanceof ViewGroup parentView4th)) {
            return false;
        }

        // Dismiss View [R.id.touch_outside] is the 1st ChildView of the 4th ParentView.
        // This only shows in phone layout.
        Utils.clickView(parentView4th.getChildAt(0));

        // In tablet layout there is no Dismiss View, instead we just hide all two parent views.
        parentView3rd.setVisibility(View.GONE);
        parentView4th.setVisibility(View.GONE);

        // Show old playback speed menu.
        showCustomPlaybackSpeedMenu(recyclerView.getContext());

        return true;
    }

    /**
     * This method is sometimes used multiple times
     * To prevent this, ignore method reuse within 1 second.
     *
     * @param context Context for [playbackSpeedDialogListener]
     */
    private static void showCustomPlaybackSpeedMenu(@NonNull Context context) {
        // This method is sometimes used multiple times.
        // To prevent this, ignore method reuse within 1 second.
        final long now = System.currentTimeMillis();
        if (now - lastTimeOldPlaybackMenuInvoked < 1000) {
            return;
        }
        lastTimeOldPlaybackMenuInvoked = now;

        if (Settings.CUSTOM_PLAYBACK_SPEED_MENU_TYPE.get()) {
            // Open playback speed dialog
            VideoUtils.showPlaybackSpeedDialog(context);
        } else {
            // Open old style flyout menu
            VideoUtils.showPlaybackSpeedFlyoutMenu();
        }
    }

}
