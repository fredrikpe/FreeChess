package fred.freechess;

public enum PieceType {
    K,
    Q,
    B,
    N,
    R,
    P;

    public String toString(PieceColor color) {
        if (color == PieceColor.WHITE) {
            switch (this) {
                case K: return Character.toString((char) 0x2654);
                case Q: return Character.toString((char) 0x2655);
                case R: return Character.toString((char) 0x2656);
                case B: return Character.toString((char) 0x2657);
                case N: return Character.toString((char) 0x2658);
                case P: return Character.toString((char) 0x2659);
                default: throw new IllegalArgumentException();
            }
        } else {
            switch(this) {
                case K:
                    return Character.toString((char) 0x265A);
                case Q:
                    return Character.toString((char) 0x265B);
                case R:
                    return Character.toString((char) 0x265C);
                case B:
                    return Character.toString((char) 0x265D);
                case N:
                    return Character.toString((char) 0x265E);
                case P:
                    return Character.toString((char) 0x265F);
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}


