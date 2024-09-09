package ch.zhaw.prog2.account;

class AccountTransferTask implements Runnable {

    private final Account fromAccount;
    private final Account toAccount;
    private final int amount;
    private int totalTransfer = 0;

    public AccountTransferTask(Account fromAccount, Account toAccount, int amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public int getTotalTransfer() {
        return this.totalTransfer;
    }

    @Override
    public void run() {
        //transfer();
        transferDLfree();
    }

    /* b) ensure no lost updates are happening */
    public void transfer() {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                // Account must not be overdrawn
                if (fromAccount.getBalance() >= amount) {
                    fromAccount.transferAmount(-amount);
                    toAccount.transferAmount(amount);
                    totalTransfer += amount;
                }
            }
        }
    }

    /* c) deadlock free using ascending order of IDs */
    public void transferDLfree() {
        // This implementation is deadlock free using the second possibility
        // -> locking of accounts in ascending order of accountID
        boolean isLower = fromAccount.getId() < toAccount.getId();
        Account lowerAccount = isLower ? fromAccount : toAccount;
        Account higherAccount = !isLower ? fromAccount : toAccount;
        synchronized (lowerAccount) {
            synchronized (higherAccount) {
                // Account must not be overdrawn
                if (fromAccount.getBalance() >= amount) {
                    fromAccount.transferAmount(-amount);
                    toAccount.transferAmount(amount);
                    totalTransfer += amount;
                }
            }
        }
    }
}
