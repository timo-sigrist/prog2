package ch.zhaw.prog2.account;

public class Account {
    private final int id;
    private int balance = 0;

    public Account(int id, int initialAmount) {
        this.id = id;
        this.balance = initialAmount;
    }

    public int getId() {
        return id;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public synchronized void transferAmount(int amount) {
        this.balance += amount;
    }
}
