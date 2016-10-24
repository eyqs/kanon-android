package ca.eyqs.kanon;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NoteValue {
    private static final int[] LINE_MAP =
        { 0, -1, 1, -1, 2, 3, -3, 4, -4, 5, -5, 6 };
    private static final Character[] WHITE_NOTES_ARRAY =
        { 'C', 'D', 'E', 'F', 'G', 'A', 'B' };
    private static final List<Character> WHITE_NOTES =
        Arrays.asList(WHITE_NOTES_ARRAY);
    private static final Map<Character, Integer> PITCH_MAP = makePitchMap();
    private static Map<Character, Integer> makePitchMap() {
        Map<Character, Integer> res = new HashMap<>(7);
        res.put('C', 0);
        res.put('D', 2);
        res.put('E', 4);
        res.put('F', 5);
        res.put('G', 7);
        res.put('A', 9);
        res.put('B', 11);
        return Collections.unmodifiableMap(res);
    }
    private static final Map<String, Integer> ACCIDENTAL_MAP = makeAcciMap();
    private static Map<String, Integer> makeAcciMap() {
        Map<String, Integer> res = new HashMap<>(5);
        res.put("bb", -2);
        res.put("b", -1);
        res.put("", 0);
        res.put("#", 1);
        res.put("x", 2);
        return Collections.unmodifiableMap(res);
    }
    private static final Map<String, Integer> INTERVAL_MAP = makeIvalMap();
    private static Map<String, Integer> makeIvalMap() {
        Map<String, Integer> res = new HashMap<>();
        res.put("d1", -1);
        res.put("P1", 0);
        res.put("A1", 1);
        res.put("d2", 0);
        res.put("m2", 1);
        res.put("M2", 2);
        res.put("A2", 3);
        res.put("d3", 2);
        res.put("m3", 3);
        res.put("M3", 4);
        res.put("A3", 5);
        res.put("d4", 4);
        res.put("P4", 5);
        res.put("A4", 6);
        res.put("d5", 6);
        res.put("P5", 7);
        res.put("A5", 8);
        res.put("d6", 7);
        res.put("m6", 8);
        res.put("M6", 9);
        res.put("A6", 10);
        res.put("d7", 9);
        res.put("m7", 10);
        res.put("M7", 11);
        res.put("A7", 12);
        res.put("d8", 11);
        res.put("P8", 12);
        res.put("A8", 13);
        return Collections.unmodifiableMap(res);
    };
    private final int midi;
    private final char pitch;
    private final int octave;
    private final int accidental;

    NoteValue(String name) {
        pitch = name.charAt(0);
        octave = Integer.parseInt(
            name.substring(name.length() - 1, name.length()));
        accidental = ACCIDENTAL_MAP.get(name.substring(1, name.length() - 1));
        midi = (octave + 1) * 12 + PITCH_MAP.get(pitch) + accidental;
    }

    public int getHeight(int middleCPosition) {
        int line = LINE_MAP[PITCH_MAP.get(pitch) % 12];
        return middleCPosition - line - 7 * (octave - 4);
    }

    public int getAccidental() {
        return accidental;
    }

    public int getMidi() {
        return midi;
    }

    public NoteValue transposeUp(String interval) {
        int compound = 0;
        int size = Integer.parseInt(interval.substring(1));
        while (size > 8) {
            compound++;
            size -= 7;
        }
        String simple = interval.substring(0, 1) + Integer.toString(size);

        int newMidi = midi + compound * 12 + INTERVAL_MAP.get(simple);
        int newOctave = octave + compound;
        int pitchIndex = WHITE_NOTES.indexOf(pitch) + size - 1;
        while (pitchIndex > 6) {
            newOctave++;
            pitchIndex -= 7;
        }
        char newPitch = WHITE_NOTES.get(pitchIndex);

        int currMidi = newOctave * 12 + PITCH_MAP.get(newPitch) + 12;
        String newAcci = "ERROR";
        for (String acci : ACCIDENTAL_MAP.keySet()) {
            if (currMidi + ACCIDENTAL_MAP.get(acci) == newMidi) {
                newAcci = acci;
                break;
            }
        }
        return new NoteValue(Character.toString(newPitch) +
            newAcci + Integer.toString(newOctave));
    }
}
