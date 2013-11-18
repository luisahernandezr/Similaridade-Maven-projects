package controller;

import jaccardCoefficient.JaccardCoefficient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;








import filesManagement.PomTreatment;

public class PrincipalWindow extends JFrame {

	/**
	 * 
	 */


	String[] pomPathList= new String[101];
	//Matriz de similaridad
	String matrixSimilarity [][]= new String [100][100];
	
	double matrixSimilarityDouble[][]= new double [100][100];

	JaccardCoefficient insJaccardCoefficient = new JaccardCoefficient();

	private static final long serialVersionUID = 001;
	JPanel pdatos;
	JLabel limagen,labelName;
	String directoryNames="";

	//Ruta Destino de proyecto old y es ahi donde estara el POM.xml old

	String pathDestOlder="C:\\older";

	//Rutas de cada POM.XML tanto del sw antiguo como del nuevo
	public String path_POM_older=pathDestOlder+"\\pom.xml";	

	//Aqui se guardara un reporte en cada una de las versiones
	String path_FileRecommendation=pathDestOlder+"\\recommendation.txt";

	int contaOld=0;
	int contaNew=0;

	//inicializa instancia para manejo de los archivos pom.xml
	PomTreatment insFileReading= new PomTreatment();

	//Maneja los atributos gráficos para leer el archivo recomendación.
	FileInputStream pathFileRecommendation_inStream;
	BufferedReader bufferedReader_FileRecommendation;
	JTextArea textArea_recommendation;
	String textAreatoShow = " ";
	JScrollPane scrollArea;
	//maneja los botones para abrir proyectos OLD y NEW
	JPanel software_old;	
	JButton btm_OpenRepository;
	JPanel software_new;
	JButton btmOpen_swNew;


	public void ListDirectories( File rutaSeleccionada)
	{
		File dir = rutaSeleccionada;

		String[] ficheros = dir.list();

		if (ficheros == null)
			System.out.println("No hay ficheros en el directorio especificado");
		else { 
			for (int x=0;x<ficheros.length;x++)
			{
				System.out.println(ficheros[x]);
				File filePOM = new File(dir+"\\"+ficheros[x]+"\\pom.xml");

				if (filePOM.exists())
				{
					pomPathList[x]=dir+"\\"+ficheros[x]+"\\pom.xml";
				}

				System.out.println("pom path  "+pomPathList[x]);
			}
		}
	}


	/**
	 * This method manage all the principal window interface and call method to do the plugins recommendation
	 */
	public PrincipalWindow()
	{

		//Declaration of Labels and buttoms
		software_old=new JPanel();		
		software_old.add(new JLabel("Select repository: "));
		//software_new=new JPanel();
		//software_new.add(new JLabel("Select new software: "));

		btm_OpenRepository=new JButton("Open Repository");
		//btmOpen_swNew=new JButton("Open New");

		//activates file chooser for user and call method to copy directories
		fileChooserAction(btm_OpenRepository);
		//fileChooserAction(btmOpen_swNew, "newVersion");

		//add buttoms to labels
		software_old.add(btm_OpenRepository);
		//software_new.add(btmOpen_swNew); 

		pdatos=new JPanel();
		pdatos.add(new JLabel("Repository name: "));
		labelName=new JLabel();
		pdatos.add(labelName);

		add(software_old,BorderLayout.WEST);
		//add(software_new,BorderLayout.EAST);
		//add(recommendationPanel,BorderLayout.WEST);
		add(pdatos);
	}



