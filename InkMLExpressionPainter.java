import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import org.junit.Assert;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * 
 * Klassen bugger åp InkmlParser.java. Hvor InkMLParser generer symboler for å trene opp en cnn.
 * Denne klassen tegner hele uttrykkene for visualisering av relasjoner.
 * 
 * 
 */
public class InkMLExpressionPainter {

	File inkFile;
	static int savedCounter = 0;
	static int WIDTH = 1000;
	static int HEIGHT = 1000;
	int IMAGESIZE = 28;
	static int MODE=1;
final static int MODE_BUILD_TRAIN = 1;
	final static int MODE_BUILD_TEST = 2;
	static List<InkMLSymbol> tempSymbols;
	static List<InkMLSymbol> symbols;
	static String outpath="";
	private static double xMax=0;
	private static double xMin=100000;
	private static double yMax=0;
	private static double yMin=100000;
	public InkMLExpressionPainter() {

	}
	public static boolean init() throws Exception{
		boolean develop = true;
		int symbolCounter=0;
		String testDataOutput ="/DrawnExpressionINK";
		String testDataPath = "C:\\mldata\\CROHME2019\\subTask_structure\\Train\\Train\\INKMLs\\Train_2014";
		String test = "/KAIST";
		String dbPath = "C:\\mldata\\CROHME2019\\subTask_structure\\Train\\Train\\INKMLs\\Train_2014";
		String oupath = "c:\\mldata\\MathIMG\\ConvertedExpressionINK";
		if (MODE== MODE_BUILD_TRAIN) {
			outpath = oupath;
		}else if(MODE==MODE_BUILD_TEST){
			outpath =testDataOutput;
			oupath = testDataOutput;
			dbPath = testDataPath;
		}
	
		ArrayList<String> dataBaseFilePaths = Tools.buildDataBaseFilePaths(dbPath);
		if(dataBaseFilePaths.isEmpty()) {
		dataBaseFilePaths.add(dbPath);
		}
		System.out.println(dataBaseFilePaths.toString());
		for(String dbfs: dataBaseFilePaths) {
		ArrayList<String> aFilePaths = Tools.buildFileList(dbfs,".inkml");
		System.out.println("Initializing");
		for(String path: aFilePaths) {
			System.out.println("Current path:"+path);
		File inkfile = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(inkfile);
		List<InkMLSymbol> links = new ArrayList<InkMLSymbol>();
		Node nod = doc.getFirstChild();
		NodeList nodes = nod.getChildNodes(); 
		StrokeFactory sf = new StrokeFactory();
		tempSymbols = new ArrayList<InkMLSymbol>();
		symbols = new ArrayList<InkMLSymbol>();
		for(int i =0; i<nodes.getLength();i++) {
			Node n = nodes.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				
				Element el = (Element)n;
			
				switch(n.getNodeName()) {
				case "trace" :{
					InkMLSymbol ims = new InkMLSymbol();
					NamedNodeMap nmm = n.getAttributes();
					Node sid = nmm.getNamedItem("id");
					ims.setXmlID(sid.getNodeValue());
	
					String pairs = n.getTextContent();
					Stroke s = sf.generate(pairs);
					
					ims.addStroke(s);
					tempSymbols.add(ims);
					break;
				}
				case "annotation" :{
		//		System.out.println("annotation:...");
		//		System.out.println(n.getTextContent()+"..."+ n.getNodeValue());
					break;
				}
				case "traceFormat" :{
					break;}
				case "annotationXML" :{
		//			System.out.println("AnnotationXML: break");
					break;}
				case "traceGroup" : {
					//er vi kommet hit så bør id allered eksistere i symboler
					
		//			System.out.println("TraceGroup: investigating");
					String cont = n.getTextContent();
					//System.out.println(cont);
					NodeList trg = n.getChildNodes();
					
					for(int a=0;a<trg.getLength();a++) {
						Node ne = trg.item(a);
						
		//				System.out.println("traceGroup: "+ne.getNodeName()+"..."+ne.getNodeValue());
						switch(ne.getNodeName()) {
						//Secondlayer of tracegroup. The outher describes the expression, the inner
						//describes the symbols of the expression
						case "traceGroup":{
							InkMLSymbol ims = new InkMLSymbol();
							NamedNodeMap nmm = ne.getAttributes();
							Node sid = nmm.getNamedItem("xml:id");
							ims.setXmlID(sid.getNodeValue());
							NodeList subnodes = ne.getChildNodes();
								for(int ab = 0; ab < subnodes.getLength();ab++) {
								Node aNode = subnodes.item(ab);
		//						System.out.println(ab+" av "+subnodes.getLength()+":"+aNode.getNodeName()+":" +aNode.getTextContent());
								switch(aNode.getNodeName()) {
								case ("annotation"): {
									String Label=null;
									NamedNodeMap nnm = aNode.getAttributes();
									Node type = nnm.getNamedItem("type");
									//String sType = aNode.getTextContent();
									String sclean = Tools.CleanString(aNode.getTextContent());								
									//System.out.println("stype:"+sclean+" "+type+ " "+type.getNodeValue());
									
									ims.setLabel(sclean);
									symbolCounter++;
									symbols.add(ims);
											break;}//Truth value of this symbol
								case ("annotationXML"):{
									NamedNodeMap nnm = aNode.getAttributes();
									Node axml = nnm.getNamedItem("href");
									String cleaned = Tools.CleanString(axml.getNodeValue());
		//							System.out.println("annotationXML ref:"+cleaned);
									break;}//describes how this trace fits in the expression?
								case ("traceView"):{
									
									NamedNodeMap nnm = aNode.getAttributes();
									Node tDRF = nnm.getNamedItem("traceDataRef");
			
									ims.addReferenceXMLID(tDRF.getNodeValue());
									break;}//reference to which trace it belongs. Please NOTICE!: a symbol could
												 //consist of multiple traces!! so there could be more then one!
								}
								}
							break;
						}
						case "annotation":{
		//					System.out.println("traceGroup:annotation"+ne.getNodeValue());
							break;
						}
						}
					}
					}
				default :{
		//			System.out.println("end-");
					break;
					}
				
				}
			}
			else {
		//		System.out.println("? not shure what this is ? "+n.getNodeName()+ n.getNodeValue()+ n.getNodeType());
			}
		}
		System.out.println("symbols:"+symbols.size());
		System.out.println("temporary symbols:"+tempSymbols.size());
		
		
		
		mergeTraces();
		//find the expression global max min coordinate on x and y axis
		setSymbolsMaxMin();
		
		//Scale all symbols coordinates to fit within image size
		for(InkMLSymbol ims: symbols)
		{
			
			ims.Scale(1000,0,Tools.Max(xMax,yMax),Tools.Min(xMin,yMin));
		}	
		drawImage(path);
		listSymbols();
		
		}
		System.out.println("Num items in symbols:"+symbols.size());
		System.out.println("Files:"+aFilePaths.size());
		System.out.println("Symbols:"+symbolCounter);
		System.out.println("Sent to save:"+savedCounter);
		
		int[] ff = Tools.numFilesAndFolders(oupath);
		//Tools.buildFileList(test, ".inkml");	
		System.out.println(outpath+" :Files:"+ff[0]);
		System.out.println(outpath+" :Folders:"+ff[1]);
		System.out.println("input: "+dbPath);
		System.out.println("Initialized");
		
		}
		return true;
	}
	

	public static void setSymbolsMaxMin() {
		for(InkMLSymbol ims: symbols) {
			if(ims.Max().x > xMax) {xMax = ims.Max().x;}
			if(ims.Max().y > yMax) {yMax = ims.Max().y;}
			if(ims.Min().y < yMin) {yMin = ims.Min().y;}
			if(ims.Min().x < xMin) {xMin = ims.Min().x;}
		}
	}
	
	
	public static void main(String[] args) {
	
		try {
			if(symbols==null) {
				symbols = new ArrayList<InkMLSymbol>();
			}
		init();
		//mergeTraces();
		//drawSymbols();
		//listSymbols();
		//cleanUpCoordinates();
          
	}catch(Exception e) {
		e.printStackTrace();
		}
		}

	
	private static void listSymbols() {
		for(InkMLSymbol im: symbols) {
			String addit = "";
			for(String ia: im.refXmlId) {
				addit += ia+", ";
			}
		System.out.println("Symbol: id"+im.getXmlId()+" Label:"+im.getLabel()+". refered: "+addit);
		}
		
		
	}

	public static void drawImage(String path) {
		String filename="impInk";
		BufferedImage bim=null;
	int a=0;


	System.out.println("Painting all symbols of exp:");
	 bim = new BufferedImage(1000,1000,BufferedImage.TYPE_BYTE_BINARY);

     Graphics2D g = bim.createGraphics();
	for(InkMLSymbol iml: symbols) {

			a++;
       
        ArrayList<Stroke> als = (ArrayList<Stroke>) iml.getStrokes();
        for(Stroke s: als) {
       	 ArrayList<Point> poi = (ArrayList<Point>) s.getPoints();
       	 for(int i=0; i<poi.size()-1;i++) {
       	 Point p1 = poi.get(i);
       	 Point p2 = poi.get(i+1);
       	 java.awt.Stroke str = new java.awt.BasicStroke(3);
       	 g.setStroke(str);
       	 g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
       	 }
       	 }
        //BufferedImage scaled = Tools.ScaleImage(bim,56,56);
		}
       // BufferedImage bscaled = ImageTools.ImageScale(bim);
       int endIng = path.lastIndexOf("/") ;
        String endPath = path.substring(endIng);
    	Path pa = Paths.get(outpath+"/"+endPath+"/");
    	
    	if(Files.isDirectory(pa)) {
    	}else {
    		//File f = new File(outpath+"/"+endPath);
    		//f.mkdir();

    	}
        Tools.saveImage(bim,outpath+"/"+endPath,"jpg");
        savedCounter++;

        System.out.println("finished: "+symbols.size());
}
	
	public List<InkMLSymbol> getSymbols() {
		System.out.println("return inkmlSymbol: "+symbols.size()+" items");
		return this.symbols;
	}
	
	
	//
	public static void cleanUpCoordinates() {
		//After scaling there exist some ambigious lines
		//So let's convert coordinates to int and remove lines that is
		//close to each other
		
		for(InkMLSymbol im: symbols) {
			for(Stroke s: im.strokes) {
				for(Point p: s.points) {
					p.convertCoordToIntValue();
				}
			}
		}
	}
// Draw the strokes of a symbol
	public static void drawSymbol(InkMLSymbol ims, Graphics2D g) {
		for(Stroke s: ims.strokes) {
			ArrayList<Point> pos = (ArrayList<Point>) s.getPoints();
			for(int i=0; i<pos.size()-1;i++) {
			Point p = pos.get(i);
			Point o2 = pos.get(i+1);
				g.drawLine((int)p.x,(int)p.y,(int)o2.x,(int)o2.y);
			}
		}	
	}
	//merge traces into one symbol
	public static void mergeTraces() {
		//for hvert symbol i symbol - hent traces fra tempSymbols hvor
		//symbol refxmlid = tempSymbol id
		
		for(InkMLSymbol ims: symbols) {
			ArrayList<String> symRefs = (ArrayList<String>) ims.getRefXMLID();
			for(String sid: symRefs) {
				for(InkMLSymbol tim: tempSymbols) {
					String tid = tim.getXmlId();
					if(sid.equals(tid)) {
						for(Stroke s: tim.getStrokes()) {
						ims.addStroke(s);
						}
					}
				}
			}
		}
	}
}
