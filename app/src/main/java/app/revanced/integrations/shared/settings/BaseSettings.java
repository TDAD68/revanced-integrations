package app.revanced.integrations.shared.settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import app.revanced.integrations.shared.patches.ReturnYouTubeUsernamePatch.DisplayFormat;

/**
 * Settings shared across multiple apps.
 * <p>
 * To ensure this class is loaded when the UI is created, app specific setting bundles should extend
 * or reference this class.
 */
public class BaseSettings {
    public static final BooleanSetting ENABLE_DEBUG_LOGGING = new BooleanSetting("revanced_enable_debug_logging", FALSE);
    /**
     * When enabled, share the debug logs with care.
     * The buffer contains select user data, including the client ip address and information that could identify the end user.
     */
    public static final BooleanSetting ENABLE_DEBUG_BUFFER_LOGGING = new BooleanSetting("revanced_enable_debug_buffer_logging", FALSE);
    public static final BooleanSetting SETTINGS_INITIALIZED = new BooleanSetting("revanced_settings_initialized", FALSE, false, false);
    public static final BooleanSetting GMS_SHOW_DIALOG = new BooleanSetting("revanced_gms_show_dialog", TRUE);

    /**
     * These settings are used by YouTube and YouTube Music.
     */
    public static final BooleanSetting HIDE_FULLSCREEN_ADS = new BooleanSetting("revanced_hide_fullscreen_ads", TRUE, true);
    public static final BooleanSetting HIDE_PROMOTION_ALERT_BANNER = new BooleanSetting("revanced_hide_promotion_alert_banner", TRUE);

    public static final BooleanSetting DISABLE_AUTO_CAPTIONS = new BooleanSetting("revanced_disable_auto_captions", FALSE, true);

    public static final BooleanSetting BYPASS_IMAGE_REGION_RESTRICTIONS = new BooleanSetting("revanced_bypass_image_region_restrictions", FALSE, true);
    public static final StringSetting BYPASS_IMAGE_REGION_RESTRICTIONS_DOMAIN = new StringSetting("revanced_bypass_image_region_restrictions_domain", "yt4.ggpht.com", true);

    public static final BooleanSetting RETURN_YOUTUBE_USERNAME_ENABLED = new BooleanSetting("revanced_return_youtube_username_enabled", FALSE, true);
    public static final EnumSetting<DisplayFormat> RETURN_YOUTUBE_USERNAME_DISPLAY_FORMAT = new EnumSetting<>("revanced_return_youtube_username_display_format", DisplayFormat.USERNAME_ONLY, true);
    public static final StringSetting RETURN_YOUTUBE_USERNAME_YOUTUBE_DATA_API_V3_DEVELOPER_KEY = new StringSetting("revanced_return_youtube_username_youtube_data_api_v3_developer_key", "", true, false);

    public static final BooleanSetting SANITIZE_SHARING_LINKS = new BooleanSetting("revanced_sanitize_sharing_links", TRUE, true);
}