	/**
	 * this method calls the rigth methods when the bottoms are chosen, and lets select the OLD and new projects for treating them
	 * @param btmType tipo de boton oprimido old/new
	 * @param inputVersion Cadena de entrada que indica si es old/new para asi mismo elegir ruta destino
	 */
	public void fileChooserAction(JButton btmType)
	{
		JButton type=btmType;
		type.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser selector=new JFileChooser();
				selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int r=selector.showOpenDialog(null);
				if(r==JFileChooser.APPROVE_OPTION){
					try {
						File fileSelected=selector.getSelectedFile();

						//Ruta Fuente de proyectos a copiar
						File ruta= new File (fileSelected.getAbsolutePath());
						System.out.println("ruta "+ruta);

						//Validación si es nuevo o antoguo para guardar separados proyectos en ruta destino, con eso saber donde queda cada archivo POM


						ListDirectories(ruta);
						double similarityValue;
						String pomPathList_withoutNulls[]=insFileReading.removeNullsFromVector(pomPathList);
						System.out.println("tamaaa"+pomPathList_withoutNulls.length);


						for (int i = 0; i < pomPathList_withoutNulls.length; i++)
						{
							for (int j = 0; j < pomPathList_withoutNulls.length; j++) 
							{
								if (pomPathList_withoutNulls[i]!=null && pomPathList_withoutNulls[j]!=null)
								{
									String artifacListPerPOM_A[]=insFileReading.readFile_DOM("old",pomPathList_withoutNulls[i]);
									String artifactLisPerPOM_B[]=insFileReading.readFile_DOM("old",pomPathList_withoutNulls[j]);

									for (int k = 0; k < artifactLisPerPOM_B.length; k++) 
									{
										System.out.println("lista A "+artifacListPerPOM_A[k]+ "  lista B  "+ artifactLisPerPOM_B[k]);
									}
									System.out.println(artifacListPerPOM_A.length);
									System.out.println(artifactLisPerPOM_B.length);

									System.out.println("i  "+i+"j  "+j+" pomI "+pomPathList_withoutNulls[i]+"  pomJ "+pomPathList_withoutNulls[j]);


									if(i==j)
									{
										matrixSimilarity[i][j]="1.00";
										matrixSimilarityDouble[i][j]=1.00;

									}else
									{
										similarityValue=insJaccardCoefficient.similarity(artifacListPerPOM_A, artifactLisPerPOM_B);
										String sinPuntos=String.format("%.2f", similarityValue);

										System.out.println("similarityyyy "+similarityValue);
										matrixSimilarity[i][j]=""+sinPuntos;
										
										//Tiene que ser con puntos y no con , como se hace al volverlo String
										matrixSimilarityDouble[i][j]=similarityValue;
									}
								}
							}
						}

						for (int i = 0; i < matrixSimilarity.length; i++) {
							for (int j = 0; j < matrixSimilarity.length; j++) {
								System.out.print("  "+matrixSimilarity[i][j]);
							}		
							System.out.println();
						}

						//String con nombre de cada uno de los directorios seleccionados
						directoryNames=directoryNames+" \n "+fileSelected.getName();						
						labelName.setText(directoryNames);
					
					
						printSimilarityMatrix();
						//INTENTOOOOOOOOOOOOOO
						writeCVSFile_Matrix();
						converCVStoXLS_Matrix();
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} );
	}
	
	
	public void converCVStoXLS_Matrix()
	{
		try {
	        String csvFileAddress = "WriteTest.csv"; //csv file address
	        String xlsxFileAddress = "test.xlsx"; //xlsx file address
	        XSSFWorkbook workBook = new XSSFWorkbook();
	        XSSFSheet sheet = workBook.createSheet("sheet1");
	        String currentLine=null;
	        int RowNum=0;
	        BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
	        while ((currentLine = br.readLine()) != null) {
	            String str[] = currentLine.split(",");
	            RowNum++;
	            XSSFRow currentRow=sheet.createRow(RowNum);
	            for(int i=0;i<str.length;i++){
	                currentRow.createCell(i).setCellValue(str[i]);
	            }
	        }

	        FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
	        workBook.write(fileOutputStream);
	        fileOutputStream.close();
	        System.out.println("Done");
	    } catch (Exception ex) {
	        System.out.println(ex.getMessage()+"Exception in try");
	    }
		/*
		Workbook wb = new HSSFWorkbook();
        CreationHelper helper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");

        CSVReader reader = new CSVReader(new FileReader("data.csv"));
        String[] line;
        int r = 0;
        while ((line = reader.readNext()) != null) {
            Row row = sheet.createRow((short) r++);

            for (int i = 0; i < line.length; i++)
                row.createCell(i)
                   .setCellValue(helper.createRichTextString(line[i]));
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();*/
	}
	
	public void writeCVSFile_Matrix() throws IOException
	{
	
		FileWriter fw = new FileWriter("WriteTest.csv");
		   PrintWriter out = new PrintWriter(fw);
		
		   
		   String strLinea="";
		   for (int i_Fila = 0; i_Fila < matrixSimilarityDouble.length; i_Fila++) {
				
				for (int j_Column = 0; j_Column < matrixSimilarityDouble.length; j_Column++)
				{
					strLinea=("  "+matrixSimilarityDouble[i_Fila][j_Column]);
					out.print(strLinea);
					if(j_Column==matrixSimilarityDouble.length-1)
						System.out.println("termina columna");
					else
						out.print(",");
				}		
				 out.println("");
			}
					
			
		   
		   
		   
		      
		   //Flush the output to the file
		   out.flush();
		       
		   //Close the Print Writer
		   out.close();
		       
		   //Close the File Writer
		   fw.close();       
		   
	}
	
	/**
	 * this method writes the @file recommendation.txt with the final result of the program and add the
	  content of the file to the window.
	 * @throws IOException this is for treating the file exception
	 */
	public void printSimilarityMatrix( )
	{
		textArea_recommendation = new JTextArea(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		
			
		   String strLinea="";
			for (int i = 0; i < matrixSimilarity.length; i++) {
	
				for (int j = 0; j < matrixSimilarity.length; j++)
				{
					strLinea=("  "+matrixSimilarity[i][j]);
					textAreatoShow=textAreatoShow+strLinea;
				}		
				textAreatoShow=textAreatoShow+strLinea+"\n";
			}
					
			
			
		

		textArea_recommendation.setText(textAreatoShow);
		textArea_recommendation.setEditable(false);

		scrollArea = new JScrollPane(textArea_recommendation, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollArea, BorderLayout.CENTER);
		//btm_OpenRepository.setEnabled(false);
		
	
	}



}