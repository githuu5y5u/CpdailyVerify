package org.example;

import dev.brachtendorf.jimagehash.datastructures.tree.Result;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import dev.brachtendorf.jimagehash.matcher.persistent.database.H2DatabaseImageMatcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] m) throws SQLException, IOException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./database/wisDB"
                , "OmesDoGmEnterATonT", "F69x$4YSlCeLRC&v");
        H2DatabaseImageMatcher db = new H2DatabaseImageMatcher(conn);
        db.addHashingAlgorithm(new PerceptiveHash(64), 0.4);

        BufferedImage buffer = ImageIO.read(new File("./1.jpg"));
        PriorityQueue<Result<String>> results = db.getMatchingImages(buffer);

        ConcurrentMap<String, Double> map = results.stream().filter(r2 ->
                r2.normalizedHammingDistance <= 0.18618
        ).collect(Collectors.toConcurrentMap(result -> result.value, result -> result.normalizedHammingDistance));

        java.util.logging.Logger.getGlobal().info(map.toString());
    }
}
