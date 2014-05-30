package org.train.helper;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Font;

public class StringHelper {
    public static List<String> createLines(String text, int lineWidth, Font font) {
        String chunks[] = text.split(" ");
        ArrayList<String> lines = new ArrayList<String>(chunks.length);
        String line = "";
        int last = 0;
        for (int j = 0; j < chunks.length; j++) {
            String additionalText = line.equals("") ? chunks[j] : " " + chunks[j];
            if (font.getWidth(line + additionalText) > lineWidth) {
                if (last == j) {
                    line += chunks[j];
                }
                lines.add(line);
                line = (last == j) ? "" : chunks[j];
                last = j;
            } else {
                line += additionalText;
            }
        }
        lines.add(line);

        return lines;
    }
}
