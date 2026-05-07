package com.baldeagle.towny.service;

import com.baldeagle.towny.object.resident.Resident;

public final class TownyPlaceholderService {
    private TownyPlaceholderService() {}

    public static String resolve(Resident resident, String placeholder) {
        if (resident == null) return "";
        return switch (placeholder) {
            case "towny_resident_name" -> resident.getName();
            case "towny_resident_town" -> resident.hasTown() ? resident.getTown().getName() : "none";
            case "towny_resident_nation" -> resident.hasNation() ? resident.getTown().getNation().getName() : "none";
            case "towny_resident_jailed" -> Boolean.toString(resident.isJailed());
            default -> "";
        };
    }
}
