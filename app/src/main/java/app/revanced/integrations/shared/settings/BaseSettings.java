package app.revanced.integrations.shared.settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
    public static final BooleanSetting HIDE_FULLSCREEN_ADS_TYPE = new BooleanSetting("revanced_hide_fullscreen_ads_type", TRUE, true);

    public static final BooleanSetting BYPASS_IMAGE_REGION_RESTRICTIONS = new BooleanSetting("revanced_bypass_image_region_restrictions", FALSE, true);
    public static final StringSetting BYPASS_IMAGE_REGION_RESTRICTIONS_DOMAIN = new StringSetting("revanced_bypass_image_region_restrictions_domain", "yt4.ggpht.com", true);
}
