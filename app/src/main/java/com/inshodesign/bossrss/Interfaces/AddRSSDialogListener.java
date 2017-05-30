package com.inshodesign.bossrss.Interfaces;

import com.inshodesign.bossrss.Dialogs.AddFeedDialog;

/**
 * Interface between {@link AddFeedDialog} and {@link com.inshodesign.bossrss.MainActivity}, for handlign
 * AddRssDialog window click actions
 */
public interface AddRSSDialogListener {
    void saveRSSFeed(String rssURI, boolean isIntent);
}