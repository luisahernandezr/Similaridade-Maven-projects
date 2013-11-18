package filesManagement;
import java.io.File;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PomTreatment {



	String versions_Old_F1 []= new String [50];
	String versions_New_F1 []= new String [50];

	/**
	 * @author Luisa Hernández R.
	 * @param typeVersion String that lets know if the version is OLD or NEW
	 * @param filePOM_path String that indicates the path of the POM.xml
	 * 
	 * This method allows to read each file pom.xml
	 */
	public String[] readFile_DOM(String typeVersion , String filePOM_path)
	{	
		 String artifactName_Old_F1 []= new String [50];
	 String artifactName_New_F1 []= new String [50];
	try {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File(filePOM_path));
		doc.getDocumentElement().normalize();

		System.out.println("El elemento raíz es: " + doc.getDocumentElement().getNodeName());
		NodeList listaPlugins = doc.getElementsByTagName("plugin");

		String primerHijo []=new String[listaPlugins.getLength()];
		String segundoHijo []=new String[listaPlugins.getLength()];
		String tercerHijo []=new String[listaPlugins.getLength()];

		String primerHijo_dato []=new String[listaPlugins.getLength()];
		String segundoHijo_dato []=new String[listaPlugins.getLength()];
		String tercerHijo_dato []=new String[listaPlugins.getLength()];

		for (int i = 0; i < listaPlugins.getLength(); i ++) {

			Node persona = listaPlugins.item(i);
			Element elemento = (Element) persona;
			//PRUEBA
			if(elemento.getChildNodes().item(1)!=null)
			{
				primerHijo[i]=elemento.getChildNodes().item(1).getNodeName();
				primerHijo_dato[i]=elemento.getChildNodes().item(1).getTextContent();
				segundoHijo[i]=elemento.getChildNodes().item(3).getNodeName();
				segundoHijo_dato[i]=elemento.getChildNodes().item(3).getTextContent();

				if (elemento.getChildNodes().item(5)==null)
				{
					tercerHijo[i]="NULO";
				}
				else
				{
					tercerHijo[i]=elemento.getChildNodes().item(5).getNodeName();
					tercerHijo_dato[i]=elemento.getChildNodes().item(5).getTextContent();
				}


				if(primerHijo[i].equals("groupId"))	{
					if(segundoHijo[i].equals("artifactId")) {
						if (tercerHijo[i].equals("version"))	{
							if(typeVersion.equals("old"))	{
								artifactName_Old_F1[i]=segundoHijo_dato[i];
								versions_Old_F1[i]=tercerHijo_dato[i];
							}else {
								artifactName_New_F1[i]=segundoHijo_dato[i];
								versions_New_F1[i]=tercerHijo_dato[i];
							}
						}
					}
				}else if(primerHijo[i].equals("artifactId")) {
					if(segundoHijo[i].equals("version")){
						if (typeVersion.equals("old")){
							artifactName_Old_F1[i]=primerHijo_dato[i];
							versions_Old_F1[i]=segundoHijo_dato[i];
						}else	{
							artifactName_New_F1[i]=primerHijo_dato[i];
							versions_New_F1[i]=segundoHijo_dato[i];
						}
					}
				}
			}
		} 
	} catch (Exception e) {
		e.printStackTrace();
	}
	//artifactName_Old_F1=removeNullsFromVector(artifactName_Old_F1);
	return artifactName_Old_F1;

	}

	/**
	 * @author Luisa Hernández R.
	 * @param vectorToRemove vector with nulls to be removed
	 * @return vector without nulls
	 */
	public String [] removeNullsFromVector (String [] vectorToRemove)
	{
		int count_newSize=0;

		for (int i = 0; i < vectorToRemove.length; i++) {
			if(vectorToRemove[i]!=null)
				count_newSize++;
		}

		String vectorWithoutNulls [] = new String[count_newSize];
		for (int j = 0; j < count_newSize; j++) 
		{
			if(vectorToRemove[j]!=null)
				vectorWithoutNulls[j]=vectorToRemove[j];

		}
		return vectorWithoutNulls;
	}

	/**
	 * @author Luisa Hernández R.
	 * void method that compareArtifacts to find the equalnames and pass it as a new vector with just the equasl to be
	 * compare then their versions, so it calls the @method compareVersion() 
	 */
}