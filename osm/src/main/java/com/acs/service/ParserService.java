package com.acs.service;

import com.acs.models.Location;
import com.acs.models.LocationRange;
import com.acs.models.statics.Road;
import com.acs.models.statics.RoadType;
import com.acs.models.statics.StaticPoint;
import com.acs.models.statics.StaticType;
import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParserService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Value("${osm.map.file.path}")
    private String OSM_MAP_FILE;

    @Getter
    private List<StaticPoint> statics = new ArrayList<>();

    @Getter
    private List<Road> roads = new ArrayList<>();

    @Getter
    private LocationRange locationRange;

    @PostConstruct
    public void postConstruct() {
        try {
            OSMParser p = new OSMParser();

            logger.info("Parsing file: " + OSM_MAP_FILE);
            ClassPathResource classPathResource = new ClassPathResource(OSM_MAP_FILE);

            InputStream inputStream = classPathResource.getInputStream();
            File osmFile = File.createTempFile("map", ".osm");
            FileUtils.copyInputStreamToFile(inputStream, osmFile);

            Map<String, Element> result = p.parse(osmFile);

            parseStatics(result);
            parseWays(result);
            parseLocationRange(osmFile);
        } catch (IOException | SAXException e) {
            logger.error("Unexpected: ", e);
        }
    }

    private void parseStatics(Map<String, Element> result) {
        logger.info("Parsing statics.");
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
        logger.info("Parsing ways.");
        for (String key : result.keySet()) {
            if (key.contains("W")) {
                Element element = result.get(key);
                if (element instanceof Way) {
                    Way way = (Way) element;

                    RoadType roadType = parseRoadType(element);
                    Road road = new Road(roadType);
                    for (Node node : way.getNodes()) {
                        road.addPoint(new Location(node.getLon(), node.getLat()));
                    }

                    roads.add(road);
                }
            }
        }
    }

    private RoadType parseRoadType(Element element) {
        RoadType roadType;
        if (element.getTags().keySet().contains("highway")) {
            String highway = element.getTags().get("highway");
            try {
                roadType = RoadType.valueOf(highway.toUpperCase());
            } catch (IllegalArgumentException e) {
                roadType = RoadType.UNKNOWN;
                logger.warn("Unknown type of road: " + highway);
            }
        } else {
            roadType = RoadType.OTHER;
        }
        return roadType;
    }

    private void parseLocationRange(File file) {
        logger.info("Parsing location range.");
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            org.w3c.dom.Element root = document.getDocumentElement();
            NodeList bounds = root.getElementsByTagName("bounds");
            org.w3c.dom.Node bound = bounds.item(0);

            String minLat = bound.getAttributes().getNamedItem("minlat").getNodeValue();
            String maxLat = bound.getAttributes().getNamedItem("maxlat").getNodeValue();
            String minLng = bound.getAttributes().getNamedItem("minlon").getNodeValue();
            String maxLng = bound.getAttributes().getNamedItem("maxlon").getNodeValue();

            this.locationRange = LocationRange.builder()
                    .minLat(Double.valueOf(minLat))
                    .maxLat(Double.valueOf(maxLat))
                    .minLng(Double.valueOf(minLng))
                    .maxLng(Double.valueOf(maxLng))
                    .build();

        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
