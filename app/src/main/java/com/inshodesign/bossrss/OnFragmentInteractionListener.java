package com.inshodesign.bossrss;

import com.inshodesign.bossrss.XMLModel.AudioStream;

/**
 * Created by JClassic on 3/8/2017.
 */

public interface OnFragmentInteractionListener {
    void getRSSFeed(final String feedURL);
    void showRemoveDialog(Integer removeRowID);
    void playAudio(AudioStream audioStream);
}