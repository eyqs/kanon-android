package ca.eyqs.kanon;

public class NotePosition {
    public int x;
    public int y;
    public int a;
    public NotePosition(int position, int height, int accidental) {
        x = position;
        y = height;
        a = accidental;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getA() { return a; }
}
