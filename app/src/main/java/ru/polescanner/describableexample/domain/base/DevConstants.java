package ru.polescanner.describableexample.domain.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.polescanner.describableexample.R;

public class DevConstants {

    public static final Map<String, Integer> ITEM__VIEW_TYPE = initItemViewTypeMap();
    public static final Map<String, Integer> ITEM__LAYOUT = initItemLayoutMap();

    private static Map<String, Integer> initItemViewTypeMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("ImagePortrait", 0);
        map.put("ImageLandscape", 1);
        map.put("Video", 3);
        map.put("Audio", 4);
        map.put("Note", 5);
        map.put("AddDescriptionStub", 6);
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, Integer> initItemLayoutMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("ImagePortrait", R.layout.item_description_image_portrait);
        map.put("ImageLandscape", R.layout.item_description_image_landscape);
        map.put("Video", R.layout.item_description_video);
        map.put("Audio", R.layout.item_description_audio);
        map.put("Note", R.layout.item_description_note);
        map.put("AddDescriptionStub", R.layout.item_description_add_stub);
        return Collections.unmodifiableMap(map);
    }
}
