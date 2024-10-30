package app.revanced.integrations.music.settings.preference;

import static app.revanced.integrations.music.utils.ExtendedUtils.getDialogBuilder;
import static app.revanced.integrations.shared.utils.ResourceUtils.getStringArray;
import static app.revanced.integrations.shared.utils.StringRef.str;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.Arrays;

import app.revanced.integrations.shared.settings.Setting;
import app.revanced.integrations.shared.utils.Logger;

public class ResettableListPreference {
    private static int mClickedDialogEntryIndex;

    public static void showDialog(Activity mActivity, @NonNull Setting<String> setting, int defaultIndex) {
        try {
            final String settingsKey = setting.key;

            final String entryKey = settingsKey + "_entries";
            final String entryValueKey = settingsKey + "_entry_value";
            final String[] mEntries = getStringArray(entryKey);
            final String[] mEntryValues = getStringArray(entryValueKey);

            final int findIndex = Arrays.binarySearch(mEntryValues, setting.get());
            mClickedDialogEntryIndex = findIndex >= 0 ? findIndex : defaultIndex;

            getDialogBuilder(mActivity)
                    .setTitle(str(settingsKey + "_title"))
                    .setSingleChoiceItems(mEntries, mClickedDialogEntryIndex,
                            (dialog, id) -> mClickedDialogEntryIndex = id)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_extended_settings_reset"), (dialog, which) -> {
                        setting.resetToDefault();
                        ReVancedPreferenceFragment.showRebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        setting.save(mEntryValues[mClickedDialogEntryIndex]);
                        ReVancedPreferenceFragment.showRebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            Logger.printException(() -> "showDialog failure", ex);
        }
    }
}
