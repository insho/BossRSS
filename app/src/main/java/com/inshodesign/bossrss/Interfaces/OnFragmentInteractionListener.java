package com.inshodesign.bossrss.Interfaces;

import com.inshodesign.bossrss.Fragments.RSSListFragment;
import com.inshodesign.bossrss.Fragments.RSSItemsFragment;
import com.inshodesign.bossrss.Models.AudioStream;

/**
 * Interface between fragments {@link RSSListFragment}, {@link RSSItemsFragment} and
 * {@link com.inshodesign.bossrss.MainActivity}. MainActivity handles RSS feed look-up and media streaming that is initiated
 * in the fragments
 */
public interface OnFragmentInteractionListener {
    void getRSSFeed(final String feedURL);
    void showRemoveDialog(String rssUrl);
    void playAudio(AudioStream audioStream);
    void showProgressBar(Boolean show);
}