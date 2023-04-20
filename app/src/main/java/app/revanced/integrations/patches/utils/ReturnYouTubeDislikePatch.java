package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike.Vote;

import android.text.SpannableString;
import android.text.Spanned;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicReference;

import app.revanced.integrations.returnyoutubedislike.ReturnYouTubeDislike;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.LogHelper;

public class ReturnYouTubeDislikePatch {

    /**
     * Injection point.
     */
    public static void newVideoLoaded(String videoId) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) return;
            ReturnYouTubeDislike.newVideoLoaded(videoId);
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "newVideoLoaded failure", ex);
        }
    }

    /**
     * Injection point.
     * <p>
     * Required to update the UI after the user dislikes.
     *
     * @param textRef Reference to the dislike char sequence. The CharSequence inside can be null.
     */
    public static void onComponentCreated(@NonNull Object conversionContext, @NonNull AtomicReference<CharSequence> textRef) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return;
            }

            SpannableString replacement = ReturnYouTubeDislike.getDislikeSpanForContext(conversionContext, textRef.get());
            if (replacement != null) {
                textRef.set(replacement);
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "onComponentCreated AtomicReference failure", ex);
        }
    }

    /**
     * Injection point.
     * <p>
     * Called when a litho text component is initially created.
     * <p>
     * This method is sometimes called on the main thread, but it usually is called _off_ the main thread.
     * This method can be called multiple times for the same UI element (including after dislikes was added).
     */
    public static CharSequence onComponentCreated(@NonNull Object conversionContext, @NonNull CharSequence original) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return original;
            }

            SpannableString dislikes = ReturnYouTubeDislike.getDislikeSpanForContext(conversionContext, original);
            if (dislikes != null) {
                return dislikes;
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "onComponentCreated CharSequence failure", ex);
        }
        return original;
    }

    /**
     * Injection point.
     * <p>
     * Called when a Shorts dislike Spanned is created.
     */
    public static Spanned onShortsComponentCreated(Spanned original) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return original;
            }
            Spanned replacement = ReturnYouTubeDislike.onShortsComponentCreated(original);
            if (replacement != null) {
                return replacement;
            }
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "onShortsComponentCreated failure", ex);
        }
        return original;
    }

    /**
     * Injection point.
     * <p>
     * Called when the user likes or dislikes.
     *
     * @param vote int that matches {@link ReturnYouTubeDislike.Vote#value}
     */
    public static void sendVote(int vote) {
        try {
            if (!SettingsEnum.RYD_ENABLED.getBoolean()) {
                return;
            }

            for (Vote v : Vote.values()) {
                if (v.value == vote) {
                    ReturnYouTubeDislike.sendVote(v);
                    return;
                }
            }
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "Unknown vote type: " + vote);
        } catch (Exception ex) {
            LogHelper.printException(ReturnYouTubeDislikePatch.class, "sendVote failure", ex);
        }
    }
}