package resources;

public enum CellState {
    empty, eat, snake1, snake2;
    public static CellState fromInteger(int x) {
        switch(x) {
            case 0:
                return empty;
            case 1:
                return eat;
            case 2:
                return snake1;
            case 3:
                return snake2;
        }
        return null;
    }
}
