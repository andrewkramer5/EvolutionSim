import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import animationwrapper.IntValue;
import creature.Creature;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import world.World;

public class Main extends Application {
	
	Button slowButton;
	Button speedButton;
	Label speedLabel;
	double speed;
	Button pauseAiButton;
	boolean creatureAiPaused;
	Button decreaseMinButton;
	Button increaseMinButton;
	Label minCreaturesLabel;
	int minCreatures;
	GridPane menuPane;
	
	Pane creatureList;
	Pane selectedCreature;
	VBox menusPane;
	
	Canvas map;
	GraphicsContext graphics;
	Pane canvasPane;
	
	Stage window;
	
	Random r;
	Dimension screenSize;
	int MAP_W;
	int MAP_H;
	int tilePixelSize;
	
	World w;
	ArrayList<Creature> creatures;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("2D Evolution Sim");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MAP_W = (int) screenSize.getHeight() - 80;//1000
		MAP_H = (int) screenSize.getHeight() - 80;//1000
		w = new World(50);
		w.generate();
		creatures = new ArrayList<Creature>();
		tilePixelSize = MAP_H / w.getSize();
		r = new Random();
		//System.out.println("Width: " + screenSize.getWidth() + " Height: " + screenSize.getHeight());
		
		speed = 1.0;
		slowButton = new Button("<<");
		slowButton.setFont(new Font(20));
		slowButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (speed > 0.25)
				{
					speed /= 2.0;
					speedLabel.setText(Double.toString(speed) + "x Speed");
				}
			}
		});
		speedButton = new Button(">>");
		speedButton.setFont(new Font(20));
		speedButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (speed < 16.0)
				{
					speed *= 2.0;
					speedLabel.setText(Double.toString(speed) + "x Speed");
				}
			}
		});
		speedLabel = new Label(Double.toString(speed) + "x Speed");
		speedLabel.setFont(new Font(20));
		
		creatureAiPaused = true;
		pauseAiButton = new Button("Start Creature AI");
		pauseAiButton.setFont(new Font(20));
		pauseAiButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				creatureAiPaused = !creatureAiPaused;
				if (creatureAiPaused) {
					pauseAiButton.setText("Start Creature AI");
				} else {
					pauseAiButton.setText("Pause Creature AI");
				}
			}
		});
		
		minCreatures = 0;
		decreaseMinButton = new Button("-5");
		decreaseMinButton.setFont(new Font(20));
		decreaseMinButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (minCreatures > 0) {
					minCreatures -= 5;
					minCreaturesLabel.setText("Min Creatures: " + minCreatures);
				}
			}
		});
		increaseMinButton = new Button("+5");
		increaseMinButton.setFont(new Font(20));
		increaseMinButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				minCreatures += 5;
				minCreaturesLabel.setText("Min Creatures: " + minCreatures);
			}
		});
		minCreaturesLabel = new Label("Min Creatures: " + minCreatures);
		minCreaturesLabel.setFont(new Font(20));
		
		menuPane = new GridPane();
		menuPane.setPadding(new Insets(10, 10, 10, 10));
		menuPane.setVgap(16);
		menuPane.setHgap(20);
		GridPane.setConstraints(slowButton, 0, 0);
		GridPane.setConstraints(speedLabel, 1, 0);
		GridPane.setConstraints(speedButton, 2, 0);
		GridPane.setConstraints(pauseAiButton, 1, 1);
		GridPane.setConstraints(decreaseMinButton, 0, 2);
		GridPane.setConstraints(minCreaturesLabel, 1, 2);
		GridPane.setConstraints(increaseMinButton, 2, 2);
		menuPane.getChildren().addAll(slowButton, speedLabel, speedButton, pauseAiButton, decreaseMinButton, minCreaturesLabel, increaseMinButton);
		menusPane = new VBox();
		menusPane.getChildren().addAll(menuPane);
		
		map = new Canvas(MAP_W, MAP_H);
		graphics = map.getGraphicsContext2D();
		canvasPane = new Pane();
		canvasPane.getChildren().add(map);
		
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: black;");
		layout.setRight(menusPane);
		layout.setCenter(canvasPane);
		
		Scene scene1 = new Scene(layout);
		
		window.setScene(scene1);
		window.setMaximized(true);
		
		for (int i = 0; i < 50; i++) {
			creatures.add(new Creature(MAP_H));
		}
		
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				update();
				render(graphics);
			}
		}.start();
		
		window.show();
	}
	
	public void update() {
		w.update(speed);
		WritableImage pastImage = map.snapshot(null, null);
		PixelReader pr = pastImage.getPixelReader();
		for (int i = 0; i < creatures.size(); i++) {
			Creature temp = creatures.get(i);
			Color under = new Color(0, 0, 0, 1);
			if (temp.getX() >= 0 && temp.getX() <= MAP_H && temp.getY() >= 0 && temp.getY() <= MAP_H) {
				under = w.getWorld()[(int) temp.getY() / tilePixelSize][(int) temp.getX() / tilePixelSize].getColor();
			}
			temp.update(MAP_H, speed, creatureAiPaused, tilePixelSize, under, new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1), new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1));
			//pr.getColor((int) temp.getFLX(), (int) temp.getFLY())
		}
	}
	
	public void render(GraphicsContext g) {
		
		for (int y = 0; y < w.getSize(); y++) {
			for (int x = 0; x < w.getSize(); x++) {
				g.setFill(w.getTile(x, y).getColor());
				g.fillRect(x * tilePixelSize, y * tilePixelSize, tilePixelSize, tilePixelSize);
			}
		}
		
		for (int i = 0; i < creatures.size(); i++) {
			Creature tempC = creatures.get(i);
			g.setFill(Color.BLACK);
			/*
			if (!creatureAiPaused) {
				System.out.println("Creature X: " + tempC.getX() + " Y: " + tempC.getY());
				System.out.println("Creature FL X: " + tempC.getFLX() + " Y: " + tempC.getFLY());
				System.out.println("Creature FR X: " + tempC.getFRX() + " Y: " + tempC.getFRY());
			}*/
			
			g.strokeLine(tempC.getX(), tempC.getY(), tempC.getFLX(), tempC.getFLY());
			g.strokeLine(tempC.getX(), tempC.getY(), tempC.getFRX(), tempC.getFRY());
			g.setFill(tempC.getColor());
			g.fillOval(tempC.getX(), tempC.getY(), tempC.getRadius(tilePixelSize) * 2, tempC.getRadius(tilePixelSize) * 2);
			g.setFill(Color.BLACK);
			g.strokeOval(tempC.getX(), tempC.getY(), tempC.getRadius(tilePixelSize) * 2, tempC.getRadius(tilePixelSize) * 2);
		}
	}
	
	public void spawnCreature() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
