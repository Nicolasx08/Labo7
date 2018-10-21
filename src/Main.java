import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main extends Application {
    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) {

        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("Graphiques");
        BorderPane bp = new BorderPane();

        MenuItem lignes = new MenuItem("Lignes");
        MenuItem regions = new MenuItem("Regions");
        MenuItem barres = new MenuItem("Barres");
        MenuItem png = new MenuItem("PNG");
        MenuItem gif = new MenuItem("GIF");

        MenuBar menuBar = new MenuBar();
        Menu importer = new Menu("Importer");
        Menu exporter = new Menu("Exporter");

        importer.getItems().addAll(lignes, regions, barres);
        exporter.getItems().addAll(png, gif);
        menuBar.getMenus().addAll(importer, exporter);

        final CategoryAxis mois = new CategoryAxis();
        mois.setLabel("Mois");
        final NumberAxis chiffre = new NumberAxis();
        chiffre.setLabel("Température");
        final LineChart<String,Number> lc = new LineChart(mois,chiffre);
        lc.setTitle("Températures moyennes");

        lignes.setOnAction((event) -> {

            FileChooser fc = new FileChooser();

            lc.getData().addAll(donnees(fc.showOpenDialog(stage)));
            if (lc.getData().size()!=1)
                lc.getData().remove(0);

            bp.setCenter(lc);
        });
        final CategoryAxis mois1 = new CategoryAxis();
        mois1.setLabel("Mois");
        final NumberAxis chiffre1= new NumberAxis();
        chiffre1.setLabel("Température");
        final AreaChart<String,Number> ac = new AreaChart<>(mois1,chiffre1);
        ac.setTitle("Températures moyennes");

        regions.setOnAction((event) -> {

            FileChooser fc = new FileChooser();

            ac.getData().addAll(donnees(fc.showOpenDialog(stage)));
            if (ac.getData().size()!=1)
                ac.getData().remove(0);

            bp.setCenter(ac);
        });

        final CategoryAxis mois2 = new CategoryAxis();
        mois2.setLabel("Mois");
        final NumberAxis chiffre2 = new NumberAxis();
        chiffre2.setLabel("Températures");
        final BarChart<String,Number> bc = new BarChart<>(mois2,chiffre2);

        barres.setOnAction((event) -> {

            bc.setTitle("Températures moyennes");
            FileChooser fc = new FileChooser();

            bc.getData().addAll(donnees(fc.showOpenDialog(stage)));
            if (bc.getData().size()!=1)
                bc.getData().remove(0);

            bp.setCenter(bc);
        });

        png.setOnAction((event) -> {
            if (bp.getCenter()==lc)
                saveAsPng(lc, stage);

            if (bp.getCenter()==ac)
                saveAsPng(ac, stage);

            if (bp.getCenter()==bc)
                saveAsPng(bc, stage);
        });

        gif.setOnAction((event) -> {
            if (bp.getCenter()==lc)
                saveAsGif(lc, stage);

            if (bp.getCenter()==ac)
                saveAsGif(ac,stage);

            if (bp.getCenter()==bc)
                saveAsGif(bc, stage);
        });

        bp.setTop(menuBar);

        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }

    public XYChart.Series donnees(File file){
        try {
            List<String> allLines = Files.readAllLines(Paths.get(file.getPath()));

            XYChart.Series series = new XYChart.Series();
            series.setName("Données");

            String chaine = allLines.get(0);
            String[] mois = chaine.split(",");

            chaine = allLines.get(1);
            String[] temp = chaine.split(",");

            int[] temperature = new int[temp.length];
            for (int i = 0; i < temp.length; i++){
                temperature[i] = Integer.parseInt(temp[i].trim());
            }

            for (int i=0; i<temperature.length; i++){
                series.getData().add(new XYChart.Data(mois[i], temperature[i]));
            }

            return series;
        }catch (Exception e){
            XYChart.Series series = new XYChart.Series();
            series.setName("Données");
            series.getData().add(new XYChart.Data(null, null));
            return series;
        }
    }

    public void saveAsPng(Chart chart, Stage primaryStage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier png", "*.png"
                ));

        File fichier = fc.showSaveDialog(primaryStage);



        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsGif(Chart chart, Stage primaryStage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier gif", "*.gif"
                ));

        File fichier = fc.showSaveDialog(primaryStage);

        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "gif", fichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}