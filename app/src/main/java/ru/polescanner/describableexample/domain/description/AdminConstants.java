package ru.polescanner.describableexample.domain.description;

public class AdminConstants {
    //ToDo clean that mess
    public static final double DESCRIPTION_THUMB_PORTRAIT_RATIO_WH = 2 / 3.0;
    public static final int DESCRIPTION_THUMB_PORTRAIT_HEIGHT = 100;
    public static final int DESCRIPTION_THUMB_PORTRAIT_WIDTH =
            (int) Math.round(DESCRIPTION_THUMB_PORTRAIT_HEIGHT
                                     * DESCRIPTION_THUMB_PORTRAIT_RATIO_WH);

    public static final double DESCRIPTION_THUMB_LANDSCAPE_RATIO_WH = 3 / 2.0;
    public static final int DESCRIPTION_THUMB_LANDSCAPE_HEIGHT = 100;
    public static final int DESCRIPTION_THUMB_LANDSCAPE_WIDTH =
            (int) Math.round(DESCRIPTION_THUMB_LANDSCAPE_HEIGHT
                                     * DESCRIPTION_THUMB_LANDSCAPE_RATIO_WH);

}
