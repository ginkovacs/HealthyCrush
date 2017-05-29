package healthy.crush.view;

import javafx.scene.image.ImageView;

public class CellImageView extends ImageView
{
	private PositionImageView position;

	public CellImageView()
	{

	}

	public CellImageView(int row, int col)
	{
		super();
		position = new PositionImageView(row, col);
	}

	public PositionImageView getPosition()
	{
		return position;
	}

	public void setPosition(PositionImageView position)
	{
		this.position = position;
	}

}
