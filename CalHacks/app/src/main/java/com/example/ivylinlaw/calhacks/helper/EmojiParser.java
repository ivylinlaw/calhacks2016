package com.example.ivylinlaw.calhacks.helper;

import com.example.ivylinlaw.calhacks.R;
import com.microsoft.projectoxford.emotion.contract.Scores;

/**
 * Created by yubowang on 11/12/16.
 */

public class EmojiParser {
    private final static int ANGER = 0;
    private final static int CONTEMPT = 1;
    private final static int DISGUST = 2;
    private final static int FEAR = 3;
    private final static int HAPPINESS = 4;
    private final static int NEUTRAL = 5;
    private final static int SADNESS = 6;
    private final static int SURPRISE = 7;

    public static int getEmojibyScore(Scores scores)
    {
        double [] scorelist = new double[8];
        scorelist[0] = scores.anger;
        scorelist[1] = scores.contempt;
        scorelist[2] = scores.disgust;
        scorelist[3] = scores.fear;
        scorelist[4] = scores.happiness;
        scorelist[5] = scores.neutral;
        scorelist[6] = scores.sadness;
        scorelist[7] = scores.surprise;
        double max = 0;
        int max_index = 0;
        for(int i = 0; i < scorelist.length; i++)
        {
            if(max < scorelist[i])
            {
                max = scorelist[i];
                max_index = i;
            }
        }
        if(max_index == ANGER)
        {
            return R.drawable.anger_2;
        }
        else if(max_index == CONTEMPT)
        {
            return  R.drawable.contempt_1;
        }
        else if(max_index == DISGUST)
        {
            return  R.drawable.disgust_1;
        }
        else if(max_index == FEAR)
        {
            return  R.drawable.disgust_3;
        }
        else if(max_index == NEUTRAL)
        {
            return  R.drawable.neutral_1;
        }
        else if(max_index == HAPPINESS)
        {
            return R.drawable.happiness_1;
        }
        else if(max_index == SADNESS)
        {
            return R.drawable.sadness_4;
        }
        else if(max_index == SURPRISE)
        {
            return R.drawable.surprise_1;
        }
        return -1;
    }

}
