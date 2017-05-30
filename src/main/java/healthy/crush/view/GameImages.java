package healthy.crush.view;

import java.util.HashMap;
import java.util.Map;

import healthy.crush.model.Cell;
import javafx.scene.image.Image;

public class GameImages
{
	Map<Cell, String>	imgs;
	static GameImages	instance	= null;

	public static GameImages getInstance()
	{
		if(instance == null) {
			instance = new GameImages();
		}
		return instance;
	}

	private GameImages()
	{
		super();
		imgs = new HashMap<Cell, String>();
		imgs.put(Cell.EMPTY, "/img/empty.png");
		imgs.put(Cell.CABBAGE, "/img/cabbage.png");
		imgs.put(Cell.CARROT, "/img/carrot.png");
		imgs.put(Cell.CORN, "/img/corn.png");
		imgs.put(Cell.ONION, "/img/onion.png");
		imgs.put(Cell.PAPRIKA, "/img/paprika.png");

	}

	public Image getImage(Cell cell)
	{
		return new Image(imgs.get(cell));
	}

}
