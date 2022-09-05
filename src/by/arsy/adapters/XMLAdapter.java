package by.arsy.adapters;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public final class XMLAdapter {

    private static int id;
    private static String gamerVisible;
    private static String ballVisible;
    private static String spiceVisible;
    private static String wallVisible;
    private static String coinVisible;
    private static int startSizeGamers;

    private static int rows;
    private static int cols;
    private static int keyKodeLeftG1;
    private static int keyKodeRightG1;
    private static int keyKodeLeftG2;
    private static int keyKodeRightG2;

    private static long pauseForStepBall;
    private static long bustBallSpeed;
    private static long timerSpeedUp;

    private static int bustAmountWalls;
    private static long pauseBuildWallSpeed;

    static {
        loadNodes();
    }

    private XMLAdapter() {
    }

    private static void loadNodes() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder document = factory.newDocumentBuilder();
            Document options = document.parse("resources/options.xml");
            NodeList optionsList = options.getElementsByTagName("options");
            id = writeGamerId(optionsList, options);
            NodeList op = optionsList.item(0).getChildNodes();
            for (int i = 0; i < op.getLength(); i++) {
                Node node = op.item(i);
                switch (node.getNodeName()) {
                    case "tennis_runner" -> writeTennisRunnerOptions(node);
                    case "window_size" -> writeGameWindowOptions(node);
                    case "control_button_codes" -> writeControlButtonOptions(node);
                    case "ball" -> writeBallOptions(node);
                    case "walls" -> writeWallsOptions(node);

                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static int writeGamerId(NodeList optionsList, Document doc) {
        Node item = optionsList.item(0).getAttributes().item(0);
        int id = Integer.parseInt(item.getTextContent());
        if (id == 0) {
            id = JBDCAdapter.createNewGamers();
            item.setNodeValue(String.valueOf(id));
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("resources/options.xml"));
                transformer.transform(source, result);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    private static void writeTennisRunnerOptions(Node attributes) {
        NamedNodeMap nodeMap = attributes.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "gamer_visible" -> gamerVisible = node.getTextContent();
                case "ball_visible" -> ballVisible = node.getTextContent();
                case "spice_visible" -> spiceVisible = node.getTextContent();
                case "wall_visible" -> wallVisible = node.getTextContent();
                case "coin_visible" -> coinVisible = node.getTextContent();
                case "start_size_gamers" -> startSizeGamers = Integer.parseInt(node.getTextContent());
            }
        }
    }

    private static void writeGameWindowOptions(Node attributes) {
        NamedNodeMap nodeMap = attributes.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "rows" -> rows = Integer.parseInt(node.getTextContent());
                case "cols" -> cols = Integer.parseInt(node.getTextContent());
            }
        }
    }

    private static void writeControlButtonOptions(Node attributes) {
        NamedNodeMap nodeMap = attributes.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "gamer1left" -> keyKodeLeftG1 = Integer.parseInt(node.getTextContent());
                case "gamer1right" -> keyKodeRightG1 = Integer.parseInt(node.getTextContent());
                case "gamer2left" -> keyKodeLeftG2 = Integer.parseInt(node.getTextContent());
                case "gamer2right" -> keyKodeRightG2 = Integer.parseInt(node.getTextContent());
            }
        }
    }

    private static void writeBallOptions(Node attributes) {
        NamedNodeMap nodeMap = attributes.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "bust_speed" -> bustBallSpeed = Long.parseLong(node.getTextContent());
                case "step_pause" -> pauseForStepBall = Long.parseLong(node.getTextContent());
                case "timer_speed_up" -> timerSpeedUp = Long.parseLong(node.getTextContent());
            }
        }
    }

    private static void writeWallsOptions(Node attributes) {
        NamedNodeMap nodeMap = attributes.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            switch (node.getNodeName()) {
                case "bust_amount" -> bustAmountWalls = Integer.parseInt(node.getTextContent());
                case "pause_build" -> pauseBuildWallSpeed = Long.parseLong(node.getTextContent());
            }
        }
    }

    public static int getId() {
        return id;
    }

    public static String getGamerVisible() {
        return gamerVisible;
    }

    public static String getBallVisible() {
        return ballVisible;
    }

    public static String getSpiceVisible() {
        return spiceVisible;
    }

    public static String getWallVisible() {
        return wallVisible;
    }

    public static String getCoinVisible() {
        return coinVisible;
    }

    public static int getStartSizeGamers() {
        return startSizeGamers;
    }

    public static long getPauseForStepBall() {
        return pauseForStepBall;
    }

    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    public static int getKeyKodeLeftG1() {
        return keyKodeLeftG1;
    }

    public static int getKeyKodeRightG1() {
        return keyKodeRightG1;
    }

    public static int getKeyKodeLeftG2() {
        return keyKodeLeftG2;
    }

    public static int getKeyKodeRightG2() {
        return keyKodeRightG2;
    }

    public static long getBustBallSpeed() {
        return bustBallSpeed;
    }

    public static long getTimerSpeedUp() {
        return timerSpeedUp;
    }

    public static int getBustAmountWalls() {
        return bustAmountWalls;
    }

    public static long getPauseBuildWallSpeed() {
        return pauseBuildWallSpeed;
    }
}



