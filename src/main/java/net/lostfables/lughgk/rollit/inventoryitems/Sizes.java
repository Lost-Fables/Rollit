package net.lostfables.lughgk.rollit.inventoryitems;

public enum Sizes {
    NINE(9),
    EIGHTEEN(18),
    TWENTYSEVEN(27),
    THIRTYSIX(36),
    FOURTYFIVE(45),
    FIFTYFOUR(54);

    private int size;

    Sizes(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
