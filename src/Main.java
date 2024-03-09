public class Main {
    public static void main(String[] args) throws NothingToUndo {
        Account acc = new Account("Вася Пупкин");
        System.out.println(acc.getNameAcc());
        acc.setSaldo(Curr.CNY, 9);
        acc.setSaldo(Curr.SPT, 34);
        acc.setSaldo(Curr.CNY, 23);
        System.out.println(acc.getCurrList());
        //Сохранение+UNDO
        Loadable qs1 = acc.Save();
        acc.setNameAcc("Ярл Балгруф");
        System.out.println(acc.getNameAcc());
        acc.undo();
        System.out.println(acc.getNameAcc());
        acc.undo().undo();
        System.out.println(acc.getCurrList());
        qs1.load();
        System.out.println(acc.getNameAcc());
        System.out.println(acc.getCurrList());

    }
}
