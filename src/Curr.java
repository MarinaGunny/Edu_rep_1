public enum Curr {
    RUR("Российский рубль"),
    EUR("ЕВРО"),
    CNY("Китайский юань"),
    USD("Доллар США"),
    SPT("Тамриэльский септим");
    //Наименование валюты
    private String curName;

    Curr(String curName) {
        this.curName = curName;
    }

    @Override
    public String toString() {
        return curName;
    }
}
