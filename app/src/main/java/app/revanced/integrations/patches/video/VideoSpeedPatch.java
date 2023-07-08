package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ReVancedUtils.showToastShort;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.REVANCED;
import static app.revanced.integrations.utils.SharedPrefHelper.getBoolean;
import static app.revanced.integrations.utils.SharedPrefHelper.getFloat;
import static app.revanced.integrations.utils.StringRef.str;

import java.util.Objects;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class VideoSpeedPatch {
    private static float selectedSpeed = 1.0f;
    private static String currentContentCpn;

    public static void newVideoStarted(final String contentCpn, final boolean isLive) {
        try {
            if (contentCpn.isEmpty() || Objects.equals(currentContentCpn, contentCpn))
                return;

            currentContentCpn = contentCpn;

            if (getBoolean(REVANCED, "revanced_disable_default_video_speed_live", true) && isLive)
                return;

            selectedSpeed = getFloat(REVANCED, "revanced_default_video_speed", 1.0f);

            overrideSpeed(selectedSpeed);
        } catch (Exception ex) {
            LogHelper.printException(VideoSpeedPatch.class, "Failed to setDefaultVideoSpeed", ex);
        }
    }

    public static void userChangedSpeed(final float speed) {
        selectedSpeed = speed;

        if (SettingsEnum.ENABLE_SAVE_VIDEO_SPEED.getBoolean()) {
            SettingsEnum.DEFAULT_VIDEO_SPEED.saveValue(speed);
            showToastShort(str("revanced_save_video_speed") + speed + "x");
        }
    }

    public static void overrideSpeed(final float speedValue) {
        if (speedValue != selectedSpeed)
            selectedSpeed = speedValue;
    }
}
