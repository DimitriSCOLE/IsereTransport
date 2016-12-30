package com.mazzone.isere.transport.util;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

public class AnswersUtil {

    public static final String TYPE_TIME_TABLE = "TimeTable";

    private static final String KEY_STOP_NAME = "Stop name";

    private AnswersUtil() {
    }

    public static void logContentView(String description, String type, String id, String name) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(description)
                .putContentType(type)
                .putContentId(id)
                .putCustomAttribute(KEY_STOP_NAME, name));
    }

}
