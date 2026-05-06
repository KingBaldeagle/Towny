package com.baldeagle.towny.object.economy;

public record TownyTransactionRecord(
    String type,
    String fromAccount,
    String toAccount,
    long amountInCopper,
    boolean success,
    long timestampEpochMs,
    String details
) {}
