package app.revanced.integrations.youtube.patches.components;

import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import app.revanced.integrations.shared.patches.components.ByteArrayFilterGroup;
import app.revanced.integrations.shared.patches.components.ByteArrayFilterGroupList;
import app.revanced.integrations.shared.patches.components.Filter;
import app.revanced.integrations.shared.patches.components.StringFilterGroup;
import app.revanced.integrations.youtube.settings.Settings;

import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class ShortsButtonFilter extends Filter {
    // Pattern: reel_comment_button … number (of comments) + space? + character / letter (for comments) … 4 (random number),
    // probably unstable.
    // If comment button does not have number of comments, then it is disabled or with label "0".
    private static final Pattern REEL_COMMENTS_DISABLED_PATTERN = Pattern.compile("reel_comment_button.+\\d\\s?\\p{L}.+4");
    private final static String REEL_CHANNEL_BAR_PATH = "reel_channel_bar.eml";
    private final static String REEL_LIVE_HEADER_PATH = "immersive_live_header.eml";
    /**
     * For paid promotion label and subscribe button that appears in the channel bar.
     */
    private final static String REEL_METAPANEL_PATH = "reel_metapanel.eml";

    private final static String SHORTS_PAUSED_STATE_BUTTON_PATH = "|ScrollableContainerType|ContainerType|button.eml|";

    private final StringFilterGroup subscribeButton;
    private final StringFilterGroup joinButton;
    private final StringFilterGroup pausedOverlayButtons;
    private final StringFilterGroup metaPanelButton;
    private final ByteArrayFilterGroupList pausedOverlayButtonsGroupList = new ByteArrayFilterGroupList();

    private final ByteArrayFilterGroup shortsCommentDisabled;

    private final StringFilterGroup suggestedAction;
    private final ByteArrayFilterGroupList suggestedActionsGroupList = new ByteArrayFilterGroupList();

    private final StringFilterGroup actionBar;
    private final ByteArrayFilterGroupList videoActionButtonGroupList = new ByteArrayFilterGroupList();

    private final ByteArrayFilterGroup useThisSoundButton = new ByteArrayFilterGroup(
            Settings.HIDE_SHORTS_USE_THIS_SOUND_BUTTON,
            "yt_outline_camera"
    );

    public ShortsButtonFilter() {
        StringFilterGroup floatingButton = new StringFilterGroup(
                Settings.HIDE_SHORTS_FLOATING_BUTTON,
                "floating_action_button"
        );

        addIdentifierCallbacks(floatingButton);

        pausedOverlayButtons = new StringFilterGroup(
                null,
                "shorts_paused_state"
        );

        StringFilterGroup channelBar = new StringFilterGroup(
                Settings.HIDE_SHORTS_CHANNEL_BAR,
                REEL_CHANNEL_BAR_PATH
        );

        StringFilterGroup fullVideoLinkLabel = new StringFilterGroup(
                Settings.HIDE_SHORTS_FULL_VIDEO_LINK_LABEL,
                "reel_multi_format_link"
        );

        StringFilterGroup videoTitle = new StringFilterGroup(
                Settings.HIDE_SHORTS_VIDEO_TITLE,
                "shorts_video_title_item"
        );

        StringFilterGroup reelSoundMetadata = new StringFilterGroup(
                Settings.HIDE_SHORTS_SOUND_METADATA_LABEL,
                "reel_sound_metadata"
        );

        StringFilterGroup infoPanel = new StringFilterGroup(
                Settings.HIDE_SHORTS_INFO_PANEL,
                "shorts_info_panel_overview"
        );

        StringFilterGroup stickers = new StringFilterGroup(
                Settings.HIDE_SHORTS_STICKERS,
                "stickers_layer.eml"
        );

        StringFilterGroup liveHeader = new StringFilterGroup(
                Settings.HIDE_SHORTS_LIVE_HEADER,
                "immersive_live_header"
        );

        StringFilterGroup paidPromotionButton = new StringFilterGroup(
                Settings.HIDE_SHORTS_PAID_PROMOTION_LABEL,
                "reel_player_disclosure.eml"
        );

        metaPanelButton = new StringFilterGroup(
                null,
                "|ContainerType|button.eml|"
        );

        joinButton = new StringFilterGroup(
                Settings.HIDE_SHORTS_JOIN_BUTTON,
                "sponsor_button"
        );

        subscribeButton = new StringFilterGroup(
                Settings.HIDE_SHORTS_SUBSCRIBE_BUTTON,
                "subscribe_button"
        );

        actionBar = new StringFilterGroup(
                null,
                "shorts_action_bar"
        );

        suggestedAction = new StringFilterGroup(
                null,
                "|suggested_action_inner.eml|"
        );

        addPathCallbacks(
                suggestedAction, actionBar, joinButton, subscribeButton, metaPanelButton,
                paidPromotionButton, pausedOverlayButtons, channelBar, fullVideoLinkLabel,
                videoTitle, reelSoundMetadata, infoPanel, liveHeader, stickers
        );

        //
        // Action buttons
        //
        shortsCommentDisabled =
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_COMMENTS_DISABLED_BUTTON,
                        "reel_comment_button"
                );

        videoActionButtonGroupList.addAll(
                // This also appears as the path item 'shorts_like_button.eml'
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_LIKE_BUTTON,
                        "reel_like_button",
                        "reel_like_toggled_button"
                ),
                // This also appears as the path item 'shorts_dislike_button.eml'
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_DISLIKE_BUTTON,
                        "reel_dislike_button",
                        "reel_dislike_toggled_button"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_COMMENTS_BUTTON,
                        "reel_comment_button"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_SHARE_BUTTON,
                        "reel_share_button"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_REMIX_BUTTON,
                        "reel_remix_button"
                ),
                new ByteArrayFilterGroup(
                        Settings.DISABLE_SHORTS_LIKE_BUTTON_FOUNTAIN_ANIMATION,
                        "shorts_like_fountain"
                )
        );

        //
        // Paused overlay buttons.
        //
        pausedOverlayButtonsGroupList.addAll(
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_TRENDS_BUTTON,
                        "yt_outline_fire_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_SHOPPING_BUTTON,
                        "yt_outline_bag_"
                )
        );

        //
        // Suggested actions.
        //
        suggestedActionsGroupList.addAll(
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_TAGGED_PRODUCTS,
                        // Product buttons show pictures of the products, and does not have any unique icons to identify.
                        // Instead, use a unique identifier found in the buffer.
                        "PAproduct_listZ"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_SHOP_BUTTON,
                        "yt_outline_bag_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_LOCATION_BUTTON,
                        "yt_outline_location_point_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_SAVE_MUSIC_BUTTON,
                        "yt_outline_list_add_",
                        "yt_outline_bookmark_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_SEARCH_SUGGESTIONS_BUTTON,
                        "yt_outline_search_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_SUPER_THANKS_BUTTON,
                        "yt_outline_dollar_sign_heart_"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_USE_TEMPLATE_BUTTON,
                        "yt_outline_template_add"
                ),
                new ByteArrayFilterGroup(
                        Settings.HIDE_SHORTS_GREEN_SCREEN_BUTTON,
                        "shorts_green_screen"
                ),
                useThisSoundButton
        );
    }

    private boolean isEverySuggestedActionFilterEnabled() {
        for (ByteArrayFilterGroup group : suggestedActionsGroupList)
            if (!group.isEnabled()) return false;

        return true;
    }

    @Override
    public boolean isFiltered(String path, @Nullable String identifier, String allValue, byte[] protobufBufferArray,
                              StringFilterGroup matchedGroup, FilterContentType contentType, int contentIndex) {
        if (matchedGroup == subscribeButton || matchedGroup == joinButton) {
            // Selectively filter to avoid false positive filtering of other subscribe/join buttons.
            if (StringUtils.startsWithAny(path, REEL_CHANNEL_BAR_PATH, REEL_LIVE_HEADER_PATH, REEL_METAPANEL_PATH)) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            }
            return false;
        }

        if (matchedGroup == metaPanelButton) {
            if (path.startsWith(REEL_METAPANEL_PATH) && useThisSoundButton.check(protobufBufferArray).isFiltered()) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            }
            return false;
        }

        // Video action buttons (like, dislike, comment, share, remix) have the same path.
        if (matchedGroup == actionBar) {
            // If the Comment button is hidden, there is no need to check {@code REEL_COMMENTS_DISABLED_PATTERN}.
            // Check {@code videoActionButtonGroupList} first.
            if (videoActionButtonGroupList.check(protobufBufferArray).isFiltered()) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            }

            String protobufString = new String(protobufBufferArray);
            if (shortsCommentDisabled.check(protobufBufferArray).isFiltered()) {
                return !REEL_COMMENTS_DISABLED_PATTERN.matcher(protobufString).find();
            }
            return false;
        }

        if (matchedGroup == suggestedAction) {
            if (isEverySuggestedActionFilterEnabled()) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            }
            // Suggested actions can be at the start or in the middle of a path.
            if (suggestedActionsGroupList.check(protobufBufferArray).isFiltered()) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            }
            return false;
        }

        if (matchedGroup == pausedOverlayButtons) {
            if (Settings.HIDE_SHORTS_PAUSED_OVERLAY_BUTTONS.get()) {
                return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
            } else if (StringUtils.contains(path, SHORTS_PAUSED_STATE_BUTTON_PATH)) {
                if (pausedOverlayButtonsGroupList.check(protobufBufferArray).isFiltered()) {
                    return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
                }
            }
            return false;
        }

        // Super class handles logging.
        return super.isFiltered(path, identifier, allValue, protobufBufferArray, matchedGroup, contentType, contentIndex);
    }
}
