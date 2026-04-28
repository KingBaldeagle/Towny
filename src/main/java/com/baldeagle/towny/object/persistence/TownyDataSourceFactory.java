package com.baldeagle.towny.object.persistence;

import com.baldeagle.towny.Config;

import java.nio.file.Path;

public final class TownyDataSourceFactory {
    private TownyDataSourceFactory() {}

    public static TownyDataSource create() {
        String backend = Config.PERSISTENCE_BACKEND.get();
        Path rootDir = Path.of(Config.DATA_DIRECTORY.get());
        if ("json_snapshot".equalsIgnoreCase(backend)) {
            return new JsonTownyDataSource(rootDir);
        }
        return new DirectoryJsonTownyDataSource(rootDir);
    }
}
