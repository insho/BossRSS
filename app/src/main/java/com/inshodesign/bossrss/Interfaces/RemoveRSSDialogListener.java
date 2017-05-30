package com.inshodesign.bossrss.Interfaces;

/**
 * Interface between {@link com.inshodesign.bossrss.Dialogs.RemoveFeedDialog} and {@link com.inshodesign.bossrss.MainActivity},
 * for handling RemoveRssDialog window click actions
 */
public interface RemoveRSSDialogListener {
    void onRemoveRSSDialogPositiveClick(String rssUrl);
}