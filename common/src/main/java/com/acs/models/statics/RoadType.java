package com.acs.models.statics;

public enum RoadType {

    /**
     * Cars
     */
    PRIMARY, SECONDARY, PRIMARY_LINK, SECONDARY_LINK,
    RESIDENTIAL, TERTIARY, TERTIARY_LINK, TRUNK, TRUNK_LINK,
    CONSTRUCTION,

    /**
     * Pedestrians
     */
    FOOTWAY, PATH, PLATFORM, CROSSING, REST_AREA, STEPS, PEDESTRIAN, ELEVATOR,

    /**
     * Cycles
     */
    CYCLEWAY,

    MOTORWAY, MOTORWAY_LINK,

    LIVING_STREET, SERVICE, SERVICES, ROAD, BUS_STOP,

    RACEWAY, BRIDLEWAY, ABANDONED, CORRIDOR, PLANNED, TRACK,

    /**
     * Others
     */
    YES, NO, PROPOSED, UNCLASSIFIED, UNKNOWN, OTHER;

    public static Boolean isInCarGroup(RoadType roadType){
        return roadType == RoadType.PRIMARY_LINK
                || roadType == RoadType.SECONDARY_LINK
                || roadType == RoadType.PRIMARY
                || roadType == RoadType.SECONDARY
                || roadType == RoadType.RESIDENTIAL
                || roadType == RoadType.TERTIARY
                || roadType == RoadType.TERTIARY_LINK
                || roadType == RoadType.CONSTRUCTION
                || roadType == RoadType.TRUNK
                || roadType == RoadType.TRUNK_LINK
                || roadType == RoadType.UNCLASSIFIED;
    }
}
