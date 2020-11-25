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

public class InkMLParser {

	File inkFile;
	static int savedCounter = 0;
	static int WIDTH = 100;
	static int HEIGHT = 100;
	int IMAGESIZE = 28;
	static int MODE=1;
final static int MODE_BUILD_TRAIN = 1;
	final static int MODE_BUILD_TEST = 2;
	static List<InkMLSymbol> tempSymbols;
	static List<InkMLSymbol> symbols;

	static String outpath="";
	
	public InkMLParser() {
	}
	
	public static boolean init() throws Exception{
		int symbolCounter=0;
		
		String dataPath = "C:\\mldata\\CROHME2019\\subTask_structure\\Train\\Train\\INKMLs\\Train_2014";
	
		String dbPath = dataPath;
		String oupath = "c:\\mldata\\MathIMG\\ConvertedINK";
		if (MODE== MODE_BUILD_TRAIN) {
			outpath = oupath;
		}else if(MODE==MODE_BUILD_TEST){
			//outpath =dataOutput;
			//oupath = testDataOutput;
			//dbPath = testDataPath;
		}

		ArrayList<String> dataBaseFilePaths = Tools.buildDataBaseFilePaths(dbPath);
		
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
				//String id = n.getAttributes().getNamedItem("id").getNodeValue();
				//System.out.print("element:"+el);
				switch(n.getNodeName()) {
				case "trace" :{
					InkMLSymbol ims = new InkMLSymbol();
					NamedNodeMap nmm = n.getAttributes();
					Node sid = nmm.getNamedItem("id");
					ims.setXmlID(sid.getNodeValue());
					
		//			System.out.println("trace: extract pairs "+ sid);
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
		//This asumes that a symbols should be drawn from 0,0 in a cartesian coordinated system
		//Maybe we should concider to move the symbol so it is centered
		mergeTraces();
		for(InkMLSymbol ims: symbols)
		{
			Point max = ims.Max();
			Point min = ims.Min();
			Point p = Tools.buildScalingFactors(max,min,28,28);
			Point tf = Tools.buildMoveToZeroFactors(max,min);
			Point mc = Tools.buildMoveToCenterFactor(max,min,WIDTH,HEIGHT);
			ims.translate(tf);
			ims.translate(mc);
			max = ims.Max();
			min = ims.Min();
			ims.Scale(100,0,Tools.Max(max.x,max.y),Tools.Min(min.x,min.y));
		}		
		drawSymbols();
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

	//Draws InkML symbols to jpg files,
	//TODO:Need a mechanism in import to get the ground-truth of the object
	public static void drawSymbols() {
		String filename="impInk";
		
	//String path = System.getProperty("user.dir")+"/ConvertedINK";
	
	int a=0;
	System.out.println("Painting all symbols of exp:");
		for(InkMLSymbol iml: symbols) {
			//System.out.println(a);
			Path pa = Paths.get(outpath+"/"+iml.getLabel()+"/");
			
			if(Files.isDirectory(pa)) {
				//Folder exist, do nothing
				//System.out.println("Exist: "+path+"..."+iml.getLabel());
			}else {
				File f = new File(outpath+"/"+iml.getLabel());
				f.mkdir();

			}
			a++;
        BufferedImage bim = new BufferedImage(100,100,BufferedImage.TYPE_BYTE_BINARY);
        
        Graphics2D g = bim.createGraphics();
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
        BufferedImage bscaled = ImageTools.ImageScale(bim);
Tools.saveImage(bscaled,outpath+"/"+iml.getLabel()+"/"+a+savedCounter,"jpg");
        savedCounter++;
		}
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
