package bustrackerproject.bastrackingsystemv3.model;

import java.util.Arrays;
import java.util.List;

public class Route {
    public static List<String> getRouteCheckpoints(String routeId) {
        if ("A".equalsIgnoreCase(routeId)) {
            return Arrays.asList("KPZ", "FBMK", "FSKTM", "FS", "SPE", "FSTM", "KPZ");
        } else if ("B".equalsIgnoreCase(routeId)) {
            return Arrays.asList("K10", "FBMK", "FSKTM", "FS", "SPE", "FSTM", "K10");
        } else if ("C".equalsIgnoreCase(routeId)) {
            return Arrays.asList("KTMB", "FBMK", "FSKTM", "FS", "SPE", "FSTM", "KTMB");
        }
        return Arrays.asList("Mula");
    }

    public static int getEtaToNextStop(String nextStop) {
        if ("K10".equalsIgnoreCase(nextStop)) return 5;
        if ("KTMB".equalsIgnoreCase(nextStop)) return 10;
        return 3;
    }
}