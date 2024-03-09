import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

class NothingToUndo extends Exception {
    public NothingToUndo() {
        super();
    }

    public NothingToUndo(String message) {
        super(message);
    }
}

public class Account {
    // Имя владельца счета
    private String nameAcc;

    public String getNameAcc() {
        return nameAcc;
    }

    public void setNameAcc(String nameAcc) {
        if (nameAcc == null || nameAcc.isEmpty())
            throw new IllegalArgumentException("Имя владельца не должно быть пустым");
        String oldName = this.nameAcc;
        //Т.к. метод вызывается в т.ч. в конструкторе, где значение по умолчанию null,
        //которое нас не устроаивает, его не пушим
        if (oldName != null) {
            commands.push(() -> this.nameAcc = oldName);
        }
        this.nameAcc = nameAcc;
    }

    // Список остатков по валютам
    private HashMap<Curr, Integer> currList;

    public HashMap<Curr, Integer> getCurrList() {
        return new HashMap<Curr, Integer>(this.currList);
    }

    private Deque<Command> commands = new ArrayDeque<>();

    public Loadable Save() {
        return new Snapshot();
    }

    private class Snapshot implements Loadable {
        private String nameAcc;
        private HashMap<Curr, Integer> currList;

        public Snapshot() {
            this.nameAcc = Account.this.nameAcc;
            this.currList = new HashMap<>(Account.this.currList);
        }

        @Override
        public void load() {
            Account.this.nameAcc = this.nameAcc;
            Account.this.currList = new HashMap<>(this.currList);
        }
    }

    //Конструктор
    public Account(String name) {
        setNameAcc(name);
        this.currList = new HashMap<>();
    }

    //Установить остаток по валюте
    public void setSaldo(Curr curr, Integer saldo) {
        if (saldo < 0) throw new IllegalArgumentException("Значение остатка не может быть отрицательным");
        if (currList.containsKey(curr)) {
            Integer oldSaldo = this.currList.get(curr);
            commands.push(() -> this.currList.put(curr, oldSaldo));
        } else {
            commands.push(() -> this.currList.remove(curr));
        }
        this.currList.put(curr, saldo);
    }

    //UNDO
    public Account undo() throws NothingToUndo {
        if (this.commands.isEmpty()) throw new NothingToUndo("Вы находитесь в исходном состоянии. Отмена невозможна");
        this.commands.pop().perform();
        return this;
    }
}
