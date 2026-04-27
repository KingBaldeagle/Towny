package com.baldeagle.towny.object;

public abstract class Government extends TownyObject {
    protected double balance = 0.0;
    protected double taxes = 0.0;
    protected boolean taxPercent = false;
    protected String board = "";
    protected String tag = "";
    protected boolean isPublic = true;
    protected boolean isOpen = false;
    protected boolean isNeutral = false;

    protected Government(java.util.UUID uuid, String name) {
        super(uuid, name);
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean collect(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) { balance += amount; }

    public double getTaxes() { return taxes; }
    public void setTaxes(double taxes) { this.taxes = taxes; }
    public void setTaxPercent(boolean taxPercent) { this.taxPercent = taxPercent; }
    public boolean isTaxPercent() { return taxPercent; }

    public String getBoard() { return board; }
    public void setBoard(String board) { this.board = board != null ? board : ""; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag != null ? tag : ""; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }

    public boolean isNeutral() { return isNeutral; }
    public void setNeutral(boolean neutral) { isNeutral = neutral; }
}