package com.acs.service;

import com.acs.models.Location;
import com.acs.models.statics.Road;
import com.acs.models.statics.StaticPoint;
import com.acs.models.statics.StaticType;
import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParserService {

    private static final String OSM_MAP_FILE = "map.osm";

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Getter
    private List<StaticPoint> statics = new ArrayList<>();

    @Getter
    private List<Road> roads = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        try {
            OSMParser p = new OSMParser();
            File osmFile = Paths.get(getClass().getClassLoader().getResource(OSM_MAP_FILE).toURI()).toFile();
            Map<String, Element> result = p.parse(osmFile);

            parseStatics(result);
            parseWays(result);
        } catch (URISyntaxException | IOException | SAXException e) {
            logger.error("Unexpected: ", e);
        }
    }

    private void parseStatics(Map<String, Element> result) {
        for (String key : result.keySet()) {
            if (key.contains("N")) {
                Element element = result.get(key);
                if (element instanceof Node) {
                    Node node = (Node) result.get(key);
                    Map<String, String> tags = element.getTags();
                    if (!tags.isEmpty()) {
                        for (String v : tags.values()) {
                            if (v.equals("traffic_signals")) {
                                statics.add(new StaticPoint(new Location(node.getLon(), node.getLat()), StaticType.LIGHTS));
                            } else if (v.equals("crossing")) {
                                statics.add(new StaticPoint(new Location(node.getLon(), node.getLat()), StaticType.CROSSING));
                            } else if (v.contains("stop")) {
                                statics.add(new StaticPoint(new Location(node.getLon(), node.getLat()), StaticType.STOP));
                            }
                        }
                    }
                }
            }
        }
    }

    private void parseWays(Map<String, Element> result) {
        for (String key : result.keySet()) {
            if (key.contains("W")) {
                Element element = result.get(key);
                if (element instanceof Way) {
                    Way way = (Way) element;
                    Road road = new Road();
                    for (Node node : way.getNodes()) {
                        road.addPoint(new Location(node.getLon(), node.getLat()));
                    }
                    roads.add(road);
                }
            }
        }
    }
}
