package io.datasearch.diseasedata.store.dengdipipeline.fuseengine;


import io.datasearch.diseasedata.store.dengdipipeline.models.configmodels.GranularityRelationConfig;
import io.datasearch.diseasedata.store.dengdipipeline.models.granularityrelationmap.GranularityMap;
import io.datasearch.diseasedata.store.dengdipipeline.models.granularityrelationmap.SpatialGranularityRelationMap;
import io.datasearch.diseasedata.store.dengdipipeline.models.granularityrelationmap.TemporalGranularityMap;
import org.geotools.data.DataStore;
import java.util.HashMap;

/**
 * For data fusion.
 */
public class FuseEngine {
    //aggregating
    private DataFrameBuilder dataFrameBuilder;
    //granularityConvertor
    private GranularityConvertor granularityConvertor;
    //granularityRelationMapper
    private GranularityRelationMapper granularityRelationMapper;

    private DataStore dataStore;

    private HashMap<String, GranularityRelationConfig> spatialGranularityConfigs =
            new HashMap<String, GranularityRelationConfig>();

    public FuseEngine(DataStore dataStore) {
        this.dataStore = dataStore;
        this.granularityRelationMapper = new GranularityRelationMapper(this.dataStore);
    }

    public GranularityRelationMapper getGranularityRelationMapper() {
        return this.granularityRelationMapper;
    }

    public HashMap<String, GranularityRelationConfig> getSpatialGranularityConfigs() {
        return spatialGranularityConfigs;
    }

    public HashMap<String, SpatialGranularityRelationMap> buildSpatialGranularityMap(
            HashMap<String, GranularityRelationConfig> granularityRelationConfigs) {

        HashMap<String, SpatialGranularityRelationMap> relationMaps =
                new HashMap<String, SpatialGranularityRelationMap>();

        granularityRelationConfigs.forEach((featureType, config) -> {
            SpatialGranularityRelationMap spatialGranularityRelationMap =
                    granularityRelationMapper.buildSpatialGranularityMap(config);
            relationMaps.put(featureType, spatialGranularityRelationMap);
        });

        return relationMaps;
    }


    public HashMap<String, GranularityMap> buildGranularityMap(
            HashMap<String, GranularityRelationConfig> granularityRelationConfigs) {

        HashMap<String, GranularityMap> granularityMaps = new HashMap<String, GranularityMap>();

        granularityRelationConfigs.forEach((featureType, granularityRelationConfig) -> {

            SpatialGranularityRelationMap spatialMap =
                    this.granularityRelationMapper.buildSpatialGranularityMap(granularityRelationConfig);
            TemporalGranularityMap temporalMap =
                    this.granularityRelationMapper.buildTemporalMap(granularityRelationConfig);

            GranularityMap granularityMap = new GranularityMap(featureType, spatialMap, temporalMap);

            granularityMaps.put(featureType, granularityMap);
        });

        return granularityMaps;
    }
}

