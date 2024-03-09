import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class AccountTest {

    //Тест Имя владельца
    @Test
    void setNameAccTest() {
        Account accGood = new Account("Собакин Дормидонт");
        //Проверка присвоения в конструкторе
        Assertions.assertEquals(accGood.getNameAcc(), "Собакин Дормидонт");
        accGood.setNameAcc("Вася Пупкин");
        //Проверка присвоения не в конструкторе
        Assertions.assertEquals(accGood.getNameAcc(), "Вася Пупкин");
        //Проверка присвоения пустого значения
        Assertions.assertThrows(IllegalArgumentException.class, () -> accGood.setNameAcc(null));
    }

    @Test
    void saveAndLoadTest() {
        Account acc = new Account("Рагнар Рыжий");
        acc.setSaldo(Curr.SPT, 1000);
        acc.setSaldo(Curr.RUR, 3000);
        HashMap<Curr, Integer> originalCurr = acc.getCurrList();

        Loadable qs1 = acc.Save();

        acc.setNameAcc("Элисиф Красивая");
        acc.setSaldo(Curr.EUR, 200);
        acc.setSaldo(Curr.USD, 300);

        qs1.load();

        //Проверка, что загружено старое состояние
        Assertions.assertEquals(acc.getNameAcc(), "Рагнар Рыжий");
        Assertions.assertEquals(acc.getCurrList(), originalCurr);
    }

    //Тест сальдо
    @Test
    void setSaldoTest() {
        Account acc = new Account("Элисиф Красивая");
        HashMap<Curr, Integer> gold = new HashMap<>();
        gold.put(Curr.SPT, 2000);
        gold.put(Curr.CNY, 3000);

        acc.setSaldo(Curr.SPT, 1000);
        acc.setSaldo(Curr.CNY, 3000);
        acc.setSaldo(Curr.SPT, 2000);
        //Проверка на присвоение корректных значений
        Assertions.assertEquals(acc.getCurrList(), gold);
        //Проверка на присвоение некорректных значений
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.setSaldo(Curr.SPT, -100));
    }

    //Тест инкапсуляции
    @Test
    void incapsTest() {
        Account acc = new Account("Вася Пупкин");
        acc.setSaldo(Curr.EUR, 200);
        acc.setSaldo(Curr.USD, 300);
        HashMap<Curr, Integer> gold = acc.getCurrList();
        gold.put(Curr.USD, -200000);
        //Проверка что объект не испортился
        Assertions.assertEquals(acc.getCurrList().get(Curr.USD), 300);
    }

    //Тест undo
    @Test
    void undoTest() throws NothingToUndo {
        Account acc = new Account("Элисиф Красивая");
        //Проверка что после создания отменять пока что нечего
        Assertions.assertThrows(NothingToUndo.class, () -> acc.undo());
        acc.setSaldo(Curr.SPT, 1000);
        acc.setSaldo(Curr.CNY, 3000);
        acc.setSaldo(Curr.SPT, 2000);
        acc.setSaldo(Curr.USD, 5000);
        acc.setNameAcc("Дура страшная");
        acc.undo();
        //Проверка что Элисиф снова красивая
        Assertions.assertEquals(acc.getNameAcc(), "Элисиф Красивая");

        HashMap<Curr, Integer> gold = new HashMap<>();
        gold.put(Curr.SPT, 1000);
        gold.put(Curr.CNY, 3000);
        acc.undo().undo();
        //Проверка, что последние 2 суммы отменены
        Assertions.assertEquals(acc.getCurrList(), gold);
        //Проверка, что отменять нечего
        Assertions.assertThrows(NothingToUndo.class, () -> acc.undo().undo().undo());
    }
}