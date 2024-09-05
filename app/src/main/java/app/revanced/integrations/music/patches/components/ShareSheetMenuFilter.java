package app.revanced.integrations.music.patches.components;

import androidx.annotation.Nullable;

import app.revanced.integrations.music.patches.misc.ShareSheetPatch;
import app.revanced.integrations.music.settings.Settings;
import app.revanced.integrations.shared.patches.components.Filter;
import app.revanced.integrations.shared.patches.components.StringFilterGroup;

/**
 * Abuse LithoFilter for {@link ShareSheetPatch}.
 */
public final class ShareSheetMenuFilter extends Filter {
    // Must be volatile or synchronized, as litho filtering runs off main thread and this field is then access from the main thread.
    public static volatile boolean isShareSheetMenuVisible;

    public ShareSheetMenuFilter() {
        addIdentifierCallbacks(
                new StringFilterGroup(
                        Settings.CHANGE_SHARE_SHEET,
                        "share_sheet_container.eml"
                )
        );
    }

    @Override
    public boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                              StringFilterGroup matchedGroup, FilterContentType contentType, int contentIndex) {
        isShareSheetMenuVisible = true;

        return false;
    }
}
