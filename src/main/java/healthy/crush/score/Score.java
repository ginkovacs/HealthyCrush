package healthy.crush.score;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Score
{

	private static Document	xmlFile;
	private static File		scoreFile;

	/**
	 * If there's no score.xml file in the home directory, it creates one with scores elements in it. 
	 * 
	 * @throws IOException IOException when an IO error has occurred.
	 */
	public static void createScoreXML() throws IOException
	{

		scoreFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "score.xml");
		if(!scoreFile.exists()) {

			FileOutputStream out = new FileOutputStream(scoreFile);

			Element scoresElem = new Element("scores");
			xmlFile = new Document(scoresElem);

			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.output(xmlFile, out);
			out.close();
		}
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			xmlFile = saxBuilder.build(scoreFile);
		}
		catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the xml file, using the loaded data.
	 * 
	 * @throws IOException when an IO error has occurred.
	 */

	private static void updateXML() throws IOException
	{
		FileOutputStream out = new FileOutputStream(scoreFile);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		outputter.output(xmlFile, out);
		out.close();
	}

	/**
	 * Saves a score element in the xml file, than updates it.
	 * 
	 * @param score the score to be saved
	 */
	public static void saveXML(int score)
	{
		Element scoreElem = new Element("score");

		scoreElem.setText(String.valueOf(score));

		xmlFile.getRootElement().addContent(scoreElem);

		try {
			updateXML();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks, if there is a file to be loaded, and loads in the highest of it's scores.
	 * 
	 * @return highest of the scores
	 */
	public static int loadXML()
	{

		int highscore = 0;

		try {
			createScoreXML();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		Element root = xmlFile.getRootElement();
		for (Element scoreElem : root.getChildren()) {
			int score = Integer.parseInt(scoreElem.getText());
			if(score >= highscore) {
				highscore = score;
			}
		}
		return highscore;
	}

}
