package com.dockerapp.database;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SchemaMigrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaMigrator.class);

    private Flyway flyway;
    private boolean applyChanges;
    private boolean dropSchema;
    private String location;
    private String locationTest;
    private boolean loadTestData;

    public void migrate() {
        List<String> locations = new ArrayList<>();
        locations.add(location);

        if (loadTestData) {
            locations.add(locationTest);
        }

        if (dropSchema) {
            Preconditions.checkState(applyChanges, "can't drop schema if apply changes is false");
            LOGGER.info("dropping schema");
            flyway.clean();
        }

        flyway.setOutOfOrder(loadTestData);
        flyway.setLocations(locations.toArray(new String[locations.size()]));

        if (applyChanges) {
        // LOGGER.info("applying {} changes to database", flyway.info().pending().length);
            flyway.migrate();
        }

        validateMigration();
    }

    private void validateMigration() {
        LOGGER.info("validating DB migration");
        MigrationInfoService info = flyway.info();
        Set<MigrationInfo> missingMigrations = Sets.difference(Sets.newHashSet(info.all()), Sets.newHashSet(info.applied()));
        Preconditions.checkState(missingMigrations.isEmpty(),
                "the following change scripts are not applied, please apply them before attempting to run this application: %s",
                getMigrationNames(missingMigrations));
        flyway.validate();
    }

    private List<String> getMigrationNames(Set<MigrationInfo> migrations) {
        List<MigrationInfo> migrationInfos = Lists.newArrayList(migrations);
        Collections.sort(migrationInfos);
        return Lists.transform(migrationInfos, new Function<MigrationInfo, String>() {
            @Override
            public String apply(MigrationInfo migration) {
                return String.format("\n%s", migration.getScript());
            }
        });
    }

    public Flyway getFlyway() {
        return flyway;
    }

    public void setFlyway(Flyway flyway) {
        this.flyway = flyway;
    }

    public boolean isApplyChanges() {
        return applyChanges;
    }

    public void setApplyChanges(boolean applyChanges) {
        this.applyChanges = applyChanges;
    }

    public boolean isDropSchema() {
        return dropSchema;
    }

    public void setDropSchema(boolean dropSchema) {
        this.dropSchema = dropSchema;
    }

    public boolean isLoadTestData() {
        return loadTestData;
    }

    public void setLoadTestData(boolean loadTestData) {
        this.loadTestData = loadTestData;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationTest() {
        return locationTest;
    }

    public void setLocationTest(String locationTest) {
        this.locationTest = locationTest;
    }
}
