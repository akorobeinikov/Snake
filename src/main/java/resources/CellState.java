package resources;

public enum CellState {
    empty, eat, snake;
    public static CellState fromInteger(int x) {
        switch(x) {
            case 0:
                return empty;
            case 1:
                return eat;
            case 2:
                return snake;
        }
        return null;
    }
}
